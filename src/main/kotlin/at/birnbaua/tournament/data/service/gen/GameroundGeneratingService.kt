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
        val gr = genDataFromTemplate(template, gameroundNumber)
        log.debug("Generate Gameround instance with ${teams.size} teams.")
        gr.groups.addAll(toGroups(template, teams))
        return gr
    }

    fun generateFeiziRound1(template: GameroundTemplate, teams: List<Team>, gameroundNumber: Int? = null) : Gameround {
        return generate(template, teams, gameroundNumber)
    }

    fun generateFeiziRound2(template: GameroundTemplate, oldGameround: Gameround) : Gameround {
        return generateFeiziRound1(template, oldGameround.results.sortedBy { it.externalRank }.map { it.team.toTeam() },oldGameround.no + 1)
    }

    fun generateFeiziRound3(template: GameroundTemplate, oldGameround: Gameround) : Gameround {
        val gr = genDataFromTemplate(template, oldGameround.no + 1)
        val oldGroups = oldGameround.groups.sortedBy { it.no }
        val newGroups = mutableListOf<EmbeddedGroup>()
        for(i in 0 .. oldGroups.size/2) {
            val group = EmbeddedGroup()
            group.no = i
            group.name = "Group $i"
            val group0 = oldGroups[i]
            val group1 = oldGroups[i+1]
            group.teams = oldGameround.results
                .asSequence()
                .filter { group0.teams.contains(it.team) || group1.teams.contains(it.team) }
                .map {
                    if (group0.teams.contains(it.team)) {
                        Pair(it.internalRank + ((it.internalRank-1) * 2),it)
                    } else {
                        Pair(it.internalRank * 2,it)
                    }
                }
                .sortedBy { it.first }
                .map { it.second.team }.toMutableList()
            newGroups.add(group)
        }
        if(oldGroups.size % 2 != 0) {
            val group = EmbeddedGroup()
            group.no = oldGroups.size/2 + 1
            group.name = "Group ${oldGroups.size/2 + 1}"
            group.teams = oldGameround.results
                .filter { oldGroups.last().teams.contains(it.team) }
                .sortedBy { it.internalRank }
                .map { it.team }
                .toMutableList()
            newGroups.add(group)
        }
        gr.groups.addAll(newGroups)
        return gr
    }

    fun generateFeiziRound4(template: GameroundTemplate, oldGameround: Gameround) : Gameround {
        val gr = genDataFromTemplate(template, oldGameround.no + 1)
        val newGroups = mutableListOf<EmbeddedGroup>()
        for(oldGroup in oldGameround.groups) {
            val group = EmbeddedGroup()
            group.no = oldGroup.no
            group.name = "Group ${oldGroup.no}"
            group.teams = oldGameround.results
                .filter { oldGroup.teams.contains(it.team) }
                .sortedBy { it.internalRank }
                .map { it.team }.toMutableList()
            newGroups.add(group)
        }
        if(oldGameround.groups.size % 2 != 0) {
            newGroups.removeLast()
        }
        gr.groups.addAll(newGroups)
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
        if(template.flattenGroupsOnImproperTeamNumber && teams.size%template.defaultGroupSize != 0) {
            log.debug("Default group size: ${template.defaultGroupSize} and teams size: ${teams.size}")
            flattenGroups(groups,template.defaultGroupSize)
            groups.forEach { group -> log.trace("Group ${group.no} with ${group.teams.size} teams. ${group.teams}") }
        }
        return groups
    }

    fun flattenGroups(groups: List<EmbeddedGroup>, defaultGroupSize: Int) {
        val targetGroupSize = (defaultGroupSize - 1) % groups.size
        log.debug("Start flattening with ${groups.size} groups to target-group-size: $targetGroupSize")
        flatten(groups, targetGroupSize)
        log.debug("Flattened: ${groups.map { it.teams.size }}")
    }


    private fun flatten(groups: List<EmbeddedGroup>, targetGroupSize: Int) {
        while(groups.size > 1 && groups.last().teams.size < targetGroupSize) {
            val team = groups[groups.size - 2].teams.removeLast()
            groups.last().teams.add(0, team)
        }
        if(groups.size > 1 && groups[groups.size - 2].teams.size < targetGroupSize) {
            val newList = ArrayList<EmbeddedGroup>(groups)
            newList.removeLast()
            flatten(newList, targetGroupSize)
        }
    }


    private fun genDataFromTemplate(template: GameroundTemplate, gameroundNumber: Int? = null) : Gameround {
        val gr = Gameround()
        if(gameroundNumber != null) gr.no = gameroundNumber else gr.no = template.gameroundNumber
        gr.name = template.gameroundName
        gr.desc = template.gameroundDesc
        gr.groupBinding = template.groupBinding
        gr.matchMakingConfigs = template.matchMakingConfig
        gr.orderConfig = template.orderConfig
        gr.matchNoOffset = template.matchNumberOffset
        return gr
    }
}