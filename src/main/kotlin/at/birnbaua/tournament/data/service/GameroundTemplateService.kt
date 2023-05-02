package at.birnbaua.tournament.data.service

import at.birnbaua.tournament.data.document.template.GameroundTemplate
import at.birnbaua.tournament.data.repository.GameroundTemplateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class GameroundTemplateService {

    @Autowired
    private lateinit var repo: GameroundTemplateRepository

    fun insertIfNotExisting(template: GameroundTemplate) : Mono<GameroundTemplate> { return repo.insert(template).onErrorComplete() }
    fun findAll() : Flux<GameroundTemplate> { return repo.findAll() }
    fun findById(id: String) : Mono<GameroundTemplate> { return repo.findById(id) }
    fun findByTournamentAndGameround(tournament: String, no: Int) : Mono<GameroundTemplate> { return repo.findByTournamentAndGameroundNumber(tournament, no) }
    fun deleteAll(): Mono<Void> { return repo.deleteAll() }
}