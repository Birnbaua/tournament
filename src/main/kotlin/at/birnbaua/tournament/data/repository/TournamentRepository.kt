package at.birnbaua.tournament.data.repository

import at.birnbaua.tournament.data.document.Tournament
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TournamentRepository : ReactiveMongoRepository<Tournament,String>