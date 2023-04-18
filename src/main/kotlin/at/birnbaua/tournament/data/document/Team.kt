package at.birnbaua.tournament.data.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(value = "team")
@CompoundIndexes(value = [CompoundIndex(name = "team_index", def = "{'tournament': 1, 'no': 1}", unique = true)])
@Suppress("unused")
class Team() {

    @Id
    @Field(name = "_id")
    var id: ObjectId? = null

    @Field(name = "tournament")
    var tournament: String? = null

    @Field(name = "no")
    var no: Int = 0

    @Field(name = "name")
    var name: String = ""

    @Field(name = "desc", write = Field.Write.NON_NULL)
    var desc: String? = null

    @Field(name = "players")
    var players: MutableList<String>? = null

    @Field(name = "is_referee")
    var isReferee: Boolean? = null

    @Field(name = "audit")
    var audit: AuditEntry = AuditEntry()
}