package at.birnbaua.tournament.recovery

import at.birnbaua.tournament.data.document.*
import at.birnbaua.tournament.data.service.*
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path


@Service
class RecoveryService {

    private val log: Logger = LoggerFactory.getLogger(RecoveryService::class.java)
    private val defaultPath = "data/recovery/"

    @Autowired private lateinit var tournamentService: TournamentService
    @Autowired private lateinit var gameroundService: GameroundService
    @Autowired private lateinit var fieldService: FieldService
    @Autowired private lateinit var teamService: TeamService
    @Autowired private lateinit var matchService: MatchService
    @Autowired private lateinit var mapper: ObjectMapper

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun saveAllToFile(tournament: String, path: Path = Path(defaultPath)) {
        val destination = "$path/$tournament"
        Files.createDirectories(Path.of(destination))
        tournamentService.findById(tournament)
            .doOnNext { log.info("Save tournament: $tournament backup to disk") }
            .subscribe { Files.write(Path.of("$destination/tournament.json"),mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(it)) }
        gameroundService.findAllByTournament(tournament)
            .collectList()
            .doOnNext{ log.info("Save ${it.size} gamerounds of tournament: $tournament backup to disk") }
            .subscribe { Files.write(Path.of("$destination/gamerounds.json"),mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(it)) }
        fieldService.findAllByTournament(tournament)
            .collectList()
            .doOnNext{ log.info("Save ${it.size} fields of tournament: $tournament backup to disk") }
            .subscribe { Files.write(Path.of("$destination/fields.json"),mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(it)) }
        teamService.findAllByTournament(tournament)
            .collectList()
            .doOnNext{ log.info("Save ${it.size} teams of tournament: $tournament backup to disk") }
            .subscribe { Files.write(Path.of("$destination/teams.json"),mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(it)) }
        matchService.findAllByTournament(tournament)
            .collectList()
            .doOnNext{ log.info("Save ${it.size} matches of tournament: $tournament backup to disk") }
            .subscribe { Files.write(Path.of("$destination/matches.json"),mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(it)) }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun replaceTournamentInDatabase(tournament: String, path: Path = Path(defaultPath)) {
        log.info("Starting replacing tournament in db with external files...")
        saveAllToFile(tournament,Path.of("data/temp/"))
        Mono.just(Files.readAllBytes(Path.of("$path/tournament.json")))
            .flatMap { tournamentService.insert(mapper.convertValue(it,Tournament::class.java)) }.subscribe()
        Mono.just(Files.readAllBytes(Path.of("$path/gamerounds.json")))
            .flatMapMany {
                val type = mapper.typeFactory.constructCollectionType(List::class.java,Gameround::class.java)
                gameroundService.insert(mapper.convertValue(it, type))
            }.subscribe()
        Mono.just(Files.readAllBytes(Path.of("$path/fields.json")))
            .flatMapMany {
                val type = mapper.typeFactory.constructCollectionType(List::class.java, Field::class.java)
                gameroundService.insert(mapper.convertValue(it, type))
            }.subscribe()
        Mono.just(Files.readAllBytes(Path.of("$path/teams.json")))
            .flatMapMany {
                val type = mapper.typeFactory.constructCollectionType(List::class.java, Team::class.java)
                gameroundService.insert(mapper.convertValue(it, type))
            }.subscribe()
        Mono.just(Files.readAllBytes(Path.of("$path/matches.json")))
            .flatMapMany {
                val type = mapper.typeFactory.constructCollectionType(List::class.java, Match::class.java)
                gameroundService.insert(mapper.convertValue(it, type))
            }.subscribe()
    }
}