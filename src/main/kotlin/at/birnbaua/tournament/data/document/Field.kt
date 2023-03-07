package at.birnbaua.tournament.data.document

import at.birnbaua.tournament.data.document.sub.EmbeddedField
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "field")
@CompoundIndexes(value = [CompoundIndex(name = "field_index", def = "{'tournament': 1, 'no': 1}", unique = true)])
@Suppress("unused")
class Field {

    enum class FieldType{ SAND,GRASS,HARD,UNKNOWN }

    @Id
    @Field(name = "_id")
    var id: ObjectId? = null

    @Field(name = "tournament")
    var tournament: String? = null

    @Field(name = "no")
    var no: Int = 0

    @Field(name = "name")
    var name: String? = null

    @Field(name = "desc", write = Field.Write.NON_NULL)
    var desc: String? = null

    @Field(name = "field_type")
    var type: FieldType? = null

    @Field(name = "audit")
    var audit: AuditEntry = AuditEntry()

    fun toEmbedded() : EmbeddedField {
        return EmbeddedField(this.no, this.name)
    }
}