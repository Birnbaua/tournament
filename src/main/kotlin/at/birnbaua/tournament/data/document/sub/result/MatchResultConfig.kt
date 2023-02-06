package at.birnbaua.tournament.data.document.sub.result

class MatchResultConfig {
    data class ExplicitMatchResult(
        var sets: Pair<Long,Long> = Pair(0,0)
    ) {
        var pointsA: Long = 0
        var pointsB: Long = 0
    }

    var pointsPerSetWin: Long = 0
    var pointsPerSetDraw: Long = 0
    var pointsPerSetDefeat: Long = 0
    var pointsPerMatchWin: Long = 0
    var pointsPerMatchDraw: Long = 0
    var pointsPerMatchDefeat: Long = 0
    var explicitOutcome: ArrayList<ExplicitMatchResult> = arrayListOf()
}