package at.birnbaua.tournament.data.document.sub.gameround

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Suppress("unused")
class MatchTemplate {
    data class TeamTemplate(var group: Long? = null, var rank: Long? = null)

    //used for ordering
    var no: Int = 0
    var noOfSets: Int = 3
    var timePerSetInMinutes: BigDecimal? = BigDecimal.valueOf(20)
    var breakBetweenSetsInMinutes: BigDecimal = BigDecimal.ZERO
    var breakUntilNextMatchInMinutes: BigDecimal = BigDecimal.valueOf(5)
    var teamA: TeamTemplate? = null
    var teamB: TeamTemplate? = null
    var referee: TeamTemplate? = null
    //if null random field selected
    var field: Int? = null

    constructor(no: Int, noOfSets: Int, timePerSetInMinutes: BigDecimal, breakBetweenSetsInMinutes: BigDecimal, breakUntilNextMatchInMinutes: BigDecimal,
        teamA: TeamTemplate, teamB: TeamTemplate, referee: TeamTemplate, field: Int) {
        this.no = no
        this.noOfSets = noOfSets
        this.timePerSetInMinutes = timePerSetInMinutes
        this.breakBetweenSetsInMinutes = breakBetweenSetsInMinutes
        this.breakUntilNextMatchInMinutes = breakUntilNextMatchInMinutes
        this.teamA = teamA
        this.teamB = teamB
        this.referee = referee
        this.field = field
    }

    constructor(no: Int, noOfSets: Int, timePerSetInMinutes: Int, breakBetweenSetsInMinutes: Int, breakUntilNextMatchInMinutes: Int,
                teamA: TeamTemplate, teamB: TeamTemplate, referee: TeamTemplate, field: Int? = null) {
        this.no = no
        this.noOfSets = noOfSets
        this.timePerSetInMinutes = BigDecimal.valueOf(timePerSetInMinutes.toLong())
        this.breakBetweenSetsInMinutes = BigDecimal.valueOf(breakBetweenSetsInMinutes.toLong())
        this.breakUntilNextMatchInMinutes = BigDecimal.valueOf(breakUntilNextMatchInMinutes.toLong())
        this.teamA = teamA
        this.teamB = teamB
        this.referee = referee
        this.field = field
    }
}