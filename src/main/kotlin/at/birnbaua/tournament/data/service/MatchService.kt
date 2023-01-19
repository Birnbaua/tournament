package at.birnbaua.tournament.data.service

import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.repository.MatchRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MatchService {

    @Autowired
    private lateinit var repo: MatchRepository

    fun insert(entity: Match) : Mono<Match> { return repo.insert(entity) }
    fun findByTournamentAndNo(tournament: String, no: String) : Mono<Match> { return repo.findByTournamentAndNo(tournament, no)}
    fun findAllByTournament(tournament: String) : Flux<Match> { return repo.findAllByTournament(tournament) }
    fun deleteByTournamentAndNo(tournament: String, no: String) : Mono<Long> { return repo.deleteByTournamentAndNo(tournament, no) }
    fun deleteAllByTournament(tournament: String) : Mono<Long> { return repo.deleteAllByTournament(tournament) }
    fun deleteAllByTournamentAndNoIn(tournament: String, no: List<String> ) : Mono<Long> { return repo.deleteAllByTournamentAndNoIn(tournament, no) }

    fun updateTeamName(old: String, new: String) : Mono<Long> {
        
    }
}