package at.birnbaua.tournament.data.service.gen

import at.birnbaua.tournament.data.document.AuditEntry
import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.document.Tournament
import at.birnbaua.tournament.data.document.template.TournamentTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TournamentGeneratingService {

    fun generate(template: TournamentTemplate, noOfTeams: Int = template.properties.maxNoOfTeams, noOfFields: Int = template.properties.minNoOfFields) : Triple<Tournament,List<Team>,List<Field>> {
        val tournament = template.toTournament()
        val fields = (1 .. noOfFields)
            .map {
                val field = Field()
                field.tournament = tournament.id
                field.no = it
                field.name = "Field $it"
                field.desc = "This is field $it of tournament ${tournament.id}"
                field
            }.toList()
        val teams = (1 .. noOfTeams)
            .map {
                val team = Team()
                val time = LocalDateTime.now()
                team.tournament = tournament.id
                team.no = it
                team.name = "Team $it"
                team.isReferee = true
                team.audit = AuditEntry("auto","auto", time,time)
                team.desc = "This is team $it of tournament ${tournament.id}"
                team
            }.toList()
        return Triple(tournament,teams,fields)
    }
}