package at.birnbaua.tournament.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.slf4j.LoggerFactory
import java.util.HashMap

class TestTreeSerializer (t: Class<String>?) : StdSerializer<String>(t) {

    private val log = LoggerFactory.getLogger(StringMapSerializer::class.java)

    constructor() : this(null)
    override fun serialize(p0: String?, p1: JsonGenerator?, p2: SerializerProvider?) {
        log.info("TEST SERIALIZER")
        p1?.writeNumber(p0?.length!!)
    }
}