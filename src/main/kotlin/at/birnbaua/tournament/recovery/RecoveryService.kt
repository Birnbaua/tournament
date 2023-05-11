package at.birnbaua.tournament.recovery

import at.birnbaua.tournament.data.document.*
import at.birnbaua.tournament.data.service.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.Path
import kotlin.io.path.isDirectory


@Service
class RecoveryService {

    private val log: Logger = LoggerFactory.getLogger(RecoveryService::class.java)
    private val defaultPath = "data/recovery/current"
    private val historyPath = "data/recovery/history"

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
        copyToHistory(tournament)
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
        saveAllToFile(tournament,Path.of("data/recovery/temp"))
        Mono.just(Files.readString(Path.of("$path/$tournament/tournament.json")))
            .flatMap { tournamentService.insert(mapper.readValue(it,Tournament::class.java)) }.subscribe()


        Mono.just(Files.readString(Path.of("$path/$tournament/gamerounds.json")))
            .flatMap { gameroundService.insert(readToList<Gameround>(it)).collectList() }
            .subscribe()
        /*
        Mono.just(Files.readString(Path.of("$path/$tournament/fields.json")))
            .flatMapMany {
                val type = mapper.typeFactory.constructCollectionType(List::class.java, Field::class.java)
                gameroundService.insert(mapper.readValue(it, type))
            }.subscribe()
        Mono.just(Files.readString(Path.of("$path/$tournament/teams.json")))
            .flatMapMany {
                val type = mapper.typeFactory.constructCollectionType(List::class.java, Team::class.java)
                gameroundService.insert(mapper.readValue(it, type))
            }.subscribe()
        Mono.just(Files.readString(Path.of("$path/$tournament/matches.json")))
            .flatMapMany {
                val type = mapper.typeFactory.constructCollectionType(List::class.java, Match::class.java)
                gameroundService.insert(mapper.readValue(it, type))
            }.subscribe()

         */
    }

    fun copyToHistory(tournament: String, path: Path = Path.of(historyPath)) {
        Files.createDirectories(Path.of("$path/$tournament"))
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.SSSSSS")
        val dateTime: LocalDateTime = LocalDateTime.now()
        val now = dateTime.format(formatter).replace('_','T')
        Files.walk(Path.of("$defaultPath/$tournament"))
            .forEach {
                try {
                    if(it.isDirectory()) {
                        Files.copy(it,Path.of("$path/$tournament/$now"))
                    } else {
                        Files.copy(it,Path.of("$path/$tournament/$now/${it.fileName}"))
                    }
                }catch(_: Exception) {}
            }
    }

    private inline fun <reified T> readToList(str: String) : List<T> {
        val type = mapper.typeFactory.constructCollectionType(List::class.java,T::class.java)
        return mapper.readValue(str,type)
    }
}