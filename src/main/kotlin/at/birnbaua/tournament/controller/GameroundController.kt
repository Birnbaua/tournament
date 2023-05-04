package at.birnbaua.tournament.controller

import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.service.GameroundService
import at.birnbaua.tournament.data.service.MatchService
import at.birnbaua.tournament.data.service.TournamentService
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import at.birnbaua.tournament.pdf.PdfService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@RestController
@RequestMapping("/\${api.tournament:#{apiProperties.tournament}}/{tournament}/\${api.gameround:#{apiProperties.gameround}}")
class GameroundController {

    @Autowired private lateinit var ts: TournamentService
    @Autowired private lateinit var gs: GameroundService
    @Autowired private lateinit var ggs: GameroundGeneratingService
    @Autowired private lateinit var ms: MatchService
    @Autowired private lateinit var pdfService: PdfService

    @GetMapping
    fun getAll(@PathVariable tournament: String) : Flux<Gameround> { return gs.findAllByTournament(tournament) }

    @GetMapping(path= ["/{no}"])
    fun getGameround(@PathVariable tournament: String, @PathVariable no: Int) : Mono<Gameround> { return gs.findByTournamentAndNo(tournament,no) }

    @PutMapping(path = ["/{no}"])
    fun put(@PathVariable tournament: String, @PathVariable no: Int, @RequestBody gr: Gameround) : Mono<Gameround> { return gs.save(gr) }

    @DeleteMapping
    fun deleteAll(@PathVariable tournament: String) : Mono<Void> { return gs.deleteAllByTournament(tournament) }

    @DeleteMapping(path = ["/{no}"])
    fun delete(@PathVariable tournament: String, @PathVariable no: Int) : Mono<Void> { return gs.deleteByTournamentAndNo(tournament,no) }

    @GetMapping(path = ["/{no}/generate"])
    fun generateGameround(@PathVariable tournament: String, @PathVariable no: Int) : Mono<Gameround> {
        return ts.findById(tournament)
            .flatMap { ggs.generateGameround(it,no) }
            .flatMap { gs.save(it) }
    }

    @GetMapping(path = ["/{no}/generate/matches"])
    fun generateMatchesOfGameround(@PathVariable tournament: String, @PathVariable no: Int) : Flux<Match> { return gs.generateMatchesOf(tournament,no,LocalDateTime.now()) }

    @GetMapping(path = ["/{no}/pdf"], produces = [MediaType.APPLICATION_PDF_VALUE])
    fun genPdf(@PathVariable tournament: String, @PathVariable no: Int) : Mono<ResponseEntity<ByteArray>> {
        return ms.findAllByGameround(tournament,no)
            .collectList()
            .map { pdfService.matchesToPdf(it) }
            .map { ResponseEntity.ok()
                .header("Content-Disposition","inline; filename=spiele.pdf")
                .body(it)
            }
    }
}