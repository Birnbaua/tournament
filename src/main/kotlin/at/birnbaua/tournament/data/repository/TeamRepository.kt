package at.birnbaua.tournament.data.repository

import at.birnbaua.tournament.data.document.Team
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface TeamRepository : ReactiveMongoRepository<Team,ObjectId> {

    fun findByTournamentAndNo(tournament: String, no: String) : Mono<Team>
    fun findAllByTournament(tournament: String) : Flux<Team>
    fun existsByTournamentAndNo(tournament: String, no: String) : Mono<Boolean>
    fun deleteByTournamentAndNo(tournament: String, no: String) : Mono<Long>
    fun deleteAllByTournament(tournament: String) : Mono<Long>
    fun deleteAllByTournamentAndNoIn(tournament: String, no: List<String>) : Mono<Long>
}