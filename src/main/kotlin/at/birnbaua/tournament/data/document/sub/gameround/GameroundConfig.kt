package at.birnbaua.tournament.data.document.sub.gameround

class GameroundConfig {

    enum class OnMultiple{ STOP, REPEAT }

    var hasRestrictedScope: Boolean = false
    var isValidForBindingsEqual: Int? = null
    var isValidForBindingsGreaterThan: Int? = null
    var isValidForBindingsGreaterThanEqual: Int? = null
    var isValidForBindingsLessThan: Int? = null
    var isValidForBindingsLessThanEqual: Int? = null
    // if eg. the match making config is for 2 groups but the binding contains 4 groups, should the config be applied on the remaining ones or not
    var doOnMultiple: OnMultiple = OnMultiple.REPEAT
    var matchMakingConfig: MutableMap<String,MatchMakingConfig> = mutableMapOf()
}