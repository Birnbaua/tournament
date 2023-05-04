package at.birnbaua.tournament.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter


@WritingConverter
class MapToConverter : Converter<LinkedHashMap<in String?, Any?>, Any> {

    private val log = LoggerFactory.getLogger(MapToConverter::class.java)
    private val mapper = ObjectMapper()

    init {
        val module = SimpleModule()
        module.addSerializer(StringMapSerializer())
        mapper.registerModule(module)
    }
    override fun convert(source: LinkedHashMap<in String?, Any?>): Any {
        val typeRef: TypeReference<LinkedHashMap<String?, Any?>> = object : TypeReference<LinkedHashMap<String?, Any?>>() {}
        log.trace("Convert map with nullable String keys from database")
        val value = mapper.writeValueAsString(source).replace("\\\"","\"")
        /*
        if(source.containsKey(null).not()) {
            val newEntries = source.map { Pair(it.key.toString(),it.value) }
            source.clear()
            newEntries.forEach { source[it.first] = it.second}
            return source
        }
         */
        return mapper.readValue(value,typeRef)
    }
}