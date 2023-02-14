package at.birnbaua.tournament.data.document.sub

import org.springframework.data.mongodb.core.mapping.Field

data class EmbeddedTeam (

    @Field(name = "no")
    var no: Int = 0,

    @Field(name = "name")
    var name: String = ""
)