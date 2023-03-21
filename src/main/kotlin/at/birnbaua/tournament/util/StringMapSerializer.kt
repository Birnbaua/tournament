package at.birnbaua.tournament.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.slf4j.LoggerFactory
import java.util.LinkedHashMap

class StringMapSerializer(t: Class<LinkedHashMap<*,*>>? = LinkedHashMap::class.java) : StdSerializer<LinkedHashMap<*,*>>(t) {

    private val log = LoggerFactory.getLogger(StringMapSerializer::class.java)

    @Suppress("UNCHECKED_CAST")
    override fun serialize(p0: LinkedHashMap<*,*>?, p1: JsonGenerator?, p2: SerializerProvider?) {
        log.trace("TEST SERIALIZER")
        if (p0 != null ) {
            if (p0.containsKey(null)) {
                p0 as LinkedHashMap<String?, Any?>
                val root = p0[null]
                p0.remove(null)
                p0["null"] = root
            }
            p1?.writeStartObject()
            p0.forEach {
                if(it.key == null) p1?.writeObjectField("null",it.value)
                else p1?.writeObjectField(it.key.toString(),it.value)
            }
            p1?.writeEndObject()
        }
    }
}