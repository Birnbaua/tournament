package at.birnbaua.tournament.data.startup

import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.sub.EmbeddedTeam
import at.birnbaua.tournament.data.service.MatchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@EnableScheduling
@Configuration
class Test {

    @Autowired
    private lateinit var ms: MatchService

    @Scheduled(initialDelay = 500, fixedDelay = 1000*60)
    fun test() {
        val t = "TEST"
        ms.deleteAllByTournament(t).subscribe()
        val teamA = EmbeddedTeam()
        teamA.no = "1"
        teamA.name = "UNCHANGED"
        val match = Match()
        match.tournament = t
        match.no = "1"
        match.teamA = teamA
        match.teamB = teamA
        match.referee = teamA
        ms.insert(match).subscribe()
        ms.updateTeamNameByTournamentAndNo(t, "1","EUREKA!").subscribe()
        ms.findAllByTournament(t).map { it.teamA }.subscribe {println(it!!.name)}
    }
}