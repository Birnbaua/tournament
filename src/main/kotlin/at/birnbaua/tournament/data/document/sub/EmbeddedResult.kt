package at.birnbaua.tournament.data.document.sub

class EmbeddedResult{
    var points: Long = 0
    var gamePoints: Long = 0
    var ownGamePoints: Long = 0
    var opponentGamePoints: Long = 0
    var wins: Long = 0
    var draws: Long = 0
    var defeats: Long = 0
    var internalRank: Long = 0
    var externalRank: MutableMap<String,Long> = mutableMapOf()
    var internalCorrection: Long = 0
    var externalCorrection: MutableMap<String,Long> = mutableMapOf()
    var hasCollidingRankInternal: Boolean = false
    var hasCollidingRankExternal: MutableMap<String,Boolean> = mutableMapOf()
}