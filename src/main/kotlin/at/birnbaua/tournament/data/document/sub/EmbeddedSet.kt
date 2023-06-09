package at.birnbaua.tournament.data.document.sub

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.mongodb.core.mapping.Field

class EmbeddedSet() {

    @Field(name = "no")
    var no: Long = 0

    @JsonProperty(value = "points_a")
    @Field(name = "points_a")
    var pointsA: Long = 0

    @JsonProperty(value = "points_b")
    @Field(name = "points_b")
    var pointsB: Long = 0

    constructor(no: Int) : this(){
        this.no = no.toLong()
    }

    override fun hashCode(): Int { return no.hashCode() }
    override fun equals(other: Any?): Boolean {
        return if(other is EmbeddedSet) {
            other.no == this.no
        } else {
            false
        }
    }
}