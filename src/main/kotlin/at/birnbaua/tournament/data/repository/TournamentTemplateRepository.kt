package at.birnbaua.tournament.data.repository

import at.birnbaua.tournament.data.document.template.TournamentTemplate
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface TournamentTemplateRepository : ReactiveMongoRepository<TournamentTemplate,String> {
    fun findFirstByProperties_MinNoOfTeamsGreaterThanEqualAndProperties_MaxNoOfTeamsLessThanEqual(
        properties_minNoOfTeams: Int,
        properties_maxNoOfTeams: Int
    ) : Mono<TournamentTemplate>
}