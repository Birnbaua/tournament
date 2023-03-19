package at.birnbaua.tournament.startup

import at.birnbaua.tournament.config.tournament.vb4222.CrossGameRound
import at.birnbaua.tournament.config.tournament.vb4222.GroupInternalRound
import at.birnbaua.tournament.config.tournament.vb4222.TournamentConfig2023
import at.birnbaua.tournament.data.document.template.GameroundTemplate
import at.birnbaua.tournament.data.service.GameroundTemplateService
import at.birnbaua.tournament.data.service.MatchService
import at.birnbaua.tournament.data.service.TournamentTemplateService
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import at.birnbaua.tournament.util.Tree
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@EnableScheduling
@Configuration
class FeiziDefault {

    @Autowired private lateinit var ms: MatchService
    @Autowired private lateinit var ggs: GameroundGeneratingService
    @Autowired private lateinit var tts: TournamentTemplateService
    @Autowired private lateinit var gts: GameroundTemplateService

    @Scheduled(initialDelay = 500, fixedDelay = 1000*60)
    fun test() {
        tts.insertIfNotExisting(TournamentConfig2023().genTournamentTemplate()).subscribe()
        val defaultTournament = TournamentConfig2023().genTournamentTemplate()
        defaultTournament.id = "${defaultTournament.id}_default"
        tts.insertIfNotExisting(defaultTournament).subscribe()
        val testTemplate = GameroundTemplate()
        testTemplate.id = "test"
        gts.insertIfNotExisting(testTemplate).subscribe()
        for(i in 1..12) {
            val preliminaryRound = GroupInternalRound().genGameroundTemplate("VB4222 Vorrunde","Vorrunde",i)
            preliminaryRound.id = "vb4222_0_$i"
            preliminaryRound.tournament = TournamentConfig2023().genTournamentTemplate().tournamentId!!
            preliminaryRound.groups = i
            gts.insertIfNotExisting(preliminaryRound).subscribe()
        }
        for(i in 1..12) {
            val preliminaryRound = GroupInternalRound().genGameroundTemplate("VB4222 Zwischenrunde","Zwischenrunde",i)
            preliminaryRound.id = "vb4222_1_$i"
            preliminaryRound.tournament = TournamentConfig2023().genTournamentTemplate().tournamentId!!
            preliminaryRound.groups = i
            gts.insertIfNotExisting(preliminaryRound).subscribe()
        }
        for(i in 1..12) {
            val preliminaryRound = CrossGameRound().genGameroundTemplate("VB4222 Kreuzspiele","Kreuzspiele",i)
            preliminaryRound.id = "vb4222_2_$i"
            preliminaryRound.tournament = TournamentConfig2023().genTournamentTemplate().tournamentId!!
            preliminaryRound.groups = i
            gts.insertIfNotExisting(preliminaryRound).subscribe()
        }
    }
}