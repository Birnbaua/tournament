package at.birnbaua.tournament.data.document.sub

import org.springframework.data.mongodb.core.mapping.Field

class EmbeddedField() {

    @Field(name = "no")
    var no: Int = 0

    @Field(name = "name")
    var name: String? = null

    constructor(no: Int, name: String?) : this() {
        this.no = no
        this.name = name
    }

    override fun hashCode(): Int { return no.hashCode() }
    override fun equals(other: Any?): Boolean {
        return if(other is EmbeddedField) {
            other.no == this.no
        } else {
            false
        }
    }
}