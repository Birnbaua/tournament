package at.birnbaua.tournament

import at.birnbaua.tournament.config.tournament.feizi.GroupInternalRound
import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.service.feizi.SimpleMatchGeneratingService
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FeiziMatchGeneratingTests {

    @Test
    fun testMatchGenerating() {
        val noOfTeams = 51
        val noOfFields = 11
        val noOfGroups = if(noOfTeams % 5 == 0) noOfTeams/5 else noOfTeams/5 +1
        val gameroundTemplate = GroupInternalRound().genGameroundTemplate("Gruppenphase","Beschreibung", noOfGroups)
        gameroundTemplate.matchNumberOffset = 120
        val ggs = GameroundGeneratingService()
        val mgs = SimpleMatchGeneratingService()
        val teams = (0 until noOfTeams).map {
            val team = Team()
            team.no = it
            team.name = "Team $it"
            team
        }
        val fields = (0 until noOfFields).map {
            val field = Field()
            field.no = it
            field.name = "Field $it"
            field
        }
        val gameround = ggs.generate(gameroundTemplate,teams,0)
        gameround.groups.forEach {group ->
            group.teams.forEachIndexed { index, team ->
                team.name = "Team ${(group.no+65).toChar()}$index"
            }
        }
        val matches = mgs.generateMatchesFeizi(gameround, fields, LocalDateTime.now())
        matches.forEach { println(it) }
    }
}