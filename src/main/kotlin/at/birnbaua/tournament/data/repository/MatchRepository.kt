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
    fun findByTournamentAndNo(tournament: String, no: Int) : Mono<Match>
    fun findAllByTournament(tournament: String) : Flux<Match>
    fun deleteByTournamentAndNo(tournament: String, no: Int) : Mono<Long>
    fun deleteAllByTournament(tournament: String) : Mono<Long>
    fun deleteAllByTournamentAndNoIn(tournament: String, no: List<Int>) : Mono<Long>

    @Query(value = "{'tournament':  ?0}")
    @Update(pipeline = [
        "{\$set: {'team_a.name': {\$cond: { if: {\$eq: ['\$team_a.no',?1] }, then: ?2, else: '\$team_a.name' } } } }",
        "{\$set: {'team_b.name': {\$cond: { if: {\$eq: ['\$team_b.no',?1] }, then: ?2, else: '\$team_b.name' } } } }",
        "{\$set: {'referee.name': {\$cond: { if: {\$eq: ['\$referee.no',?1] }, then: ?2, else: '\$referee.name' } } } }"
    ])
    fun updateTeamNameByTournamentAndNo(tournament: String?, no: Int?, name: String?) : Mono<Long>

    @Query(value = "{'tournament': ?0}")
    @Update(pipeline = ["{\$set: {'field.name': {\$cond: { if: {\$eq: ['\$field.no',?1] }, then: ?2, else: '\$field.name' } } } }"])
    fun updateFieldNameByTournamentAndNo(tournament: String?, no: Int?, name: String?) : Mono<Long>
}