package at.birnbaua.tournament.data.service.feizi

import at.birnbaua.tournament.data.document.sub.EmbeddedTeam

class SimpleResult(var team: EmbeddedTeam) {
    var points: Long = 0
    var gamePoints: Long = 0
    var ownGamePoints: Long = 0
    var opponentGamePoints: Long = 0
    var wins: Long = 0
    var draws: Long = 0
    var defeats: Long = 0
    var internalRank: Long = 0
    var externalRank: Long = 0
    var internalCorrection: Long = 0
    var externalCorrection: Long = 0
    var hasCollidingRankInternal: Boolean = false
    var hasCollidingRankExternal: Boolean = false

    override fun toString(): String {
        return "Team ${team.no} | p: $points, gP: $gamePoints, iR: $internalRank, eR: $externalRank, iK: $hasCollidingRankInternal, eK: $hasCollidingRankExternal"
    }
}