package at.birnbaua.tournament.data.document.sub

import org.springframework.data.mongodb.core.mapping.Field

class EmbeddedSet {

    @Field(name = "no")
    var no: Long = 0

    @Field(name = "points_a")
    var pointsA: Long = 0

    @Field(name = "points_b")
    var pointsB: Long = 0
}