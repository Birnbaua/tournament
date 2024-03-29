package at.birnbaua.tournament.data.service

import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.repository.TeamRepository
import at.birnbaua.tournament.exception.ResourceNotFoundException
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class TeamService {

    @Autowired
    private lateinit var repo: TeamRepository

    @Autowired private lateinit var ms: MatchService
    @Autowired private lateinit var grs: GameroundService

    private val log = LoggerFactory.getLogger(TeamService::class.java)

    fun insert(entity: Team) : Mono<Team> { return repo.insert(entity) }
    fun insert(iterable: Iterable<Team>) : Flux<Team> { return repo.insert(iterable) }
    fun save(entity: Team) : Mono<Team> { return repo.save(entity) }
    fun findById(id: ObjectId) : Mono<Team> { return repo.findById(id) }
    fun findByTournamentAndNo(tournament: String, no: Int) : Mono<Team> { return repo.findByTournamentAndNo(tournament, no)}
    fun findAllByTournament(tournament: String) : Flux<Team> { return repo.findAllByTournament(tournament) }
    fun deleteByTournamentAndNo(tournament: String, no: Int) : Mono<Long> { return repo.deleteByTournamentAndNo(tournament, no) }
    fun deleteAll(): Mono<Void> { return repo.deleteAll() }
    fun deleteAllByTournament(tournament: String) : Mono<Long> { return repo.deleteAllByTournament(tournament) }
    fun deleteAllByTournamentAndNoIn(tournament: String, no: List<Int> ) : Mono<Long> { return repo.deleteAllByTournamentAndNoIn(tournament, no) }

    fun existsByTournamentAndNo(tournament: String, no: Int) : Mono<Boolean> { return repo.existsByTournamentAndNo(tournament,no) }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun upsert(entity: Team) : Mono<Team> {
        return if(entity.id != null) { findById(entity.id!!) } else { findByTournamentAndNo(entity.tournament!!,entity.no!!) }
            .zipWhen { if(it.name != entity.name) ms.updateTeamNameByTournamentAndNo(it.tournament,it.no,entity.name) else Mono.empty() }
            .doOnNext {
                entity.id = it.t1.id
                entity.audit.updatedAt = LocalDateTime.now()
                entity.audit.createdAt = it.t1.audit.createdAt
            }
            .flatMap { repo.save(entity) }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun patch(entity: Team) : Mono<Team> {
        return TODO("Do when other is finished")
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun rename(tournament: String, no: Int, name: String): Mono<Team> {
        return repo.findByTournamentAndNo(tournament, no)
            .doOnError {
                val msg = "Team of tournament: $tournament and no: $no does not exist."
                log.error(msg)
                throw ResourceNotFoundException(msg)
            }
            .flatMap {
                it.name = name
                Mono.zip(
                    repo.save(it),
                    ms.updateTeamNameByTournamentAndNo(tournament,no,name),
                    grs.updateTeamNameByTournamentAndNo(tournament,no,name)
                )
            }
            .map { it.t1 }
    }
}