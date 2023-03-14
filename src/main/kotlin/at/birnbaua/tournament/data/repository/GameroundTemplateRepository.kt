package at.birnbaua.tournament.data.repository

import at.birnbaua.tournament.data.document.template.GameroundTemplate
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface GameroundTemplateRepository : ReactiveMongoRepository<GameroundTemplate,String> {
    fun findByTournamentAndGameroundNumber(tournament: String, no: Int) : Mono<GameroundTemplate>
}