package at.birnbaua.tournament.data.service

import at.birnbaua.tournament.data.document.template.TournamentTemplate
import at.birnbaua.tournament.data.repository.TournamentTemplateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TournamentTemplateService {

    @Autowired
    private lateinit var repo: TournamentTemplateRepository

    fun insertIfNotExisting(template: TournamentTemplate) : Mono<TournamentTemplate> { return repo.insert(template).onErrorComplete() }
    fun save(template: TournamentTemplate) : Mono<TournamentTemplate> { return repo.save(template) }
    fun findById(id: String) : Mono<TournamentTemplate> { return repo.findById(id) }
    fun deleteAll(): Mono<Void> { return repo.deleteAll() }
    fun existsById(id: String) : Mono<Boolean> { return repo.existsById(id) }

}