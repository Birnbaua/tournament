package at.birnbaua.tournament.data.service

import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.Tournament
import at.birnbaua.tournament.data.document.template.GameroundTemplate
import at.birnbaua.tournament.data.repository.GameroundRepository
import at.birnbaua.tournament.data.service.feizi.SimpleMatchGeneratingService
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import at.birnbaua.tournament.exception.ResourceNotFoundException
import org.bson.types.ObjectId
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDateTime
import kotlin.IllegalArgumentException

@Service
class GameroundService {

    @Autowired private lateinit var repo: GameroundRepository

    @Autowired private lateinit var ggs: GameroundGeneratingService
    @Autowired private lateinit var gts: GameroundTemplateService
    @Autowired private lateinit var mgs: SimpleMatchGeneratingService
    @Autowired private lateinit var trs: TournamentService
    @Autowired private lateinit var ts: TeamService
    @Autowired private lateinit var fs: FieldService
    @Autowired private lateinit var ms: MatchService
    private val log: Logger = LoggerFactory.getLogger(GameroundService::class.java)
    private val genFeiziOnly: Boolean = true

    fun save(entity: Gameround) : Mono<Gameround> { return repo.save(entity) }
    fun save(publisher: Mono<Gameround>) : Mono<Gameround> { return publisher.flatMap { repo.save(it) } }
    fun insert(entities: Iterable<Gameround>) : Flux<Gameround> { return repo.insert(entities) }
    fun findById(id: ObjectId) : Mono<Gameround> { return repo.findById(id) }
    fun findByTournamentAndNo(tournament: String, no: Int) : Mono<Gameround> { return repo.findByTournamentAndNo(tournament, no) }
    fun findPreviousByTournamentAndNo(tournament: String, no: Int) : Mono<Gameround> { return repo.findByTournamentAndNo(tournament, no-1)}
    fun findAllByTournament(tournament: String) : Flux<Gameround> { return repo.findAllByTournament(tournament) }
    fun deleteByTournamentAndNo(tournament: String, no: Int) : Mono<Void> { return repo.deleteByTournamentAndNo(tournament, no) }
    fun deleteAllByTournament(tournament: String) : Mono<Void> { return repo.deleteAllByTournament(tournament) }
    fun existsById(id: ObjectId) : Mono<Boolean> { return repo.existsById(id) }

    fun generateMatchesOf(tournament: String, no: Int, startTime: LocalDateTime) : Flux<Match> {
        val fields = fs.findAllByTournament(tournament).collectList()
        val gr = findByTournamentAndNo(tournament, no)
        return Mono.zip(gr,fields)
            .map { mgs.generateMatchesFeizi(it.t1,it.t2,startTime) }
            .flatMapMany { ms.saveAll(it) }
    }

    fun generateGameround(tournament: String, no: Int) : Mono<Gameround> {
        TODO()
    }
}