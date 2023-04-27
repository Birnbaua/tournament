package at.birnbaua.tournament.data.document

import at.birnbaua.tournament.data.document.template.GameroundTemplate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable
import java.time.LocalDateTime

@Document(value = "tournament")
class Tournament() : Serializable {

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
    var gameroundTemplates: MutableMap<Int,GameroundTemplate> = mutableMapOf()

    @Field(name = "audit")
    var audit: AuditEntry = AuditEntry()
}