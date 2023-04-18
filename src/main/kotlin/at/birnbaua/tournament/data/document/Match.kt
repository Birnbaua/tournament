package at.birnbaua.tournament.data.document

import at.birnbaua.tournament.data.document.sub.EmbeddedField
import at.birnbaua.tournament.data.document.sub.EmbeddedSet
import at.birnbaua.tournament.data.document.sub.EmbeddedTeam
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(value = "match")
@CompoundIndexes(value = [CompoundIndex(name = "match_index", def = "{'tournament': 1, 'no': 1}", unique = true)])
class Match() {

    @Id
    @Field(name = "_id")
    var id: CompositeId? = null

    @Field(name = "tournament")
    var tournament: String? = null

    @Field(name = "no")
    var no: Int = 0

    @Field(name = "gameround")
    var gameround: Int? = null

    @Field(name = "start_at")
    var startAt: LocalDateTime? = null

    @Field(name = "end_at")
    var endAt: LocalDateTime? = null

    @Field(name = "field")
    var field: EmbeddedField? = null

    @Field(name = "team_a")
    var teamA: EmbeddedTeam? = null

    @Field(name = "team_b")
    var teamB: EmbeddedTeam? = null

    @Field(name = "referee")
    var referee: EmbeddedTeam? = null

    @Field(name = "sets")
    var sets: MutableList<EmbeddedSet> = mutableListOf()

    @Field(name = "audit")
    var audit: AuditEntry = AuditEntry()

    override fun toString(): String {
        return "No: $no, Start: ${startAt?.hour}:${startAt?.minute}, Field: ${field?.no}, ${teamA?.name} vs. ${teamB?.name} with ${referee?.name}, NoOfSets: ${sets.size}"
    }
}