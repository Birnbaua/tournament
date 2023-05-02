package at.birnbaua.tournament.data.service

import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.repository.MatchRepository
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MatchService {

    @Autowired
    private lateinit var repo: MatchRepository

    private val log = LoggerFactory.getLogger(MatchService::class.java)

    fun insert(entity: Match) : Mono<Match> { return repo.insert(entity) }
    fun save(publisher: Publisher<Match>) : Flux<Match> { return repo.saveAll(publisher) }
    fun saveAll(iterable: Iterable<Match>) : Flux<Match> { return repo.saveAll(iterable) }
    fun findByTournamentAndNo(tournament: String, no: Int) : Mono<Match> { return repo.findByTournamentAndNo(tournament, no)}
    fun findAllByTournament(tournament: String) : Flux<Match> { return repo.findAllByTournament(tournament) }

    fun findAllByGameround(tournament: String, gameround: Int) : Flux<Match> { return repo.findAllByTournamentAndGameround(tournament,gameround) }
    fun deleteByTournamentAndNo(tournament: String, no: Int) : Mono<Long> { return repo.deleteByTournamentAndNo(tournament, no) }
    fun deleteAllByTournament(tournament: String) : Mono<Long> { return repo.deleteAllByTournament(tournament) }
    fun deleteAll(): Mono<Void> { return repo.deleteAll() }
    fun deleteAllByTournamentAndNoIn(tournament: String, no: List<Int> ) : Mono<Long> { return repo.deleteAllByTournamentAndNoIn(tournament, no) }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun updateTeamNameByTournamentAndNo(tournament: String?, no: Int?, new: String?) : Mono<Long> {
        log.trace("Update team name in matches of tournament: $tournament with no: $no and new name: $new ")
        return repo.updateTeamNameByTournamentAndNo(tournament, no, new)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun updateFieldNameByTournamentAndNo(tournament: String?, no: Int?, new: String?) : Mono<Long> {
        log.trace("Update field name in matches of tournament: $tournament with no: $no and new name: $new")
        return repo.updateFieldNameByTournamentAndNo(tournament, no, new)
    }


}