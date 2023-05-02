package at.birnbaua.tournament.data.service.gen

import at.birnbaua.tournament.data.document.AuditEntry
import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.document.Tournament
import at.birnbaua.tournament.data.document.template.TournamentTemplate
import at.birnbaua.tournament.data.service.FieldService
import at.birnbaua.tournament.data.service.TeamService
import at.birnbaua.tournament.data.service.TournamentService
import at.birnbaua.tournament.data.service.TournamentTemplateService
import at.birnbaua.tournament.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import reactor.util.function.Tuple3
import java.time.LocalDateTime

@Service
class TournamentGeneratingService {

    @Autowired private lateinit var tournamentService: TournamentService
    @Autowired private lateinit var tts: TournamentTemplateService
    @Autowired private lateinit var fs: FieldService
    @Autowired private lateinit var ts: TeamService

    fun generate(template: TournamentTemplate, noOfTeams: Int = template.properties.maxNoOfTeams, noOfFields: Int = template.properties.minNoOfFields) : Triple<Tournament,List<Team>,List<Field>> {
        val tournament = template.toTournament()
        val fields = (1 .. noOfFields)
            .map {
                val field = Field()
                field.tournament = tournament.id
                field.no = it
                field.name = "Field $it"
                field.desc = "This is field $it of tournament ${template.tournamentId}"
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
                team.desc = "This is team $it of tournament ${template.tournamentId}"
                team
            }.toList()
        return Triple(tournament,teams,fields)
    }

    fun generateAndInsert(id: String, template: TournamentTemplate, noOfTeams: Int = template.properties.maxNoOfTeams, noOfFields: Int = template.properties.minNoOfFields) : Mono<Tuple3<Tournament,List<Team>,List<Field>>> {
        val triple = generate(template, noOfTeams, noOfFields)
        triple.first.id = id
        return Mono.zip(
            tournamentService.insert(triple.first),
            ts.insert(triple.second).collectList(),
            fs.insert(triple.third).collectList()
        )
    }

    fun generateAndInsert(id: String, template: String, noOfTeams: Int = -1, noOfFields: Int = -1) : Mono<Tuple3<Tournament,List<Team>,List<Field>>> {
        return tts.findById(template)
            .switchIfEmpty { throw ResourceNotFoundException("Template with id: $template not found") }
            .flatMap {
                generateAndInsert(id,it,
                    if(noOfTeams == -1) it.properties.maxNoOfTeams else noOfTeams,
                    if(noOfFields == -1) it.properties.minNoOfFields else noOfFields)
            }
    }
}