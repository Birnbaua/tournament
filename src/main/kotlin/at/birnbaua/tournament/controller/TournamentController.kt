package at.birnbaua.tournament.controller

import at.birnbaua.tournament.data.document.Tournament
import at.birnbaua.tournament.data.service.TournamentService
import at.birnbaua.tournament.data.service.gen.TournamentGeneratingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/\${api.tournament:#{apiProperties.tournament}}")
class TournamentController {

    private val log: Logger = LoggerFactory.getLogger(TournamentController::class.java)

    @Autowired private lateinit var service: TournamentService
    @Autowired private lateinit var tgs: TournamentGeneratingService

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
    fun generateTournament(@PathVariable id: String,
                           @RequestParam(required = false, defaultValue = "") template: String = "",
                           @RequestParam(required = false, defaultValue = "true") feizi: Boolean = true,
                           @RequestParam(required = false, defaultValue = "-1") teams: Int = -1,
                           @RequestParam(required = false, defaultValue = "-1") fields: Int = -1) : Mono<Tournament> {
        return if(template.isNotBlank()) {
            tgs.generateAndInsert(id,template,teams,fields)
                .doOnNext { log.info("Created tournament with id: $id successfully <3") }
                .doOnError { log.error("Failed to create tournament with id: $id of template $template") }
                .map { it.t1 }
        } else {
            service.generateAndInsertFeiziByTeamNo(id,teams)
        }
    }

}