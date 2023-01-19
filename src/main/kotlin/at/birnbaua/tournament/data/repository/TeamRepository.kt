package at.birnbaua.tournament.data.repository

import at.birnbaua.tournament.data.document.Team
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : ReactiveMongoRepository<Team,ObjectId> {
}