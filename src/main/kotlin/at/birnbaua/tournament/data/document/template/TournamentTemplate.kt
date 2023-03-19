package at.birnbaua.tournament.data.document.template

import at.birnbaua.tournament.data.document.Tournament
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(collection = "tournament_templates")
class TournamentTemplate {

    @Id
    @Field(name = "_id")
    var id: String? = null

    @Field(name = "template_name")
    var name: String = ""

    @Field(name = "template_desc")
    var desc: String = ""

    @Field(name = "tournament_id")
    var tournamentId: String? = null

    @Field(name = "tournament_name")
    var tournamentName: String = ""

    @Field(name = "tournament_desc")
    var tournamentDesc: String = ""

    @Field(name = "tournament_title")
    var tournamentTitle: String = ""

    @Field(name = "tournament_start")
    var tournamentStart: LocalDateTime = LocalDateTime.now().plusDays(1)

    @Field(name = "gameround_config")
    var gameroundConfig: MutableMap<Int,String> = mutableMapOf()

    fun toTournament() : Tournament {
        val tournament = Tournament()
        tournament.id = this.tournamentId
        tournament.name = this.tournamentName
        tournament.desc = this.tournamentDesc
        tournament.title = this.tournamentTitle
        tournament.start = this.tournamentStart
        tournament.gameroundTemplates = this.gameroundConfig
        return tournament
    }

}