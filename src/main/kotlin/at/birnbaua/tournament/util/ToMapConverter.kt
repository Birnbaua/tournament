package at.birnbaua.tournament.util

import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class ToMapConverter : Converter<LinkedHashMap<in String?, Any?>, Any> {
    private val log = LoggerFactory.getLogger(ToMapConverter::class.java)
    override fun convert(source: LinkedHashMap<in String?, Any?>): LinkedHashMap<in String?, Any?> {
        log.error("Convert map with nullable String keys to database")
        if(source.containsKey("null")) {
            log.trace("Map contained root node")
            source[null] = source["null"]
            source.remove("null")
        }
        return source
    }
}