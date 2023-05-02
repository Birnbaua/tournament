package at.birnbaua.tournament.data.document.template

import org.springframework.data.mongodb.core.mapping.Field

class TournamentTemplateProperties() {
    @Field(value = "min_no_of_teams")
    var minNoOfTeams: Int = 26
    @Field(value = "max_no_of_teams")
    var maxNoOfTeams: Int = 30
    @Field(value = "min_no_of_fields")
    var minNoOfFields: Int = 6

    constructor(minTeams: Int, maxTeams: Int, minFields: Int) : this() {
        minNoOfFields = minFields
        minNoOfTeams = minTeams
        maxNoOfTeams = maxTeams
    }
}