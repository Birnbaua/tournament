package at.birnbaua.tournament.data.document.sub.gameround

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(value = JsonInclude.Include.NON_NULL)
class MatchTemplate {
    data class TeamTemplate(var group: String? = null, var rank: Long? = null) { var isProvided: Boolean = false }

    //used for ordering
    var no: Long? = null
    var noOfSets: Int = 1
    var timePerSetInMinutes: BigDecimal? = null
    var breakBetweenSetsInMinutes: BigDecimal = BigDecimal.ONE
    var breakUntilNextMatchInMinutes: BigDecimal = BigDecimal.valueOf(5)
    var teamA: TeamTemplate? = null
    var teamB: TeamTemplate? = null
    var referee: TeamTemplate? = null
    //if null random field selected
    var field: String? = null
}