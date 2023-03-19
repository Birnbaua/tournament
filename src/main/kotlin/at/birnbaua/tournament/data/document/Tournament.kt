package at.birnbaua.tournament.data.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(value = "tournament")
class Tournament {

    @Id
    @Field(name = "_id")
    var id: String? = null

    @Field(name = "name")
    var name: String? = null

    @Field(name = "desc")
    var desc: String? = null

    @Field(name = "title")
    var title: String? = null

    @Field(name = "start")
    var start: LocalDateTime? = null

    @Field(name = "gameround_templates")
    var gameroundTemplates: MutableMap<Int,String> = mutableMapOf()

    @Field(name = "audit")
    var audit: AuditEntry = AuditEntry()
}