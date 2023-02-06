package at.birnbaua.tournament.data.service

import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.sub.EmbeddedResult
import at.birnbaua.tournament.data.document.sub.EmbeddedTeam
import at.birnbaua.tournament.data.document.sub.result.*
import org.springframework.stereotype.Service

@Service
class ResultService {

    fun generateResults(gr: Gameround, matches: List<Match>) : List<EmbeddedResult> {

    }

    fun genResults(gameRound: GameRound) : MutableList<ResultEmbedded> {
        val map = mapOf<TeamEmbedded,ResultEmbedded>()
        gameRound.groups.flatMap { it.matches }.flatMap { listOf(it.teamA,it.teamB) }.distinct().map { it }.map { Pair(it,ResultEmbedded(it)) }.forEach { map.plus(it) }
        genResults(gameRound.groups.flatMap { it.matches },map,gameRound.config.matchResultConfig)
        genRanksAndCollisions(gameRound,map)
        return if(gameRound.config.resultOrdering.reverseOrdering) map.values.toMutableList().asReversed() else map.values.toMutableList()
    }

    private fun genRanksAndCollisions(gameRound: GameRound, map: Map<TeamEmbedded,ResultEmbedded>) {
        val internal = gameRound.groups.sortedBy { it.no }.map { g -> g.teams.mapNotNull { map[it] } }.toMutableList()
        val external = internal.toMutableList()
        val groupMatchesInternal = gameRound.groups.map { it.matches.toMutableList() }.toMutableList()
        val groupMatchesExternal = groupMatchesInternal.toMutableList()
        gameRound.config.resultOrdering.internalOrdering
            .sortedBy { it.no }
            .mapIndexed { index, ordering -> ordering.no = index; ordering }
            .forEach { ordering -> doOrdering(groupMatchesInternal,internal,ordering,gameRound.config.resultOrdering.isRelative,true) }
    }

    private fun doOrdering(groupMatches: MutableList<MutableList<Match>>, groupResult: MutableList<List<ResultEmbedded>>, ordering: ResultOrderingConfig.Ordering, isRelative: Boolean, isInternal: Boolean) {
        val i = when(ordering.scope) {
            GeneratingScope.FIRST -> 0
            GeneratingScope.SECOND -> 1
            GeneratingScope.THIRD -> 2
            GeneratingScope.FOURTH -> 3
            GeneratingScope.FIFTH -> 4
            GeneratingScope.NEXT -> 0
            GeneratingScope.ALL -> -1
            GeneratingScope.LAST -> groupResult.size - 1
        }
        groupResult[i].sortedWith(getComparator(groupMatches[i], ordering, isInternal)).forEachIndexed { index, resultEmbedded ->
            if(resultEmbedded.hasCollidingRankInternal) {
                if(index != 0) resultEmbedded.internalRank = groupResult[i][index].internalRank else resultEmbedded.internalRank = 0 + ordering.offset
            } else {
                resultEmbedded.internalRank = index.toLong() + ordering.offset
            }
        }
        if(isRelative) { groupMatches.removeAt(i) }
    }

    private fun getComparator(matches: List<Match>, config: ResultOrderingConfig.Ordering, isInternal: Boolean = true) : Comparator<ResultEmbedded> {
        return Comparator { o1, o2 ->
            var c = 0L
            var i = 0
            val size = config.orderProperties.size
            val isEmpty = config.orderProperties.isNotEmpty()
            val ordering = config.orderProperties
            while(isEmpty && size > i) {
                when(ordering[i]) {
                    OrderProperty.POINTS -> c = o1.points - o2.points
                    OrderProperty.GAME_POINTS -> c = o1.gamePoints - o2.gamePoints
                    OrderProperty.OWN_GAME_POINTS -> c = o1.ownGamePoints - o2.ownGamePoints
                    OrderProperty.OPPONENT_GAME_POINTS -> c = o1.opponentGamePoints - o2.opponentGamePoints
                    OrderProperty.DIRECT_MATCHES -> c = compareDirectMatches(matches,o1,o2,false)
                    OrderProperty.DIRECT_MATCHES_WITH_HOME_GAMES -> compareDirectMatches(matches,o1,o2,true)
                    OrderProperty.TEAM_NO -> o1.team.no - o2.team.no
                    OrderProperty.EXTERNAL_RANK -> o2.externalRank - o1.externalRank
                    OrderProperty.INTERNAL_RANK -> o2.internalRank - o1.internalRank
                    OrderProperty.INTERNAL_CORRECTION -> o1.internalCorrection - o2.internalCorrection
                    OrderProperty.EXTERNAL_CORRECTION -> o1.externalCorrection - o2.externalCorrection
                    OrderProperty.WINS -> o1.wins - o2.wins
                    OrderProperty.DRAWS -> o1.draws - o2.draws
                    OrderProperty.DEFEATS -> o2.defeats - o1.defeats
                }
                if(c != 0L) break
                i += 1
            }
            if(c < 0L) {
                -1
            } else if(c > 0L) {
                1
            } else {
                if(isInternal) {
                    o1.hasCollidingRankInternal = true
                    o2.hasCollidingRankInternal = true
                } else {
                    o1.hasCollidingRankExternal = true
                    o2.hasCollidingRankExternal = true
                }
                0
            }
        }
    }

