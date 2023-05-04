package at.birnbaua.tournament.feizi

import at.birnbaua.tournament.config.tournament.vb4222.TournamentConfig2023
import at.birnbaua.tournament.controller.*
import at.birnbaua.tournament.data.service.*
import at.birnbaua.tournament.data.service.feizi.SimpleMatchGeneratingService
import at.birnbaua.tournament.data.service.feizi.SimpleOrderService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.kotlin.core.publisher.toFlux
import kotlin.math.ceil

@SpringBootTest
class FeiziNormalTest {

    //Controller
    @Autowired private lateinit var tournamentController: TournamentController
    @Autowired private lateinit var gameroundController: GameroundController
    @Autowired private lateinit var matchController: MatchController
    @Autowired private lateinit var fieldController: FieldController
    @Autowired private lateinit var teamController: TeamController

    //Services
    @Autowired private lateinit var tts: TournamentTemplateService
    @Autowired private lateinit var gts: GameroundTemplateService
    @Autowired private lateinit var smgs: SimpleMatchGeneratingService
    @Autowired private lateinit var tournamentService: TournamentService
    @Autowired private lateinit var grs: GameroundService
    @Autowired private lateinit var sos: SimpleOrderService
    @Autowired private lateinit var fs: FieldService
    @Autowired private lateinit var ts: TeamService
    @Autowired private lateinit var ms: MatchService

    @BeforeEach
    fun beforeEachTest() {
        tts.deleteAll().block()
        gts.deleteAll().block()
        tournamentService.deleteAll().block()
        grs.deleteAll().block()
        fs.deleteAll().block()
        ts.deleteAll().block()
        ms.deleteAll().block()
        (1..12).toFlux().flatMap { tts.insertIfNotExisting(TournamentConfig2023().genTournamentTemplate(it))}.collectList().block()
    }

    @Test
    fun testFirstRoundEvenFilled() {
        val teams = 51
        tournamentController.generateTournament("vb4222_2023","vb_standard_${ceil(teams.toDouble()/5).toInt()}", teams = teams).block()
        gameroundController.generateGameround("vb4222_2023",0).block()
        gameroundController.generateMatchesOfGameround("vb4222_2023",0).collectList().block()
        ms.findAllByTournament("vb4222_2023")
            .doOnNext { it.sets[0].pointsA = 1 }
            .flatMap { ms.save(it) }
            .collectList().block()
        gameroundController.generateResults("vb4222_2023",0).block()
        gameroundController.generateGameround("vb4222_2023",1).block()

    }

    @Test
    fun testFirstRoundEvenNotFilled() {
        val teams = 47
    }

}