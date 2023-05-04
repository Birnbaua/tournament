package at.birnbaua.tournament.feizi

import at.birnbaua.tournament.config.tournament.vb4222.TournamentConfig2023
import at.birnbaua.tournament.controller.*
import at.birnbaua.tournament.data.service.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.kotlin.core.publisher.toFlux

@SpringBootTest
class TournamentTemplateTest {

    @Autowired    private lateinit var tournamentController: TournamentController
    @Autowired    private lateinit var gameroundController: GameroundController
    @Autowired    private lateinit var matchController: MatchController
    @Autowired    private lateinit var fieldController: FieldController
    @Autowired    private lateinit var teamController: TeamController

    //Services
    @Autowired    private lateinit var tts: TournamentTemplateService
    @Autowired    private lateinit var gts: GameroundTemplateService
    @Autowired    private lateinit var tournamentService: TournamentService
    @Autowired    private lateinit var grs: GameroundService
    @Autowired    private lateinit var fs: FieldService
    @Autowired    private lateinit var ts: TeamService
    @Autowired    private lateinit var ms: MatchService

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
    fun testTemplate() {
        val tt = tts.findById("vb_standard_11").block()
        assert(tt!!.gameroundTemplates.size == 4)
        assert(tt.gameroundTemplates[0]!!.flattenGroupsOnImproperTeamNumber)
    }
}