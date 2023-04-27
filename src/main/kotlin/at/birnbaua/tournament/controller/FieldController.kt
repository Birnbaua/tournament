package at.birnbaua.tournament.controller

import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.service.FieldService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/#{apiProperties.tournament}/{tournament}/#{apiProperties.field}")
class FieldController {
    @Autowired
    private lateinit var service: FieldService

    @GetMapping
    fun getAll(@PathVariable tournament: String) : Flux<Field> { return service.findAllByTournament(tournament) }

    @GetMapping("/{no}")
    fun get(@PathVariable tournament: String, @PathVariable no: Int) : Mono<Field> { return service.findByTournamentAndNo(tournament,no) }

    @PostMapping
    fun post(@PathVariable tournament: String, @RequestBody entity: Field) : Mono<Field> {
        entity.tournament = tournament
        return service.insert(entity)
    }

    @PutMapping("/{no}")
    fun put(@PathVariable tournament: String, @PathVariable no: Int, @RequestBody entity: Field) : Mono<Field> {
        entity.tournament = tournament
        entity.no = no
        return service.upsert(entity)
    }

    @PatchMapping("/{no}")
    fun patch(@PathVariable tournament: String, @PathVariable no: Int, @RequestBody entity: Field) : Mono<Field> {
        entity.tournament = tournament
        entity.no = no
        return service.patch(entity)
    }

    @DeleteMapping("/{no}")
    fun deleteById(@PathVariable tournament: String, @PathVariable no: Int) : Mono<Long> { return service.deleteByTournamentAndNo(tournament,no) }

    @DeleteMapping
    fun deleteAll(@PathVariable tournament: String) : Mono<Long> { return service.deleteAllByTournament(tournament) }
}