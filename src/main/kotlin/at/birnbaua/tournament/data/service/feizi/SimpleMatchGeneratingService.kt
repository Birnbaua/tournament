package at.birnbaua.tournament.data.service.feizi

import at.birnbaua.tournament.data.document.CompositeId
import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.sub.EmbeddedGroup
import at.birnbaua.tournament.data.document.sub.EmbeddedSet
import at.birnbaua.tournament.data.document.sub.EmbeddedTeam
import at.birnbaua.tournament.data.document.sub.gameround.MatchMakingConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SimpleMatchGeneratingService {

    val log: Logger = LoggerFactory.getLogger(SimpleMatchGeneratingService::class.java)

    fun generateMatchesFeizi(gameround: Gameround, allFields: List<Field>, startTime: LocalDateTime = LocalDateTime.now()) : List<Match> {
        val matches = mutableListOf<Match>()
        val binding = gameround.groupBinding
        val groups = gameround.groups.associateBy { it.no }
        val configs = gameround.matchMakingConfig
        val fields = allFields.sortedBy { it.no }
        var matchNo = gameround.matchNoOffset
        log.debug("Starting match generation with: ${fields.size} fields and ${gameround.groups.size} groups")
        for(config in configs) {
            val nodes = when (config.value.doOnSubNodes) {
                MatchMakingConfig.OnSubNodes.DONT -> listOf()
                MatchMakingConfig.OnSubNodes.FIRST -> listOf(binding.getFirstLeafNode(config.key))
                MatchMakingConfig.OnSubNodes.ALL -> binding.getAllLeafNodes(config.key)
            }
            if (isInternalMatchMaking(config.value)) {
                log.debug("Do match generating with following groups...")
                val allGroups = nodes
                    .onEach { log.debug("${it.values}") }
                    .flatMap { it.values }.mapNotNull { groups[it] }.sortedBy { it.no }
                var time = startTime
                val fieldIndexOffset = config.value.fieldStartingIndex
                log.debug("Field index offset: $fieldIndexOffset")
                log.debug("Group index offset: ${config.value.groupStartingIndex}")
                log.debug("Rank index offset: ${config.value.rankStartingIndex}")
                log.debug("Starting time: $time")
                config.value.templates.forEachIndexed { roundIndex, template ->
                    allGroups.forEachIndexed { index, group ->
                        val match = Match()
                        match.startAt = time
                        match.no = matchNo++
                        match.teamA = getTeamIfPresentOrNull(group, template.teamA?.rank?.toInt())
                        match.teamB = getTeamIfPresentOrNull(group, template.teamB?.rank?.toInt())
                        match.referee = getTeamIfPresentOrNull(group, template.referee?.rank?.toInt())
                        val fieldNo: Int = if (template.field != null) template.field!! else roundIndex
                        match.field = fields[(fieldIndexOffset + index + fieldNo) % fields.size].toEmbedded()
                        match.sets.addAll((0 until template.noOfSets).map { EmbeddedSet(it) })
                        matches.add(match)
                    }
                    time = time.plusMinutes(template.timeToNextMatchStartInMinutes().toLong())
                }
            }
        }
        matches.forEach {
            it.id = CompositeId(it.tournament,it.no)
            it.tournament = gameround.tournament
            it.gameround = gameround.no
        }
        return matches
    }

    private fun getTeamIfPresentOrNull(group: EmbeddedGroup, rank: Int? = null) : EmbeddedTeam? {
        log.trace("Check if team with rank: $rank exists in group: ${group.no}, ${group.name}")
        return if(rank != null && isTeamPresentInGroup(group, rank)) {
            log.trace("Team with rank: $rank exists in group: ${group.no}, ${group.name}")
            group.teams[rank]
        } else {
            log.trace("Team with rank: $rank does not exist in group: ${group.no}, ${group.name} with size: ${group.teams.size}")
            null
        }
    }

    private fun isTeamPresentInGroup(group: EmbeddedGroup, rank: Int) : Boolean {
        log.trace("Team with rank: $rank is ${if(group.teams.size > rank) "within " else "out of "} bounds")
        return group.teams.size > rank
    }

    private fun isInternalMatchMaking(config: MatchMakingConfig) : Boolean {
        var isInternal = true
        log.trace("Check if match making config is for group internal matchmaking")
        for(template in config.templates) {
            if(
                (template.teamA?.group != null && template.teamA?.group?.toInt() != config.groupStartingIndex) ||
                (template.teamB?.group != null && template.teamB?.group?.toInt() != config.groupStartingIndex) ||
                (template.referee?.group != null && template.referee?.group?.toInt() != config.groupStartingIndex)
            ) {
                isInternal = false
                break
            }
        }
        return isInternal
    }
}