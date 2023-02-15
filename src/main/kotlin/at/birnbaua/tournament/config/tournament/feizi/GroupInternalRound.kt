package at.birnbaua.tournament.config.tournament.feizi

import at.birnbaua.tournament.data.document.sub.gameround.MatchMakingConfig
import at.birnbaua.tournament.data.document.sub.gameround.MatchTemplate
import at.birnbaua.tournament.data.document.template.GameroundTemplate

@Suppress("unused")
class GroupInternalRound() {
    var noOfSets = 2
    var setTime = 8
    var setBreakTime = 0
    var matchBreakTime = 5
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

    fun genGameroundTemplate(name: String, desc: String, numberOfGroups: Int = 1) : GameroundTemplate {
        return when(numberOfGroups) {
            1,2,3,4 -> genSmall(name,desc,numberOfGroups)
            5,6,7,8 -> genMedium(name,desc,numberOfGroups)
            9,10,11,12 -> genLarge(name,desc,numberOfGroups)
            else -> throw IllegalArgumentException("The number of group needs to be between 1 and 12!")
        }
    }

    private fun genSmall(name: String, desc: String, no: Int) : GameroundTemplate {
        val grt = GameroundTemplate()
        grt.groupBinding.addOrReplace(null, setOf(), (0 until no).toList())
        grt.matchMakingConfig[null] = genMatchMakingConfig()
        grt.defaultGroupSize = 5
        grt.name = "Template $name of style <Feizi> with $no groups"
        grt.flattenGroupsOnImproperTeamNumber = true
        grt.desc = desc
        grt.gameroundName = name
        grt.desc = desc
        return grt
    }

    private fun genMedium(name: String, desc: String, no: Int) : GameroundTemplate {
        val grt = GameroundTemplate()
        grt.groupBinding.addOrReplace(null, setOf("PROFESSIONAL","HOBBY"), listOf())
        grt.groupBinding.addOrReplace("PROFESSIONAL", setOf(), (0 until 4).toList())
        grt.groupBinding.addOrReplace("HOBBY", setOf(), (4 until no).toList())
        grt.matchMakingConfig[null] = genMatchMakingConfig()
        grt.defaultGroupSize = 5
        grt.flattenGroupsOnImproperTeamNumber = true
        grt.name = "Template $name of style <Feizi> with $no groups"
        grt.desc = desc
        grt.gameroundName = name
        grt.desc = desc
        return grt
    }

    private fun genLarge(name: String, desc: String, no: Int) : GameroundTemplate {
        val grt = GameroundTemplate()
        grt.groupBinding.addOrReplace(null, setOf("PROFESSIONAL","HOBBY A","HOBBY B"), listOf())
        grt.groupBinding.addOrReplace("PROFESSIONAL", setOf(), (0 until 4).toList())
        grt.groupBinding.addOrReplace("HOBBY A", setOf(), (4 until 8).toList())
        grt.groupBinding.addOrReplace("HOBBY B", setOf(), (8 until 12).toList())
        grt.matchMakingConfig[null] = genMatchMakingConfig()
        grt.defaultGroupSize = 5
        grt.flattenGroupsOnImproperTeamNumber = true
        grt.name = "Template $name of style <Feizi> with $no groups"
        grt.desc = desc
        grt.gameroundName = name
        grt.desc = desc
        return grt
    }
}