    private fun genResults(matches: List<Match>, map: Map<TeamEmbedded,ResultEmbedded>, config: MatchResultConfig) {
        matches.forEach { m ->
            map[m.teamA]!!.ownGamePoints += getGamePoints(m, m.teamA!!)
            map[m.teamA]!!.opponentGamePoints  += getGamePoints(m, m.teamB!!)
            map[m.teamB]!!.ownGamePoints += getGamePoints(m, m.teamB!!)
            map[m.teamB]!!.opponentGamePoints  += getGamePoints(m, m.teamA!!)
            map[m.teamA]!!.points += getPoints(m,m.teamA!!,config)
            map[m.teamB]!!.points += getPoints(m,m.teamB!!,config)
            val matchResult = getMatchResult(m)
            map[m.teamA]!!.wins += if(matchResult.first > matchResult.second) 1L else 0L
            map[m.teamA]!!.draws += if(matchResult.first == matchResult.second && (matchResult.first != 0L || getDrawSets(m) != 0L)) 1L else 0L
            map[m.teamA]!!.defeats += if(matchResult.first < matchResult.second) 1L else 0L
            map[m.teamB]!!.wins += if(matchResult.first < matchResult.second) 1L else 0L
            map[m.teamB]!!.draws += if(matchResult.first == matchResult.second && (matchResult.first != 0L || getDrawSets(m) != 0L)) 1L else 0L
            map[m.teamB]!!.defeats += if(matchResult.first > matchResult.second) 1L else 0L
        }
    }

    private fun getPoints(match: Match, team: EmbeddedTeam, config: MatchResultConfig) : Long {
        var points = 0L
        val matchResult = getMatchResult(match)
        if(config.explicitOutcome.size == 0) {
            val visaVersa = Pair(matchResult.second,matchResult.first)
            if(config.explicitOutcome.map { it.sets }.contains(matchResult)) {
                return if(match.teamA == team) {
                    config.explicitOutcome.first { it.sets == matchResult }.pointsA
                } else {
                    config.explicitOutcome.first { it.sets == matchResult }.pointsB
                }
            } else if(config.explicitOutcome.map { it.sets }.contains(visaVersa)) {
                return if(match.teamA == team) {
                    config.explicitOutcome.first { it.sets == visaVersa }.pointsB
                } else {
                    config.explicitOutcome.first { it.sets == visaVersa }.pointsA
                }
            }
        }
        if(match.teamA == team) {
            points += matchResult.first * config.pointsPerSetWin
            points += matchResult.second * config.pointsPerSetDefeat
        }
        if(match.teamB == team) {
            points += matchResult.second * config.pointsPerSetWin
            points += matchResult.first * config.pointsPerSetDefeat
        }
        points += config.pointsPerSetDraw * getDrawSets(match)
        if(matchResult.first > matchResult.second) {
            if(match.teamA == team) points += config.pointsPerMatchWin
            if(match.teamB == team) points += config.pointsPerMatchDefeat
        } else if(matchResult.first < matchResult.second) {
            if(match.teamA == team) points += config.pointsPerMatchDefeat
            if(match.teamB == team) points += config.pointsPerMatchWin
        } else if(matchResult.first != 0L) {
            points += config.pointsPerMatchDraw
        }
        return points
    }

    private fun getDrawSets(match: Match) : Long {
        return match.sets.sumOf { set ->
            if(set.pointsA == set.pointsB) 1L else 0L
        }
    }

    private fun getMatchResult(match: Match) : Pair<Long,Long> {
        return Pair(
            match.sets.sumOf {
                if (it.pointsA > it.pointsB) 1L
                else 0L
            },
            match.sets.sumOf {
                if (it.pointsB > it.pointsA) 1L
                else 0L
            }
        )
    }

    private fun getGamePoints(match: Match, team: EmbeddedTeam) : Long {
        return match.sets.sumOf {
            if (match.teamA == team)
                it.pointsA
            else
                it.pointsB
        }
    }

    private fun getDirectMatches(matches: List<Match>, teamA: EmbeddedTeam, teamB: EmbeddedTeam) : List<Match> {
        return matches.filter {
            (it.teamA == teamA && it.teamB == teamB) || (it.teamA == teamB && it.teamB == teamA)
        }
    }

    private fun compareDirectMatches(matches: List<Match>, o1: EmbeddedTeam, o2: EmbeddedTeam, hasHomeGames: Boolean) : Long {
        val directMatches = getDirectMatches(matches, o1, o2)
        val results = directMatches.map { getMatchResult(it) }
        val winA = results.sumOf { if (it.first > it.second) 1L else 0L }
        val winB = results.sumOf { if (it.first < it.second) 1L else 0L }
        //o1 always has the host of a match if home games is enabled
        return if(winA > winB) {
            1
        } else if(winA < winB) {
            -1
        } else {
            if(hasHomeGames) {
                var homeGamePointsO1: Long = matches.filter { it.teamA == o1 }.flatMap { it.sets }.sumOf { it.pointsA }
                homeGamePointsO1 += matches.filter { it.teamB == o1 }.flatMap { it.sets }.sumOf { it.pointsB }
                var homeGamePointsO2: Long = matches.filter { it.teamA == o2 }.flatMap { it.sets }.sumOf { it.pointsA }
                homeGamePointsO2 += matches.filter { it.teamB == o2 }.flatMap { it.sets }.sumOf { it.pointsB }
                homeGamePointsO1 - homeGamePointsO2
            } else {
                0
            }
        }
    }
}