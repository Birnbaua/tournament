package at.birnbaua.tournament.controller

import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.service.TeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/\${api.tournament:#{apiProperties.tournament}}/{tournament}/\${api.team:#{apiProperties.team}}")
class TeamController {

    @Autowired
    private lateinit var service: TeamService

    @GetMapping
    fun getAll(@PathVariable tournament: String) : Flux<Team> { return service.findAllByTournament(tournament) }

    @GetMapping("/{no}")
    fun get(@PathVariable tournament: String, @PathVariable no: Int) : Mono<Team> { return service.findByTournamentAndNo(tournament,no) }

    @PostMapping
    fun post(@PathVariable tournament: String, @RequestBody entity: Team) : Mono<Team> {
        entity.tournament = tournament
        return service.insert(entity)
    }

    @PutMapping("/{no}")
    fun put(@PathVariable tournament: String, @PathVariable no: Int, @RequestBody entity: Team) : Mono<Team> {
        entity.tournament = tournament
        entity.no = no
        return service.upsert(entity)
    }

    @PatchMapping("/{no}")
    fun patch(@PathVariable tournament: String, @PathVariable no: Int, @RequestBody entity: Team) : Mono<Team> {
        entity.tournament = tournament
        entity.no = no
        return service.patch(entity)
    }

    @DeleteMapping("/{no}")
    fun deleteById(@PathVariable tournament: String, @PathVariable no: Int) : Mono<Long> { return service.deleteByTournamentAndNo(tournament,no) }

    @DeleteMapping
    fun deleteAll(@PathVariable tournament: String) : Mono<Long> { return service.deleteAllByTournament(tournament) }
}