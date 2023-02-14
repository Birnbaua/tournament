package at.birnbaua.tournament.data.document.sub.gameround

class MatchMakingConfig() {

    enum class OnMultiple{ STOP, REPEAT }

    var rankStartingIndex: Int = 0
    var groupStartingIndex: Int = 0
    var fieldStartingIndex: Int = 0
    var startWithField: Int = 0
    var templates: MutableList<MatchTemplate> = mutableListOf()
    // if eg. the match making config is for 2 groups but the binding contains 4 groups, should the config be applied on the remaining ones or not
    var doOnMultiple: OnMultiple = OnMultiple.REPEAT

    constructor(rankStartingIndex: Int, groupStartingIndex: Int, fieldStartingIndex: Int, startWithField: Int, templates: MutableList<MatchTemplate>, doOnMultiple: OnMultiple = OnMultiple.REPEAT) : this() {
        this.rankStartingIndex = rankStartingIndex
        this.groupStartingIndex = groupStartingIndex
        this.fieldStartingIndex = fieldStartingIndex
        this.startWithField = startWithField
        this.templates = templates
    }
}