package at.birnbaua.tournament.recovery

import at.birnbaua.tournament.data.service.GameroundService
import at.birnbaua.tournament.data.service.TeamService
import at.birnbaua.tournament.data.service.TournamentService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@EnableScheduling
@Configuration
class RecoveryScheduler {

    @Autowired private lateinit var tournamentService: TournamentService
    @Autowired private lateinit var gameroundService: GameroundService
    @Autowired private lateinit var fieldService: GameroundService
    @Autowired private lateinit var teamService: TeamService

    private val log: Logger = LoggerFactory.getLogger(RecoveryScheduler::class.java)


    init { log.info("Recovery scheduler started...") }

    @Scheduled(initialDelay = 500, fixedDelay = 1000*60)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun savingCurrent() {

    }

    private fun save(directory: String, name: String, content: Any) {

    }

}