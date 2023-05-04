package at.birnbaua.tournament

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@EnableMongoAuditing
@SpringBootApplication
class TournamentApplication

fun main(args: Array<String>) {
    runApplication<TournamentApplication>(*args)
}
