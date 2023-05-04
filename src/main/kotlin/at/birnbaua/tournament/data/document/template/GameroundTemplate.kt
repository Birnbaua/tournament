package at.birnbaua.tournament.data.document.template

import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.sub.gameround.MatchMakingConfig
import at.birnbaua.tournament.data.service.feizi.GroupMakingConfig
import at.birnbaua.tournament.data.service.feizi.SimpleOrderConfig
import at.birnbaua.tournament.util.Tree
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "gameround_template")
class GameroundTemplate {

    @Id
    @Field(name = "_id")
    var id: String? = null

    @Field(name = "template_name")
    var name: String = "Insert template name"

    @Field(name = "template_desc")
    var desc: String = "Insert template description"

    @Field(name = "tournament")
    var tournament: String = ""

    @Field(name = "groups")
    var groups: Int = 0

    @Field(name = "gameround_number")
    var gameroundNumber: Int = 0

    @Field(name = "gameround_name")
    var gameroundName: String = "Insert gameround name!"

    @Field(name = "gameround_desc")
    var gameroundDesc: String = "Insert gameround description"

    @Field(name = "group_binding")
    var groupBinding: Tree<String,Int> = Tree()

    @Field(name = "default_group_size")
    var defaultGroupSize: Int = 5

    @Field(name = "match_number_offset")
    var matchNumberOffset: Int = 0

    @Field(name = "flatten_groups_on_improper_team_number")
    var flattenGroupsOnImproperTeamNumber: Boolean = false

    @Field(name = "order_config")
    var orderConfig: SimpleOrderConfig = SimpleOrderConfig()

    @Field(name = "match_making_config")
    var matchMakingConfig: MutableMap<String?,MatchMakingConfig> = mutableMapOf()

    @Field(name = "group_making_config")
    var groupMakingConfig: MutableMap<String?,GroupMakingConfig> = mutableMapOf()

    fun toGameround() : Gameround {
        val gr = Gameround()
        gr.no = this.gameroundNumber
        gr.matchNoOffset = this.matchNumberOffset
        gr.name = this.name
        gr.desc = this.desc
        gr.groupBinding = this.groupBinding
        gr.orderConfig = this.orderConfig
        gr.matchMakingConfig = this.matchMakingConfig
        gr.groupMakingConfig = this.groupMakingConfig
        return gr
    }
}