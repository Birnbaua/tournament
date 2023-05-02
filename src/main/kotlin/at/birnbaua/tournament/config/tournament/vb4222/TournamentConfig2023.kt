package at.birnbaua.tournament.config.tournament.vb4222

import at.birnbaua.tournament.data.document.template.GameroundTemplate
import at.birnbaua.tournament.data.document.template.TournamentTemplate
import at.birnbaua.tournament.data.document.template.TournamentTemplateProperties
import java.time.LocalDateTime

class TournamentConfig2023 {

    fun genTournamentTemplate(numberOfGroups: Int = 4) : TournamentTemplate {
        val template = TournamentTemplate()
        template.id = "vb_standard_$numberOfGroups"
        template.name = "VB4222 Standard"
        template.desc = "Template für ein Standard-Turnier des TSV St. Georgen an der Gusen mit $numberOfGroups Gruppen á 5 Teams"
        template.tournamentId = "vb4222"
        template.tournamentName = "Default VB4222"
        template.tournamentDesc = "Turnier der Sektion Volleyball des TSV St. Georgen an der Gusen mit $numberOfGroups Gruppen á 5 Teams"
        template.tournamentTitle = "Default VB4222"
        template.tournamentStart = LocalDateTime.of(2023,6,17,9,30)
        template.properties = TournamentTemplateProperties(numberOfGroups*5-4,numberOfGroups*5,numberOfGroups)
        template.gameroundTemplates = genGameroundConfig(numberOfGroups)
        return template
    }

    private fun genGameroundConfig(numberOfGroups: Int) : MutableMap<Int,GameroundTemplate> {
        val firstRound = GroupInternalRound().genGameroundTemplate("Vorrunde","VB4222 Vorrunde 2023", numberOfGroups)
        val secondRound = GroupInternalRound().genGameroundTemplate("Zwischenrunde","VB4222 Vorrunde 2023", numberOfGroups)
        val thirdRound = CrossPlaysRound().genGameroundTemplate("Kreuzspiele","VB4222 Kreuzspiele 2023", numberOfGroups)
        val fourthRound = PlacementRound().genGameroundTemplate("Platzierungsspiele", "VB4222 Platzierungsspiele 2023", numberOfGroups)
        firstRound.gameroundNumber = 0
        secondRound.gameroundNumber = 1
        thirdRound.gameroundNumber = 2
        fourthRound.gameroundNumber = 3
        return mutableMapOf(Pair(0,firstRound),Pair(1,secondRound),Pair(2,thirdRound),Pair(3,fourthRound))
    }
}