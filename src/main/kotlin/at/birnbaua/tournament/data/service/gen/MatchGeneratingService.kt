package at.birnbaua.tournament.data.service.gen

import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.service.feizi.SimpleMatchGeneratingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MatchGeneratingService {

    @Autowired private lateinit var smgs: SimpleMatchGeneratingService

    fun generate(gameround: Gameround, allFields: List<Field>, startTime: LocalDateTime = LocalDateTime.now(), offset: Int = gameround.matchNoOffset, feizi: Boolean = true) : List<Match> {
        return smgs.generateMatchesFeizi(gameround, allFields, startTime, offset)
    }

}