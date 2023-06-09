package at.birnbaua.tournament.controller

import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.service.MatchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@CrossOrigin
@RestController
@RequestMapping("/#{apiProperties.tournament}/{tournament}/#{apiProperties.match}")
class MatchController {

    @Autowired private lateinit var ms: MatchService
    @Autowired private lateinit var cs: ControllerService
    @GetMapping
    fun getAll(@PathVariable tournament: String, @RequestParam(required = false) gameround: Int? = null) : Flux<Match> {
        return if(gameround == null) ms.findAllByTournament(tournament) else ms.findAllByGameround(tournament,gameround)
    }
}