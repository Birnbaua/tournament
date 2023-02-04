package at.birnbaua.tournament.data.document.sub

import org.springframework.data.mongodb.core.mapping.Field


class EmbeddedGroup {

    @Field(name = "no")
    var no: String? = null

    @Field(name = "name")
    var name: String? = null

    @Field(name = "teams")
    var teams: MutableList<EmbeddedTeam> = mutableListOf()

}