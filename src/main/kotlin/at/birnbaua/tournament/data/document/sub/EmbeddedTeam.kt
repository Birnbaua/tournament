package at.birnbaua.tournament.data.document.sub

import org.springframework.data.mongodb.core.mapping.Field

class EmbeddedTeam {

    @Field(name = "no")
    var no: String? = null

    @Field(name = "name")
    var name: String? = null
}