package at.birnbaua.tournament.data.document

import at.birnbaua.tournament.data.document.sub.EmbeddedGroup
import at.birnbaua.tournament.data.document.sub.gameround.MatchMakingConfig
import at.birnbaua.tournament.data.service.feizi.GroupMakingConfig
import at.birnbaua.tournament.data.service.feizi.SimpleOrderConfig
import at.birnbaua.tournament.data.service.feizi.SimpleResult
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
class Gameround() {

    @Id
    @Field(name = "_id")
    var id: ObjectId? = null

    @Field(name = "tournament")
    var tournament: String? = null

    @Field(name = "no")
    var no: Int = 0

    @Field(name = "name")
    var name: String = ""

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Field(name = "desc", write = Field.Write.NON_NULL)
    var desc: String? = null

    @Field(name = "groups")
    var groups: MutableList<EmbeddedGroup> = mutableListOf()

    @Field(name = "group_binding")
    var groupBinding: Tree<String,Int> = Tree()

    @Field(name = "order_config")
    var orderConfig: SimpleOrderConfig = SimpleOrderConfig()

    @Field(name = "results")
    var results: MutableList<SimpleResult> = mutableListOf()

    @Field(name = "match_making_config")
    var matchMakingConfig: MutableMap<String?,MatchMakingConfig> = mutableMapOf()

    @Field(name = "group_making_config")
    var groupMakingConfig: MutableMap<String?, GroupMakingConfig> = mutableMapOf()

    @Field(name = "match_no_offset")
    var matchNoOffset: Int = 0

    @Field(name = "audit")
    var audit: AuditEntry = AuditEntry()
}