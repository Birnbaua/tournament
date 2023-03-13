package at.birnbaua.tournament.data.repository

import at.birnbaua.tournament.data.document.Gameround
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface GameroundRepository : ReactiveMongoRepository<Gameround,ObjectId> {
    fun findAllByTournament(tournament: String) : Flux<Gameround>
    fun findByTournamentAndNo(tournament: String, no: Int) : Mono<Gameround>
}