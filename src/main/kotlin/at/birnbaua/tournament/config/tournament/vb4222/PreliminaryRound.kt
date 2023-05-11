package at.birnbaua.tournament.config.tournament.vb4222

import at.birnbaua.tournament.data.document.sub.gameround.MatchMakingConfig
import at.birnbaua.tournament.data.document.sub.gameround.MatchTemplate
import at.birnbaua.tournament.data.document.template.GameroundTemplate
import at.birnbaua.tournament.data.service.feizi.GroupMakingConfig
import at.birnbaua.tournament.data.service.feizi.OrderProperty
import at.birnbaua.tournament.data.service.feizi.SimpleOrderConfig

@Suppress("unused")
class PreliminaryRound() {
    var id = "vb4222_1"
    var noOfSets = 2
    var setTime = 8
    var setBreakTime = 0
    var matchBreakTime = 5
    var matchNumberOffset = 0
    var flattenTeamNumberOfGroups: Boolean = true

    constructor(noOfSets: Int, setTime: Int, setBreakTime: Int, matchBreakTime: Int) : this() {
        this.noOfSets = noOfSets
        this.setTime = setTime
        this.setBreakTime = setBreakTime
        this.matchBreakTime = matchBreakTime
    }

    private fun genMatchTemplates() : MutableList<MatchTemplate> {
        return  mutableListOf(
            MatchTemplate(0,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 0), MatchTemplate.TeamTemplate(rank = 2), MatchTemplate.TeamTemplate(rank = 3)
            ),
            MatchTemplate(1,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 1), MatchTemplate.TeamTemplate(rank = 3), MatchTemplate.TeamTemplate(rank = 4)
            ),
            MatchTemplate(2,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 2), MatchTemplate.TeamTemplate(rank = 4), MatchTemplate.TeamTemplate(rank = 1)
            ),
            MatchTemplate(3,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 0), MatchTemplate.TeamTemplate(rank = 3), MatchTemplate.TeamTemplate(rank = 2)
            ),
            MatchTemplate(4,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 1), MatchTemplate.TeamTemplate(rank = 4), MatchTemplate.TeamTemplate(rank = 0)
            ),
            MatchTemplate(5,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 2), MatchTemplate.TeamTemplate(rank = 3), MatchTemplate.TeamTemplate(rank = 1)
            ),
            MatchTemplate(6,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 0), MatchTemplate.TeamTemplate(rank = 4), MatchTemplate.TeamTemplate(rank = 3)
            ),
            MatchTemplate(7,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 1), MatchTemplate.TeamTemplate(rank = 2), MatchTemplate.TeamTemplate(rank = 0)
            ),
            MatchTemplate(8,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 3), MatchTemplate.TeamTemplate(rank = 4), MatchTemplate.TeamTemplate(rank = 2)
            ),
            MatchTemplate(9,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 0), MatchTemplate.TeamTemplate(rank = 1), MatchTemplate.TeamTemplate(rank = 4)
            )
        )
    }

    private fun genMatchMakingConfig() : MatchMakingConfig {
        val mmc = MatchMakingConfig()
        mmc.groupStartingIndex = 0
        mmc.fieldStartingIndex = 0
        mmc.rankStartingIndex = 0
        mmc.doOnMultiple = MatchMakingConfig.OnMultiple.REPEAT
        mmc.startWithField = 0
        mmc.templates = genMatchTemplates()
        return mmc
    }

    private fun genGroupMakingConfig() : GroupMakingConfig {
        val config = GroupMakingConfig()
        config.isReversed = false
        config.ordering = arrayListOf(OrderProperty.EXTERNAL_RANK,OrderProperty.TEAM_NO)
        return config
    }

    fun genGameroundTemplate(name: String, desc: String, numberOfGroups: Int = 1) : GameroundTemplate {
        if(numberOfGroups <=0 ||numberOfGroups >= 12) throw IllegalArgumentException("The number of group needs to be between 1 and 12!")
        val template =  gen(name,desc,numberOfGroups)
        template.id = this.id
        template.groups = numberOfGroups
        template.orderConfig = genOrderConfig()
        return template
    }

    private fun genOrderConfig(): SimpleOrderConfig {
        val config = SimpleOrderConfig()
        config.pointsPerSetWon = 2
        config.pointsPerSetDraw = 1
        config.pointsPerMatchWon = 0
        config.pointsPerMatchDraw = 0
        config.groupInternalOrdering = listOf(OrderProperty.POINTS,OrderProperty.GAME_POINTS,OrderProperty.EXTERNAL_CORRECTION,OrderProperty.INTERNAL_CORRECTION)
        config.groupExternalOrdering = listOf(OrderProperty.INTERNAL_RANK,OrderProperty.POINTS,OrderProperty.GAME_POINTS,OrderProperty.EXTERNAL_CORRECTION)
        return config
    }

    private fun gen(name: String, desc: String, no: Int) : GameroundTemplate {
        val grt = GameroundTemplate()
        grt.groupBinding.addOrReplace(null, setOf(), (0 until no).toList())
        grt.matchMakingConfig[null] = genMatchMakingConfig()
        grt.groupMakingConfig[null] = genGroupMakingConfig()
        grt.defaultGroupSize = 5
        grt.name = "Template $name of style <Feizi> with $no groups"
        grt.flattenGroupsOnImproperTeamNumber = true
        grt.desc = desc
        grt.gameroundName = name
        grt.desc = desc
        return grt
    }
}