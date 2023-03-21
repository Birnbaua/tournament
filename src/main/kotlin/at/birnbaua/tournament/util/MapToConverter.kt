package at.birnbaua.tournament.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter


@WritingConverter
class MapToConverter : Converter<LinkedHashMap<in String?, *>, Any> {

    private val log = LoggerFactory.getLogger(MapToConverter::class.java)
    private val mapper = ObjectMapper()

    init {
        val module = SimpleModule()
        module.addSerializer(StringMapSerializer())
        mapper.registerModule(module)
    }
    override fun convert(source: LinkedHashMap<in String?, *>): Any {
        log.info("Map to converter converted")
        val typeRef: TypeReference<LinkedHashMap<String?, *>> = object : TypeReference<LinkedHashMap<String?, *>>() {}
        val value = mapper.writeValueAsString(source).replace("\\\"","\"")
        return mapper.readValue(value,typeRef)
    }
}