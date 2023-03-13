package at.birnbaua.tournament.controller

import at.birnbaua.tournament.config.tournament.feizi.GroupInternalRound
import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.service.feizi.SimpleMatchGeneratingService
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import at.birnbaua.tournament.pdf.PdfService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.time.LocalDateTime

@RestController
@RequestMapping("/test")
class TestController {

    @Autowired private lateinit var pdfService: PdfService

    @GetMapping(path = ["/pdf"], produces = [MediaType.APPLICATION_PDF_VALUE])
    fun testPdf() : Mono<ResponseEntity<ByteArray>> {
        return ResponseEntity.ok()
            .header("Content-Disposition","inline; filename=spiele.pdf")
            .body(pdfService.matchesToPdf(genMatches(genGameround(30))))
            .toMono()

    }


    private fun genMatches(gr: Gameround) : List<Match> {
        val mgs = SimpleMatchGeneratingService()
        val fields = (0 until gr.groups.size).map {
            val field = Field()
            field.no = it
            field.name = "Field $it"
            field
        }
        return mgs.generateMatchesFeizi(gr,fields, LocalDateTime.of(2023,6,17,9,0,0))
    }

    private fun genGameround(noOfTeams: Int) : Gameround {
        val noOfGroups = if(noOfTeams % 5 == 0) noOfTeams/5 else noOfTeams/5 +1
        val gameroundTemplate = GroupInternalRound().genGameroundTemplate("Gruppenphase","Beschreibung", noOfGroups)
        gameroundTemplate.matchNumberOffset = 0
        val ggs = GameroundGeneratingService()
        val teams = (0 until noOfTeams).map {
            val team = Team()
            team.no = it
            team.name = "Team $it"
            team
        }
        val gr = ggs.generate(gameroundTemplate,teams,0)
        gr.groups.forEach { group ->
            group.teams.forEachIndexed { index,team ->
                team.name = "${(group.no+65).toChar()}${index}"
            }
        }
        return gr
    }
}