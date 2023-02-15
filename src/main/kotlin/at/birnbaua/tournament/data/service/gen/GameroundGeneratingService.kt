package at.birnbaua.tournament.data.service.gen

import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.document.sub.EmbeddedGroup
import at.birnbaua.tournament.data.document.sub.EmbeddedTeam
import at.birnbaua.tournament.data.document.template.GameroundTemplate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GameroundGeneratingService {

    private val log = LoggerFactory.getLogger(GameroundGeneratingService::class.java)

    fun generate(template: GameroundTemplate, teams: List<Team>, gameroundNumber: Int? = null) : Gameround {
        val gr = Gameround()
        log.debug("Generate Gameround instance with ${teams.size} teams.")
        if(gameroundNumber != null) gr.no = gameroundNumber else gr.no = template.gameroundNumber
        gr.name = template.gameroundName
        gr.desc = template.gameroundDesc
        gr.groupBinding = template.groupBinding
        gr.matchMakingConfigs = template.matchMakingConfig
        gr.groups.addAll(toGroups(template, teams))
        return gr
    }

    private fun toGroups(template: GameroundTemplate, teams: List<Team>) : List<EmbeddedGroup> {
        val groups: MutableList<EmbeddedGroup> = mutableListOf()
        var group = EmbeddedGroup()
        log.debug("Transform ${teams.size} teams to groups...")
        for ((index,team) in teams.withIndex()) {
            if(index%template.defaultGroupSize == 0) {
                group = EmbeddedGroup(index / template.defaultGroupSize)
                group.name = "Group ${group.no}"
                log.debug("Generated new group with number: ${group.no}")
            }
            group.teams.add(EmbeddedTeam(team.no, team.name))
            if((index + 1)%template.defaultGroupSize == 0) {
                log.trace("Prepare for adding group with no: ${group.no} to group list...")
                groups.add(group)
                log.trace("Added group with no: ${group.no} to group list")
                log.trace("Current size of group list: ${groups.size}")
            }
        }
        if(groups.contains(group).not()) groups.add(group)
        if(template.flattenGroupsOnImproperTeamNumber && template.defaultGroupSize%teams.size != 0) {
            flattenGroups(groups,template.defaultGroupSize)
        }
        return groups
    }

    private fun flattenGroups(groups: List<EmbeddedGroup>, defaultGroupSize: Int) {
        val differenceToDefaultSize = defaultGroupSize - groups.last().teams.size
        var remaining = differenceToDefaultSize
        while (remaining > 0) {
            val offset = (differenceToDefaultSize - remaining)%groups.size
            val team = groups[groups.size - offset - 1].teams.removeLast()
            groups.last().teams.add(team)
            remaining  = remaining.dec()
        }
    }
}