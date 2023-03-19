package at.birnbaua.tournament.converter

import at.birnbaua.tournament.util.MapToConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions

@Configuration
class DbConverterBean {

    @Bean
    fun customConversions() : MongoCustomConversions {
        val converters = mutableListOf<Converter<*,*>>()
        converters.add(MapToConverter())
        return MongoCustomConversions(converters)
    }
}