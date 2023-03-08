package at.birnbaua.tournament

import at.birnbaua.tournament.config.tournament.feizi.GroupInternalRound
import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.service.feizi.SimpleMatchGeneratingService
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class Feizi1MatchGeneratingTests {

    @Test
    fun testMatchGen20() {
        val matches = genMatches(genGameround(20))
        checkBasic(matches)
    }

    @Test
    fun testMatchGen21() {
        val matches = genMatches(genGameround(21))
    }

    @Test
    fun testMatchGen22() {
        val matches = genMatches(genGameround(22)).forEach { println(it) }
    }

    @Test
    fun testMatchGen23() {
        val matches = genMatches(genGameround(23)).forEach { println(it) }
    }

    @Test
    fun testMatchGen24() {
        val matches = genMatches(genGameround(24)).forEach { println(it) }
    }

    @Test
    fun testMatchGenerating() {
        genMatches(genGameround(20)).forEach { println(it) }
    }

    private fun checkBasic(matches: List<Match>) {
        matches.forEach { match ->
            assert(match.teamA?.no != match.teamB?.no)
            assert(match.teamA?.no != match.referee?.no)
            assert(match.teamB?.no != match.referee?.no)
        }
    }

    private fun genMatches(gr: Gameround) : List<Match> {
        val mgs = SimpleMatchGeneratingService()
        val fields = (0 until gr.groups.size).map {
            val field = Field()
            field.no = it
            field.name = "Field $it"
            field
        }
        return mgs.generateMatchesFeizi(gr,fields,LocalDateTime.of(2023,6,17,9,0,0))
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