package at.birnbaua.tournament.data.document.sub.gameround

class MatchMakingConfig {
    var rankStartingIndex: Int = 0
    var groupStartingIndex: Int = 0
    var fieldStartingIndex: Int = 0
    var startWithField: Int = 0
    var templates: MutableSet<MatchTemplate> = mutableSetOf()
}