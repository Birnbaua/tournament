package at.birnbaua.tournament.data.service

import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.repository.FieldRepository
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
class FieldService {

    @Autowired
    private lateinit var repo: FieldRepository

    @Autowired
    private lateinit var ms: MatchService

    private val log = LoggerFactory.getLogger(TeamService::class.java)

    fun insert(entity: Field) : Mono<Field> { return repo.insert(entity) }
    fun findById(id: ObjectId) : Mono<Field> { return repo.findById(id) }
    fun findByTournamentAndNo(tournament: String, no: String) : Mono<Field> { return repo.findByTournamentAndNo(tournament, no)}
    fun findAllByTournament(tournament: String) : Flux<Field> { return repo.findAllByTournament(tournament) }
    fun deleteByTournamentAndNo(tournament: String, no: String) : Mono<Long> { return repo.deleteByTournamentAndNo(tournament, no) }
    fun deleteAllByTournament(tournament: String) : Mono<Long> { return repo.deleteAllByTournament(tournament) }

    fun upsert(entity: Field) : Mono<Field> {
        return if(entity.id != null) { findById(entity.id!!) } else { findByTournamentAndNo(entity.tournament!!,entity.no!!) }
            .zipWhen { if(it.name != entity.name) ms.updateFieldNameByTournamentAndNo(it.tournament,it.no,entity.name) else Mono.empty() }
            .doOnNext {
                entity.id = it.t1.id
                entity.audit.updatedAt = LocalDateTime.now()
                entity.audit.createdAt = it.t1.audit.createdAt
            }
            .flatMap { repo.save(entity) }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun patch(entity: Field) : Mono<Field> {
        return TODO("Do when other is finished")
    }
}