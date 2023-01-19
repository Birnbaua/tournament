package at.birnbaua.tournament.data.repository

import at.birnbaua.tournament.data.document.Match
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.mongodb.repository.Update
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

    @Query(value = "{'tournament':  ?0}")
    @Update(value = "{\$set: {'team_a': {\$cond: { if: {\$eq: ['team_a',?1] }, then: ?2, else: 'team_a' } } } }")
    fun updateTeamName(tournament: String, old: String, name: String) : Mono<Long>
}