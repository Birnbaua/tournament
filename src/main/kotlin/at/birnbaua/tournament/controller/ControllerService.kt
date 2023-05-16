package at.birnbaua.tournament.controller

import at.birnbaua.tournament.data.document.*
import at.birnbaua.tournament.data.document.template.TournamentTemplate
import at.birnbaua.tournament.data.service.*
import at.birnbaua.tournament.data.service.feizi.SimpleOrderService
import at.birnbaua.tournament.data.service.feizi.SimpleResult
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import at.birnbaua.tournament.data.service.gen.MatchGeneratingService
import at.birnbaua.tournament.data.service.gen.TournamentGeneratingService
import at.birnbaua.tournament.exception.ResourceNotFoundException
import at.birnbaua.tournament.exception.TournamentException
import at.birnbaua.tournament.pdf.PdfService
import javassist.Loader.Simple
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple3
import java.time.LocalDateTime

@Service
class ControllerService {

    private val log: Logger = LoggerFactory.getLogger(ControllerService::class.java)

    //data services
    @Autowired private lateinit var tts: TournamentTemplateService
    @Autowired private lateinit var trs: TournamentService
    @Autowired private lateinit var grs: GameroundService
    @Autowired private lateinit var fs: FieldService
    @Autowired private lateinit var ts: TeamService
    @Autowired private lateinit var ms: MatchService

    //generating services
    @Autowired private lateinit var sos: SimpleOrderService
    @Autowired private lateinit var tgs: TournamentGeneratingService
    @Autowired private lateinit var ggs: GameroundGeneratingService
    @Autowired private lateinit var mgs: MatchGeneratingService
    @Autowired private lateinit var pdfService: PdfService

    fun generateAndSaveTournamentWithTeamsAndFields(template: String, tournamentId: String, teams: Int?, fields: Int?) : Mono<Tuple3<Tournament,List<Team>,List<Field>>> {
        return tts.findById(template).flatMap { generateAndSaveTournamentWithTeamsAndFields(it,tournamentId,teams, fields) }
    }

    fun generateAndSaveTournamentWithTeamsAndFields(template: TournamentTemplate, tournamentId: String, teams: Int?, fields: Int?) : Mono<Tuple3<Tournament,List<Team>,List<Field>>> {
        val triple = tgs.generate(tournamentId,template, teams ?: template.properties.maxNoOfTeams,fields ?: template.properties.minNoOfFields)
        log.info("Tournament with id: ${triple.first.id} with ${triple.second.size} teams and ${triple.third.size} fields going to be inserted")
        return Mono.zip(
                trs.insert(triple.first),
                ts.insert(triple.second).collectList(),
                fs.insert(triple.third).collectList()
            )
            .doOnError {
                log.error("Failed to generate tournament: $tournamentId by template: ${template.id}")
                log.error("Error message: ${it.message}")
                log.error("Trace: \n ${it.stackTrace}")
                throw TournamentException("Exception with tournament: $tournamentId and template: ${template.id}\n${it.message}")
            }
    }

    fun generateAndSaveGameround(tournamentId: String, no: Int, feizi: Boolean = true) : Mono<Gameround> {
        log.info("Starting with generating gameround: $no of tournament: $tournamentId")
        return trs.findById(tournamentId)
            .flatMap { ggs.generateGameround(it,no,feizi) }
            .doOnNext { it.results = sos.genResults(listOf(),it.results.associateBy { r -> r.team },it.groups,it.orderConfig,0) }
            .flatMap { grs.insert(it) }
            .doOnNext { log.info("Gameround: $no of tournament: $tournamentId successfully generated and saved") }
            .doOnError {
                log.error("Failed to generate gameround no: $no of tournament: $tournamentId")
                log.error("Error message: ${it.message}")
                log.error("Trace: \n ${it.stackTrace}")
            }
    }

    fun replaceAndSaveGameround(tournamentId: String, no: Int, feizi: Boolean = true) : Mono<Gameround> {
        return grs.deleteByTournamentAndNo(tournamentId,no).then(Mono.defer { generateAndSaveGameround(tournamentId, no, feizi) })
    }

    fun generateAndSaveMatchesOfGameround(tournamentId: String, gameroundNo: Int, time: LocalDateTime, feizi: Boolean = true) : Flux<Match> {
        log.info("Starting with generating matches for gameround: $gameroundNo of tournament: $tournamentId")
        val fields = fs.findAllByTournament(tournamentId).collectList().doOnNext { log.debug("Found ${it.size} fields") }
        val gr = grs.findByTournamentAndNo(tournamentId, gameroundNo)
        val offset = ms.countMatchesByTournament(tournamentId).doOnNext { log.debug("Match no offset: $it") }
        return Mono.zip(gr,fields,offset)
            .map { mgs.generate(it.t1,it.t2,time,it.t3.toInt(),feizi) }
            .doOnNext { log.info("Saving ${it.size} teams...") }
            .flatMapMany { ms.insert(it) }
    }

    fun generateMatchPDF(tournamentId: String, gameroundNo: Int) : Mono<ResponseEntity<ByteArray>> {
        return ms.findAllByGameround(tournamentId,gameroundNo)
            .collectList()
            .map { pdfService.matchesToPdf(it, name = "Spielberichtsbogen_$gameroundNo") }
            .map { ResponseEntity.ok()
                .header("Content-Disposition","inline; filename=spiele_$gameroundNo.pdf")
                .body(it)
            }
    }


    fun generateAndSaveResults(tournament: String, no: Int) : Mono<Gameround> {
        //TODO("MAKE ORDERING FOR EACH LEAF OF GROUPS AND NOT ALL TOGETHER --> IS MIXING UP EXTERNAL RANKING")
        return grs.findByTournamentAndNo(tournament, no)
            .zipWhen { ms.findAllByGameround(tournament, no).collectList() }
            .map { tuple ->
                tuple.t1.results = tuple.t1.genGroupOrder()
                    .sortedBy { it.first }
                    .onEach { triple -> log.debug("Rank: ${triple.first}, groups: ${triple.second.map { it.no }}, offset: ${triple.third}") }
                    .map { triple ->
                        sos.genResults(
                            tuple.t2,
                            tuple.t1.results.filter { triple.second.flatMap { g -> g.teams }.contains(it.team) }.associateBy { it.team },
                            triple.second,
                            tuple.t1.orderConfig, triple.third)
                    }.flatten().toMutableList()
                tuple.t1
            }
            .flatMap { grs.save(it) }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun renameTeam(tournament: String, no: Int, name: String) : Mono<Team> {
        return ts.findByTournamentAndNo(tournament, no)
            .doOnError {
                val msg = "Team of tournament: $tournament with no: $no does not exist."
                log.error(msg)
                throw ResourceNotFoundException(msg)
            }
            .flatMap {
                it.name = name
                Mono.zip(
                    ts.save(it),
                    ms.updateTeamNameByTournamentAndNo(tournament,no,name),
                    grs.updateTeamNameByTournamentAndNo(tournament,no,name)
                )
            }
            .map { it.t1 }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun renameField(tournament: String, no: Int, name: String) : Mono<Field> {
        return fs.findByTournamentAndNo(tournament, no)
            .doOnError {
                val msg = "Field of tournament: $tournament with no: $no does not exist."
                log.error(msg)
                throw ResourceNotFoundException(msg)
            }
            .flatMap {
                it.name = name
                Mono.zip(
                    fs.upsert(it),
                    ms.updateFieldNameByTournamentAndNo(tournament, no, name)
                )
            }
            .map { it.t1 }
    }


}