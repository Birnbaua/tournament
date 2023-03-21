package at.birnbaua.tournament.converter

import at.birnbaua.tournament.util.StringMapSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperBean {

    @Bean
    fun objectMapper() : ObjectMapper {
        val mapper = ObjectMapper()
        val module = SimpleModule()
        module.addSerializer(StringMapSerializer())
        mapper.registerModule(module)
        return mapper
    }
}