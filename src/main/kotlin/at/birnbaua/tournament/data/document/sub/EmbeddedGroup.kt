package at.birnbaua.tournament.data.document.sub

import org.springframework.data.mongodb.core.mapping.Field


class EmbeddedGroup() {

    @Field(name = "no")
    var no: Int = 0

    @Field(name = "name")
    var name: String? = null

    @Field(name = "teams")
    var teams: MutableList<EmbeddedTeam> = mutableListOf()

    constructor(no: Int) : this() {
        this.no = no
    }

    override fun hashCode(): Int { return no.hashCode() }
    override fun equals(other: Any?): Boolean {
        return if(other is EmbeddedGroup) {
            other.no == this.no
        } else {
            false
        }
    }

}