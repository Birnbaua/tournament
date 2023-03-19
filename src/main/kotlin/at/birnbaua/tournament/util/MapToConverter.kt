package at.birnbaua.tournament.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import java.util.LinkedHashMap

class MapToConverter : Converter<LinkedHashMap<in String?,*>, String> {

    private val LOGGER = LoggerFactory.getLogger(MapToConverter::class.java)
    private val mapper = ObjectMapper()

    init {
        val module = SimpleModule()
        module.addSerializer(StringMapSerializer())
        mapper.registerModule(module)
    }
    override fun convert(source: LinkedHashMap<in String?, *>): String {
        LOGGER.info("Map to converter converted")
        return mapper.writeValueAsString(source)
    }
}