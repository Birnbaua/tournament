package at.birnbaua.tournament.data.repository

import at.birnbaua.tournament.data.document.Match
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface MatchRepository : ReactiveMongoRepository<Match,ObjectId> {
    fun findByTournamentAndNo(tournament: String, no: String) : Mono<Match>
    fun findAllByTournament(tournament: String) : Flux<Match>
    fun deleteByTournamentAndNo(tournament: String, no: String) : Mono<Long>
    fun deleteAllByTournament(tournament: String) : Mono<Long>
    fun deleteAllByTournamentAndNoIn(tournament: String, no: List<String>) : Mono<Long>
}