package at.birnbaua.tournament.controller

import at.birnbaua.tournament.data.service.GameroundService
import at.birnbaua.tournament.data.service.MatchService
import at.birnbaua.tournament.pdf.PdfService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/\${api.tournament:#{apiProperties.tournament}}/{tournament}/\${api.gameround:#{apiProperties.gameround}}")
class GameroundController {

    @Autowired private lateinit var gs: GameroundService
    @Autowired private lateinit var ms: MatchService
    @Autowired private lateinit var pdfService: PdfService

    @GetMapping(path = ["/{gameround}/pdf"], produces = [MediaType.APPLICATION_PDF_VALUE])
    fun genPdf(@PathVariable tournament: String, @PathVariable gameround: Int) : Mono<ResponseEntity<ByteArray>> {
        return ms.findAllByGameround(tournament,gameround)
            .collectList()
            .map { pdfService.matchesToPdf(it) }
            .map { ResponseEntity.ok()
                .header("Content-Disposition","inline; filename=spiele.pdf")
                .body(it)
            }
    }
}