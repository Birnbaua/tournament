package at.birnbaua.tournament.data.document

import at.birnbaua.tournament.data.document.sub.EmbeddedGroup
import at.birnbaua.tournament.data.document.sub.EmbeddedResult
import at.birnbaua.tournament.util.Tree
import com.fasterxml.jackson.annotation.JsonInclude
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(value = "gameround")
@CompoundIndexes(value = [CompoundIndex(name = "gameround_index", def = "{'tournament': 1, 'no': 1}", unique = true)])
class Gameround {

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

    @Field(name = "groups")
    var groups: MutableList<EmbeddedGroup> = mutableListOf()

    @Field(name = "group_binding")
    var groupBinding: Tree<String,String> = Tree()

    @Field(name = "results")
    var results: MutableList<EmbeddedResult> = mutableListOf()

    @Field(name = "audit")
    var audit: AuditEntry = AuditEntry()
}