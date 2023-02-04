package at.birnbaua.tournament.data.service

import at.birnbaua.tournament.data.document.Tournament
import at.birnbaua.tournament.data.repository.TournamentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TournamentService {

    @Autowired
    private lateinit var repo: TournamentRepository

    fun findAll() : Flux<Tournament> { return repo.findAll() }
    fun findById(id: String) : Mono<Tournament> { return repo.findById(id) }

    fun insert(entity: Tournament) : Mono<Tournament> { return repo.insert(entity) }

    fun upsert(entity: Tournament) : Mono<Tournament> { return repo.save(entity) }

    fun patch(entity: Tournament) : Mono<Tournament> { return repo.save(entity) }
    fun deleteById(id: String) : Mono<Void> { return repo.deleteById(id) }
}