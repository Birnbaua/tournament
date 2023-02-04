package at.birnbaua.tournament.data.repository

import at.birnbaua.tournament.data.document.Field
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface FieldRepository : ReactiveMongoRepository<Field,ObjectId> {
    fun findByTournamentAndNo(tournament: String, no: String) : Mono<Field>
    fun findAllByTournament(tournament: String) : Flux<Field>
    fun existsByTournamentAndNo(tournament: String, no: String) : Mono<Boolean>
    fun deleteByTournamentAndNo(tournament: String, no: String) : Mono<Long>
    fun deleteAllByTournament(tournament: String) : Mono<Long>
    fun deleteAllByTournamentAndNoIn(tournament: String, no: List<String>) : Mono<Long>
}