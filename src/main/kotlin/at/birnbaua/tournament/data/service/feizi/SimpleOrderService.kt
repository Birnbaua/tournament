package at.birnbaua.tournament.data.service.feizi

import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.sub.EmbeddedGroup
import at.birnbaua.tournament.data.document.sub.EmbeddedTeam
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class SimpleOrderService {

    data class MatchResult(var teamA: Int = 0, var teamB: Int = 0, var draw: Int = 0)

    fun genResults(matches: List<Match>, results: Map<EmbeddedTeam,SimpleResult>, groups: List<EmbeddedGroup>, orderConfig: SimpleOrderConfig, externalOffset: Int) : MutableList<SimpleResult> {
        val matchResults = genMatchResults(matches, orderConfig)
        genRanksAndCollisions(matchResults,groups,orderConfig, externalOffset = externalOffset)
        matchResults
            .forEach { result ->
                if(results.containsKey(result.team)){
                    result.internalCorrection = results[result.team]!!.internalCorrection
                    result.externalCorrection = results[result.team]!!.externalCorrection
                }
            }
        return matchResults
    }

    private fun genRanksAndCollisions(results: List<SimpleResult>, groups: List<EmbeddedGroup>,  orderConfig: SimpleOrderConfig, externalOffset: Int = 0) {
        groups.forEach { group ->
            val sortedInternal = results
                .filter { result -> group.teams.contains(result.team) }
                .sortedWith(getComparator(orderConfig.groupInternalOrdering, isInternal = true))
            sortedInternal
                .forEachIndexed { index, result ->
                    if(result.hasCollidingRankInternal) {
                        result.internalRank = getRankOfCollision(sortedInternal, index, orderConfig, true)
                    } else {
                        result.internalRank = index.toLong()
                    }
                }
        }
        results
            .sortedWith(getComparator(orderConfig.groupExternalOrdering, isInternal = false))
            .forEachIndexed { index, result ->
                if(result.hasCollidingRankExternal) {

                } else {
                    result.externalRank = index.toLong()
                }
            }
    }

    private fun getRankOfCollision(results: List<SimpleResult>, index: Int, orderConfig: SimpleOrderConfig, isInternal: Boolean) : Long {
        return if(index != 0) {
            val orderProperties = if(isInternal) orderConfig.groupInternalOrdering else orderConfig.groupExternalOrdering
            return if(getComparator(orderProperties, isInternal = true).compare(results[index-1],results[index]) == 0) {
                return if(isInternal) results[index-1].internalRank else results[index-1].externalRank
            } else {
                index.toLong()
            }
        } else {
            0
        }
    }

    private fun genMatchResults(matches: List<Match>, orderConfig: SimpleOrderConfig) : MutableList<SimpleResult> {
        val results = mutableListOf<SimpleResult>()
        val teams = matches.flatMap { listOf(it.teamA,it.teamB) }.filterNotNull().distinct()
        teams.forEach {team ->
            val result = SimpleResult(team)
            val matchesOfTeam = matches.filter { m -> m.teamA == team || m.teamB == team }
            result.points = matchesOfTeam.sumOf { getPoints(it,team, orderConfig) }
            result.ownGamePoints = matchesOfTeam.sumOf { getOwnGamePoints(it,team) }
            result.opponentGamePoints = matchesOfTeam.sumOf { getOpponentGamePoints(it,team) }
            result.wins = matchesOfTeam.sumOf { if(isWin(it,team)) 1L else 0L }
            result.draws = matchesOfTeam.sumOf { if(isDraw(it,team)) 1L else 0L }
            result.defeats = matchesOfTeam.sumOf { if(isDefeat(it,team)) 1L else 0L }
            results.add(result)
        }
        return results
    }

    private fun getPoints(match: Match, team: EmbeddedTeam, orderConfig: SimpleOrderConfig) : Long {
        var points = 0L
        val matchResult = getMatchResult(match)
        if(match.teamA == team) {
            points += matchResult.teamA * orderConfig.pointsPerSetWon
        } else if(match.teamB == team) {
            points += matchResult.teamB * orderConfig.pointsPerSetWon
        }
        points += matchResult.teamA * orderConfig.pointsPerSetDraw
        return points
    }

    private fun getOwnGamePoints(match: Match, team: EmbeddedTeam) : Long {
        return if(match.teamA == team) {
            match.sets.sumOf { it.pointsA }
        } else if(match.teamB == team) {
            match.sets.sumOf { it.pointsB }
        } else {
            0L
        }
    }

    private fun getOpponentGamePoints(match: Match, team: EmbeddedTeam) : Long {
        return if(match.teamA == team) {
            match.sets.sumOf { it.pointsB }
        } else if(match.teamB == team) {
            match.sets.sumOf { it.pointsA }
        } else {
            0L
        }
    }

    private fun isWin(match: Match, team: EmbeddedTeam) : Boolean {
        val matchResult = getMatchResult(match)
        return if(match.teamA == team) {
            matchResult.teamA > matchResult.teamB
        } else if(match.teamB == team) {
            matchResult.teamB > matchResult.teamA
        } else {
            false
        }
    }

    private fun isDraw(match: Match, team: EmbeddedTeam) : Boolean {
        val matchResult = getMatchResult(match)
        return (match.teamA == team || match.teamB == team) && matchResult.teamA == matchResult.teamB
    }

    private fun isDefeat(match: Match, team: EmbeddedTeam) : Boolean {
        val matchResult = getMatchResult(match)
        return if(match.teamA == team) {
            matchResult.teamA > matchResult.teamB
        } else if(match.teamB == team) {
            matchResult.teamB < matchResult.teamA
        } else {
            false
        }
    }

    private fun getMatchResult(match: Match) : MatchResult {
        return MatchResult(
            match.sets.sumOf {
                if (it.pointsA > it.pointsB) 1L
                else 0L
            }.toInt(),
            match.sets.sumOf {
                if (it.pointsB > it.pointsA) 1L
                else 0L
            }.toInt(),
            match.sets.sumOf {
                if (it.pointsA == it.pointsB) 1L
                else 0L
            }.toInt()
        )
    }

    private fun getComparator(ordering: List<OrderProperty>, isInternal: Boolean = false) : Comparator<SimpleResult> {
        return Comparator { o1, o2 ->
            var c = 0L
            var i = 0
            val size = ordering.size
            val isNotEmpty = ordering.isNotEmpty()
            val randomSeed1 = System.nanoTime()
            while(isNotEmpty && size > i) {
                when(ordering[i]) {
                    OrderProperty.POINTS -> c = o1.points - o2.points
                    OrderProperty.GAME_POINTS -> c = o1.gamePoints - o2.gamePoints
                    OrderProperty.OWN_GAME_POINTS -> c = o1.ownGamePoints - o2.ownGamePoints
                    OrderProperty.OPPONENT_GAME_POINTS -> c = o1.opponentGamePoints - o2.opponentGamePoints
                    OrderProperty.TEAM_NO -> c = o1.team.no.toLong() - o2.team.no.toLong()
                    OrderProperty.EXTERNAL_RANK -> c = o2.externalRank - o1.externalRank
                    OrderProperty.INTERNAL_RANK -> c = o2.internalRank - o1.internalRank
                    OrderProperty.INTERNAL_CORRECTION -> c = o1.internalCorrection - o2.internalCorrection
                    OrderProperty.EXTERNAL_CORRECTION -> c = o1.externalCorrection - o2.externalCorrection
                    OrderProperty.WINS -> c = o1.wins - o2.wins
                    OrderProperty.DRAWS -> c = o1.draws - o2.draws
                    OrderProperty.DEFEATS -> c = o2.defeats - o1.defeats
                    OrderProperty.DIRECT_MATCHES -> TODO("Not implemented yet")
                    OrderProperty.DIRECT_MATCHES_WITH_HOME_GAMES -> TODO("Not implemented yet")
                    OrderProperty.RANDOM -> c =  (Random(randomSeed1).nextInt() - Random(System.nanoTime()+1000).nextInt()).toLong()
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
}