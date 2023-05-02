package at.birnbaua.tournament.feizi

import at.birnbaua.tournament.config.tournament.vb4222.TournamentConfig2023
import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.document.Tournament
import at.birnbaua.tournament.data.service.*
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import at.birnbaua.tournament.data.service.gen.MatchGeneratingService
import at.birnbaua.tournament.data.service.gen.TournamentGeneratingService
import at.birnbaua.tournament.startup.FeiziDefault
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.kotlin.core.publisher.toFlux

@SpringBootTest
class GameroundGenerationTest {

    @Autowired
    private lateinit var tgs: TournamentGeneratingService
    @Autowired
    private lateinit var ggs: GameroundGeneratingService
    @Autowired
    private lateinit var mgs: MatchGeneratingService
    @Autowired
    private lateinit var tts: TournamentTemplateService
    @Autowired
    private lateinit var gts: GameroundTemplateService
    @Autowired
    private lateinit var tournamentService: TournamentService
    @Autowired
    private lateinit var grs: GameroundService
    @Autowired
    private lateinit var fs: FieldService
    @Autowired
    private lateinit var ts: TeamService
    @Autowired
    private lateinit var ms: MatchService
    @Autowired
    private lateinit var startupScheduler: FeiziDefault

    @BeforeEach
    fun clearDB() {
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
    fun saveGameround() {
        val triple = tts.findById("vb_standard_12")
            .map { tgs.generate(it) }.block()!!
        tournamentService.insert(triple.first).block()
        ts.insert(triple.second).collectList().block()
        fs.insert(triple.third).collectList().block()
        val gameround = ggs.generateGameround(tournamentService.findById("vb4222").block()!!,0).block()!!
        grs.save(gameround).block()
        val oldName = "Team 1"
        val newName = "Team 69"
        ts.rename("vb4222",1, newName).block()
    }
}