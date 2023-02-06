package at.birnbaua.tournament.data.service

import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Gameround
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.Team
import org.springframework.stereotype.Service

@Service
class MatchGeneratingService {

    fun generate(gameround: Gameround, fields: Map<String, Field>, teams: Map<String, Team>) : List<Match> {
        val matches: MutableList<Match> = mutableListOf()
        gameround.groupBinding.getAllNodes()
            .forEach {
                it.
            }

    }



}