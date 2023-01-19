package at.birnbaua.tournament.data.document

import com.fasterxml.jackson.annotation.JsonInclude
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(value = "team")
@CompoundIndexes(value = [CompoundIndex(name = "team_index", def = "{'tournament': 1, 'no': 1}", unique = true)])
class Team : AbstractDocument() {

    @Id
    @Field(name = "_id")
    var id: ObjectId? = null

    @Field(name = "tournament")
    var tournament: String? = null

    @Field(name = "no")
    var no: String? = null

    @Field(name = "name")
    var name: String = ""

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Field(name = "desc", write = Field.Write.NON_NULL)
    var desc: String? = null

    @Field(name = "players")
    var players: MutableList<String> = mutableListOf()

    @Field(name = "is_referee")
    var isReferee: Boolean? = null
}