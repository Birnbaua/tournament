package at.birnbaua.tournament.data.repository

import at.birnbaua.tournament.data.document.template.TournamentTemplate
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TournamentTemplateRepository : ReactiveMongoRepository<TournamentTemplate,String>