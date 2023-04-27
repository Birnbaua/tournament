package at.birnbaua.tournament.data.repository

import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Tournament
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface GameroundRepository : ReactiveMongoRepository<Gameround,ObjectId> {
    fun findAllByTournament(tournament: String) : Flux<Gameround>
    fun findFirstByTournamentOrderByNoDesc(tournament: String) : Mono<Gameround>
    fun findByTournamentAndNo(tournament: String, no: Int) : Mono<Gameround>
    fun deleteByTournamentAndNo(tournament: String, no: Int) : Mono<Void>
    fun deleteAllByTournament(tournament: String) : Mono<Void>
}