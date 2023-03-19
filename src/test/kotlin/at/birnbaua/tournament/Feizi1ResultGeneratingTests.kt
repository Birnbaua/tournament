package at.birnbaua.tournament

import at.birnbaua.tournament.config.tournament.vb4222.GroupInternalRound
import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.service.feizi.SimpleMatchGeneratingService
import at.birnbaua.tournament.data.service.feizi.SimpleOrderService
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.random.Random

class Feizi1ResultGeneratingTests {

    @Test
    fun testResults20Played() {
        val gr = genGameround(20)
        val matches = genMatches(gr)
        val sos = SimpleOrderService()
        playMatches(matches)
        val results = sos.genResults(matches,gr.results.associateBy{ it.team },gr.groups,gr.orderConfig,0)
        results.sortedBy { it.externalRank }.forEachIndexed { index, result ->
            assert(result.internalRank.toInt() == index/(gr.groups.size))
        }
    }

    @Test
    fun testResults20Draw() {
        val gr = genGameround(20)
        val matches = genMatches(gr)
        val sos = SimpleOrderService()
        playMatchesDraw(matches)
        val results = sos.genResults(matches,gr.results.associateBy{ it.team },gr.groups,gr.orderConfig,0)
        results.sortedBy { it.externalRank }.forEach { println(it) }
    }

    @Test
    fun testResults20Unplayed() {
        val gr = genGameround(20)
        val matches = genMatches(gr)
        val sos = SimpleOrderService()
        val results = sos.genResults(matches,gr.results.associateBy{ it.team },gr.groups,gr.orderConfig,0)
        results.sortedBy { it.externalRank }.forEach { println(it) }
    }

    @Test
    fun testResults60Played() {
        val gr = genGameround(60)
        val matches = genMatches(gr)
        val sos = SimpleOrderService()
        playMatches(matches)
        val results = sos.genResults(matches,gr.results.associateBy{ it.team },gr.groups,gr.orderConfig,0)
        results.sortedBy { it.externalRank }.forEachIndexed { index, result ->
            assert(result.internalRank.toInt() == index/(gr.groups.size))
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
        return mgs.generateMatchesFeizi(gr,fields, LocalDateTime.of(2023,6,17,9,0,0))
    }

    private fun playMatchesDraw(matches: List<Match>) {
        matches.forEach { match ->
            match.sets.forEach { set ->
                set.pointsA = if(match.teamA == null || match.teamB == null) 0 else 10L
                set.pointsB = if(match.teamA == null || match.teamB == null) 0 else 10L
            }
        }
    }

    private fun playMatches(matches: List<Match>) {
        matches.forEach { match ->
            match.sets.forEach { set ->
                set.pointsA = if(match.teamA == null || match.teamB == null) 0 else (10-match.teamA!!.no).toLong()
                set.pointsB = if(match.teamA == null || match.teamB == null) 0 else (10-match.teamB!!.no).toLong()
            }
        }
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