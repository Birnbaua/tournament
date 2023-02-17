package at.birnbaua.tournament.data.document.sub

import org.springframework.data.mongodb.core.mapping.Field

data class EmbeddedTeam (

    @Field(name = "no")
    var no: Int = 0,

    @Field(name = "name")
    var name: String = ""
) {
    override fun hashCode(): Int { return no.hashCode() }
    override fun equals(other: Any?): Boolean {
        return if(other is EmbeddedTeam) {
            other.no == this.no
        } else {
            false
        }
    }
}