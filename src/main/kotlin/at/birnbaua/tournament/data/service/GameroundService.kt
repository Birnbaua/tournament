package at.birnbaua.tournament.data.service

import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.repository.GameroundRepository
import org.bson.types.ObjectId
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class GameroundService {

    @Autowired
    private lateinit var repo: GameroundRepository

    fun save(entity: Gameround) : Mono<Gameround> { return repo.save(entity) }
    fun save(publisher: Mono<Gameround>) : Mono<Gameround> { return publisher.flatMap { repo.save(it) } }
    fun findById(id: ObjectId) : Mono<Gameround> { return repo.findById(id) }
    fun findByTournamentAndNo(tournament: String, no: Int) : Mono<Gameround> { return repo.findByTournamentAndNo(tournament, no) }
    fun findAllByTournament(tournament: String) : Flux<Gameround> { return repo.findAllByTournament(tournament) }
    fun existsById(id: ObjectId) : Mono<Boolean> { return repo.existsById(id) }
}