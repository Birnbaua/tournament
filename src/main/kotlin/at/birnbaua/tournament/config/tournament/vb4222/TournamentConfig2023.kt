package at.birnbaua.tournament.config.tournament.vb4222

import at.birnbaua.tournament.data.document.template.TournamentTemplate
import java.time.LocalDateTime

class TournamentConfig2023 {

    fun genTournamentTemplate(start: LocalDateTime = LocalDateTime.of(2023,6,17,9,30)) : TournamentTemplate {
        val template = TournamentTemplate()
        template.id = "vb_standard"
        template.name = "VB4222 Standard"
        template.desc = "Template für ein Standard-Turnier des TSV St. Georgen an der Gusen"
        template.tournamentId = "vb4222"
        template.tournamentName = "Default VB4222"
        template.tournamentDesc = "Turnier der Sektion Volleyball des TSV St. Georgen an der Gusen"
        template.tournamentTitle = "Default VB4222"
        template.tournamentStart = start
        template.gameroundConfig = genGameroundConfig()
        return template
    }

    private fun genGameroundConfig() : MutableMap<Int,String> {
        return mutableMapOf(Pair(0,"vb4222_0"),Pair(1,"vb4222_1"),Pair(2,"vb4222_2"),Pair(3,"vb4222_3"))
    }
}