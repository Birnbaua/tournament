package at.birnbaua.tournament.controller

import at.birnbaua.tournament.data.document.Tournament
import at.birnbaua.tournament.data.service.TournamentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/\${api.tournament:#{apiProperties.tournament}}")
class TournamentController {

    @Autowired
    private lateinit var service: TournamentService

    @GetMapping
    fun getAll(@RequestParam active: Boolean?) : Flux<Tournament> { return service.findAll() }

    @GetMapping("/{id}")
    fun get(@PathVariable id: String) : Mono<Tournament> { return service.findById(id) }

    @PostMapping
    fun post(@RequestBody entity: Tournament) : Mono<Tournament> { return service.insert(entity) }

    @PutMapping("/{id}")
    fun put(@PathVariable id: String, @RequestBody entity: Tournament) : Mono<Tournament> {
        entity.id = id
        return service.upsert(entity)
    }

    @PatchMapping("/{id}")
    fun patch(@PathVariable id: String, @RequestBody entity: Tournament) : Mono<Tournament> {
        entity.id = id
        return service.patch(entity)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: String) : Mono<Void> { return service.deleteById(id) }

    @GetMapping("/{id}/generate")
    fun generateTournament(@PathVariable id: String, @RequestParam template: String) : Mono<Tournament> {
        return service.generateAndInsertByTemplate(id,template)
    }

}