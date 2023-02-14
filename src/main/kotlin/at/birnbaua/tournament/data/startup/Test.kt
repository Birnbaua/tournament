package at.birnbaua.tournament.data.startup

import at.birnbaua.tournament.config.tournament.feizi.GroupInternalRound
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.document.sub.EmbeddedTeam
import at.birnbaua.tournament.data.service.MatchService
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@EnableScheduling
@Configuration
class Test {

    @Autowired
    private lateinit var ms: MatchService

    @Autowired
    private lateinit var ggs: GameroundGeneratingService

    @Scheduled(initialDelay = 500, fixedDelay = 1000*60)
    fun test() {
        val teams = (0..10)
            .map {
                val team = Team()
                team.no = it
                team.name = "Team $it"
                team
            }
        val groupsNumber = if(teams.size%5 == 0) teams.size/5 else teams.size/5 + 1
        val template = GroupInternalRound().genGameroundTemplate("test round", "test desc",groupsNumber)
        ggs.generate(template, teams).groups.forEach{ println(it.teams) }
    }
}