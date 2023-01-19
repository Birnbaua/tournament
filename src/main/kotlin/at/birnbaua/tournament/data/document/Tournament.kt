package at.birnbaua.tournament.data.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(value = "tournament")
class Tournament : AbstractDocument() {

    @Id
    @Field(name = "_id")
    var id: String? = null

    @Field(name = "name")
    var name: String? = null

    @Field(name = "title")
    var title: String? = null

    @Field(name = "start")
    var start: LocalDateTime? = null
}