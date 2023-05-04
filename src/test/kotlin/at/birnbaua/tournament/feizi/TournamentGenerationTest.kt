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
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux

@SpringBootTest
class TournamentGenerationTest {

    @Autowired private lateinit var tgs: TournamentGeneratingService
    @Autowired private lateinit var ggs: GameroundGeneratingService
    @Autowired private lateinit var mgs: MatchGeneratingService
    @Autowired private lateinit var tts: TournamentTemplateService
    @Autowired private lateinit var gts: GameroundTemplateService
    @Autowired private lateinit var tournamentService: TournamentService
    @Autowired private lateinit var grs: GameroundService
    @Autowired private lateinit var fs: FieldService
    @Autowired private lateinit var ts: TeamService
    @Autowired private lateinit var ms: MatchService
    @Autowired private lateinit var startupScheduler: FeiziDefault

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
    fun testTournamentTemplates() {
        (1..12).toFlux()
            .flatMap { template -> tts.existsById("vb_standard_$template") }
            .collectList().block()!!
            .forEach { assert(it) }
    }

    @Test
    fun testTournamentGeneration() {
        val triples: List<Triple<Tournament,List<Team>,List<Field>>> = (1..12)
            .toFlux()
            .flatMap { tts.findById("vb_standard_$it") }
            .map { tgs.generate("vb_standard_$it",it) }
            .collectList().block()!!
        assert(triples.size == 12)
        triples.forEach {
            assert(it.second.size == it.third.size * 5)
        }
    }

    @Test
    fun testTournamentGenerationSave() {
        val triples: List<Triple<Tournament,List<Team>,List<Field>>> = (1..12)
            .toFlux()
            .flatMap { tts.findById("vb_standard_$it") }
            .map { tgs.generate("vb_standard_$it",it) }
            .collectList().block()!!
        triples.forEach {
            it.first.id = it.first.id+"_${it.third.size}"
            tournamentService.insert(it.first).block()
            ts.insert(it.second).collectList().block()
            fs.insert(it.third).collectList().block()
        }
    }
}