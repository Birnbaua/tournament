package at.birnbaua.tournament.data.service.feizi

import at.birnbaua.tournament.data.document.CompositeId
import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.Tournament
import at.birnbaua.tournament.data.document.template.GameroundTemplate
import at.birnbaua.tournament.data.service.*
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import at.birnbaua.tournament.exception.NoTeamsException
import at.birnbaua.tournament.exception.ResourceNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.lang.RuntimeException
import java.time.LocalDateTime

@Service
class FeiziTotalService {

    // Storage
    @Autowired private lateinit var trs: TournamentService
    @Autowired private lateinit var grs: GameroundService
    @Autowired private lateinit var ms: MatchService
    @Autowired private lateinit var fs: FieldService
    @Autowired private lateinit var ts: TeamService

    // Utils
    @Autowired private lateinit var ggs: GameroundGeneratingService
    @Autowired private lateinit var mgs: SimpleMatchGeneratingService

    private val log: Logger = LoggerFactory.getLogger(FeiziTotalService::class.java)

    fun generateAndSaveTournament(id: String, name: String, title: String, teamNumber: Int = 30) : Mono<Tournament> {
        return Mono.empty()
    }

    fun generateAndSavePreliminary(tournament: String, template: GameroundTemplate) : Mono<Gameround> {
        val gameroundNumber = 0
        return ts.findAllByTournament(tournament)
            .collectList()
            .doOnNext {
                if(it.isEmpty()) {
                    log.error("There are no teams existing for tournament: $tournament")
                    throw NoTeamsException("There are no teams existing for tournament: $tournament")
                } else {
                    log.info("${it.size} teams found for generating preliminary round of tournament: $tournament")
                }
            }
            .doOnError { e -> log.error("${e.message}"); log.error("${e.stackTrace.map { it.toString() }}") }
            .map { ggs.generate(template,it,gameroundNumber) }
            .doOnNext { it.tournament = tournament }
            .doOnNext { log.info("Try saving preliminary round for tournament: $tournament with ${it.groups.size} groups") }
            .flatMap { grs.save(it) }
            .doOnNext { log.info("Saved preliminary round for tournament: $tournament") }
    }

    fun generateAndSavePreliminaryMatches(tournament: String, start: LocalDateTime) : Flux<Match> {
        val fields = fs.findAllByTournament(tournament)
            .collectList()
            .doOnNext {
                if (it.isEmpty()) {
                    log.error("There are no fields existing for tournament: $tournament")
                    throw NoTeamsException("There are no fields existing for tournament: $tournament")
                } else {
                    log.info("${it.size} fields found for generating preliminary round of tournament: $tournament")
                }
            }
        val gameround = grs.findByTournamentAndNo(tournament,0)
            .switchIfEmpty { throw ResourceNotFoundException("No Gameround of tournament: $tournament and number: 0") }
        val matches = Mono.zip(gameround,fields)
            .flatMapIterable{ mgs.generateMatchesFeizi(it.t1,it.t2,start) }
            .doOnNext { it.tournament = tournament }
            .doOnNext { it.id = CompositeId(tournament,it.no) }
        return ms.save(matches)
    }
}