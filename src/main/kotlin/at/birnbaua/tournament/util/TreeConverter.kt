package at.birnbaua.tournament.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter


class TreeConverter : Converter<Tree<String?,Any?>, Any> {

    private val log = LoggerFactory.getLogger(MapToConverter::class.java)
    private val mapper = ObjectMapper()
    override fun convert(source: Tree<String?,Any?>): Any {
        if(source.entries.containsKey(null)) {
            val value = source.entries[null]!!
            source.entries.remove(null)
            source.entries["root"] = value
        }
        println(source.entries)
        return source
    }
}