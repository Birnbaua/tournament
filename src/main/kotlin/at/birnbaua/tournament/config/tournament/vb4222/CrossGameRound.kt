package at.birnbaua.tournament.config.tournament.vb4222

import at.birnbaua.tournament.data.document.sub.gameround.MatchMakingConfig
import at.birnbaua.tournament.data.document.sub.gameround.MatchTemplate
import at.birnbaua.tournament.data.document.template.GameroundTemplate

@Suppress("unused")
class CrossGameRound() {
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
                MatchTemplate.TeamTemplate(rank = 0), MatchTemplate.TeamTemplate(rank = 1), MatchTemplate.TeamTemplate(rank = 2)
            ),
            MatchTemplate(1,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 2), MatchTemplate.TeamTemplate(rank = 3), MatchTemplate.TeamTemplate(rank = 1)
            ),
            MatchTemplate(2,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 4), MatchTemplate.TeamTemplate(rank = 5), MatchTemplate.TeamTemplate(rank = 6)
            ),
            MatchTemplate(3,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 6), MatchTemplate.TeamTemplate(rank = 7), MatchTemplate.TeamTemplate(rank = 5)
            ),
            MatchTemplate(4,noOfSets,setTime,setBreakTime,matchBreakTime,
                MatchTemplate.TeamTemplate(rank = 8), MatchTemplate.TeamTemplate(rank = 9), MatchTemplate.TeamTemplate(rank = 7)
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
            1,2 -> genSmall(name,desc,numberOfGroups)
            3,4 -> genMedium(name,desc,numberOfGroups)
            5,6,7,8 -> genLarge(name,desc,numberOfGroups)
            9,10,11,12 -> genXLarge(name,desc,numberOfGroups)
            else -> throw IllegalArgumentException("The number of group needs to be between 1 and 12!")
        }
    }

    private fun genSmall(name: String, desc: String, no: Int) : GameroundTemplate {
        val grt = GameroundTemplate()
        grt.groupBinding.addOrReplace(null, setOf(), (0 until no).toList())
        grt.matchMakingConfig[null] = genMatchMakingConfig()
        grt.name = "Template $name of style <Feizi> with $no groups"
        grt.desc = desc
        grt.gameroundName = name
        grt.desc = desc
        return grt
    }

    private fun genMedium(name: String, desc: String, no: Int) : GameroundTemplate {
        val grt = GameroundTemplate()
        grt.groupBinding.addOrReplace(null, setOf(), listOf())
        grt.groupBinding.addOrReplace("PROFESSIONAL", setOf(), (0..1).toList())
        grt.groupBinding.addOrReplace("HOBBY", setOf(), (2 until no).toList())
        grt.matchMakingConfig[null] = genMatchMakingConfig()
        grt.name = "Template $name of style <Feizi> with $no groups"
        grt.desc = desc
        grt.gameroundName = name
        grt.desc = desc
        return grt
    }

    private fun genLarge(name: String, desc: String, no: Int) : GameroundTemplate {
        val grt = GameroundTemplate()
        grt.groupBinding.addOrReplace(null, setOf(), listOf())
        grt.groupBinding.addOrReplace("PROFESSIONAL", setOf(), (0..3).toList())
        grt.groupBinding.addOrReplace("HOBBY", setOf(), (4 until no).toList())
        grt.matchMakingConfig[null] = genMatchMakingConfig()
        grt.name = "Template $name of style <Feizi> with $no groups"
        grt.desc = desc
        grt.gameroundName = name
        grt.desc = desc
        return grt
    }

    private fun genXLarge(name: String, desc: String, no: Int) : GameroundTemplate {
        val grt = GameroundTemplate()
        grt.groupBinding.addOrReplace(null, setOf(), listOf())
        grt.groupBinding.addOrReplace("PROFESSIONAL", setOf(), (0..3).toList())
        grt.groupBinding.addOrReplace("HOBBY 1", setOf(), (4..7).toList())
        grt.groupBinding.addOrReplace("HOBBY 2", setOf(), (8 until no).toList())
        grt.matchMakingConfig[null] = genMatchMakingConfig()
        grt.name = "Template $name of style <Feizi> with $no groups"
        grt.desc = desc
        grt.gameroundName = name
        grt.desc = desc
        return grt
    }
}