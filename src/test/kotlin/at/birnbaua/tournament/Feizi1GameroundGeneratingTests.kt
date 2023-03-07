package at.birnbaua.tournament

import at.birnbaua.tournament.config.tournament.feizi.GroupInternalRound
import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.service.gen.GameroundGeneratingService
import org.junit.jupiter.api.Test

class Feizi1GameroundGeneratingTests {

    @Test
    fun test20() {
        val gr = generateGameround(20)
        checkOptimalFit(gr,20)
    }

    @Test
    fun test21() {
        val gr = generateGameround(21)
        check1TooMuch(gr,21)
    }

    @Test
    fun test22() {
        val gr = generateGameround(22)
        check2TooMuch(gr,22)
    }

    @Test
    fun test23() {
        val gr = generateGameround(23)
        check3TooMuch(gr,23)
    }

    @Test
    fun test24() {
        val gr = generateGameround(24)
        check4TooMuch(gr,24)
    }

    @Test
    fun test25() {
        val gr = generateGameround(25)
        checkOptimalFit(gr,25)
    }

    @Test
    fun test26() {
        val gr = generateGameround(26)
        check1TooMuch(gr,26)
    }

    @Test
    fun test27() {
        val gr = generateGameround(27)
        check2TooMuch(gr,27)
    }

    @Test
    fun test28() {
        val gr = generateGameround(28)
        check3TooMuch(gr,28)
    }

    @Test
    fun test29() {
        val gr = generateGameround(29)
        check4TooMuch(gr,29)
    }

    @Test
    fun test30() {
        val gr = generateGameround(20)
        checkOptimalFit(gr,20)
    }

    @Test
    fun test31() {
        val gr = generateGameround(31)
        check1TooMuch(gr,31)
    }

    @Test
    fun test32() {
        val gr = generateGameround(32)
        check2TooMuch(gr,32)
    }

    @Test
    fun test33() {
        val gr = generateGameround(33)
        check3TooMuch(gr,33)
    }

    @Test
    fun test34() {
        val gr = generateGameround(34)
        check4TooMuch(gr,34)
    }

    @Test
    fun test35() {
        val gr = generateGameround(35)
        checkOptimalFit(gr,35)
    }

    @Test
    fun test36() {
        val gr = generateGameround(36)
        check1TooMuch(gr,36)
    }

    @Test
    fun test37() {
        val gr = generateGameround(37)
        check2TooMuch(gr,37)
    }

    @Test
    fun test38() {
        val gr = generateGameround(38)
        check3TooMuch(gr,38)
    }

    @Test
    fun test39() {
        val gr = generateGameround(39)
        check4TooMuch(gr,39)
    }

    @Test
    fun test40() {
        val gr = generateGameround(40)
        checkOptimalFit(gr,40)
    }

    @Test
    fun test41() {
        val gr = generateGameround(41)
        check1TooMuch(gr,41)
    }

    @Test
    fun test42() {
        val gr = generateGameround(42)
        check2TooMuch(gr,42)
    }

    @Test
    fun test43() {
        val gr = generateGameround(43)
        check3TooMuch(gr,43)
    }

    @Test
    fun test44() {
        val gr = generateGameround(44)
        check4TooMuch(gr,44)
    }

    @Test
    fun test45() {
        val gr = generateGameround(45)
        checkOptimalFit(gr,45)
    }

    @Test
    fun test46() {
        val gr = generateGameround(46)
        check1TooMuch(gr,46)
    }

    @Test
    fun test47() {
        val gr = generateGameround(47)
        check2TooMuch(gr,47)
    }

    @Test
    fun test48() {
        val gr = generateGameround(48)
        check3TooMuch(gr,48)
    }

    @Test
    fun test49() {
        val gr = generateGameround(49)
        check4TooMuch(gr,49)
    }

    @Test
    fun test50() {
        val gr = generateGameround(50)
        checkOptimalFit(gr,50)
    }

    @Test
    fun test51() {
        val gr = generateGameround(51)
        check1TooMuch(gr,51)
    }

    @Test
    fun test52() {
        val gr = generateGameround(52)
        check2TooMuch(gr,52)
    }

    @Test
    fun test53() {
        val gr = generateGameround(53)
        check3TooMuch(gr,53)
    }

    @Test
    fun test54() {
        val gr = generateGameround(54)
        check4TooMuch(gr,54)
    }

    @Test
    fun test55() {
        val gr = generateGameround(55)
        checkOptimalFit(gr,55)
    }

    @Test
    fun test56() {
        val gr = generateGameround(56)
        check1TooMuch(gr,56)
    }

    @Test
    fun test57() {
        val gr = generateGameround(57)
        check2TooMuch(gr,57)
    }

    @Test
    fun test58() {
        val gr = generateGameround(58)
        check3TooMuch(gr,58)
    }

    @Test
    fun test59() {
        val gr = generateGameround(59)
        check4TooMuch(gr,59)
    }

    @Test
    fun test60() {
        val gr = generateGameround(60)
        checkOptimalFit(gr,60)
    }

    private fun check4TooMuch(gr: Gameround, teams: Int) {
        assert(gr.groups.size == teams/5 +1)
        assert(gr.groups.last().teams.size == 4)
        gr.groups.subList(0,gr.groups.size - 1).forEach { group -> assert(group.teams.size == 5) }
    }

    private fun check3TooMuch(gr: Gameround, teams: Int) {
        assert(gr.groups.size == teams/5 +1)
        assert(gr.groups[gr.groups.size - 2].teams.size == 4)
        assert(gr.groups.last().teams.size == 4)
        gr.groups.subList(0,gr.groups.size - 2).forEach { group -> assert(group.teams.size == 5) }
    }

    private fun check2TooMuch(gr: Gameround, teams: Int) {
        assert(gr.groups.size == teams/5 + 1)
        assert(gr.groups[gr.groups.size - 3].teams.size == 4)
        assert(gr.groups[gr.groups.size - 2].teams.size == 4)
        assert(gr.groups.last().teams.size == 4)
        gr.groups.subList(0,gr.groups.size - 3).forEach { group -> assert(group.teams.size == 5) }
    }

    private fun check1TooMuch(gr: Gameround, teams: Int) {
        assert(gr.groups.size == teams/5 + 1)
        assert(gr.groups[gr.groups.size - 4].teams.size == 4)
        assert(gr.groups[gr.groups.size - 3].teams.size == 4)
        assert(gr.groups[gr.groups.size - 2].teams.size == 4)
        assert(gr.groups.last().teams.size == 4)
        gr.groups.subList(0,gr.groups.size - 4).forEach { group -> assert(group.teams.size == 5) }
    }

    private fun checkOptimalFit(gr: Gameround, teams: Int) {
        assert(gr.groups.size == teams/5)
        assert(0 == teams%5)
        gr.groups.forEach { group -> assert(group.teams.size == 5) }
    }
    private fun generateGameround(noOfTeams: Int) : Gameround {
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
        return ggs.generate(gameroundTemplate,teams,0)
    }
}