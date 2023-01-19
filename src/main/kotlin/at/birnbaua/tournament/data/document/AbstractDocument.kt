package at.birnbaua.tournament.data.document

import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

abstract class AbstractDocument {

    @Field(name = "created_by", write = Field.Write.NON_NULL)
    var createdBy: String? = null

    @Field("updated_by", write = Field.Write.NON_NULL)
    var updatedBy: String? = null

    @Field("created_at", write = Field.Write.ALWAYS)
    var createdAt: LocalDateTime? = LocalDateTime.now()

    @Field("updated_at", write = Field.Write.ALWAYS)
    var updatedAt: LocalDateTime? = LocalDateTime.now()
}