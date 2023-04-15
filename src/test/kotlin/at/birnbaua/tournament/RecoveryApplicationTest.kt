package at.birnbaua.tournament

import at.birnbaua.tournament.data.document.Tournament
import at.birnbaua.tournament.data.service.TournamentService
import at.birnbaua.tournament.recovery.RecoveryService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RecoveryApplicationTest {

    @Autowired private lateinit var tournamentService: TournamentService
    @Autowired private lateinit var recoveryService: RecoveryService

    @BeforeEach
    fun beforeEach() {
        tournamentService.deleteAll().subscribe()
    }

    @Test
    fun testRecoverySave() {
        val tournament = Tournament()
        tournament.id = "TEST"
        tournament.name = "TEST NAME"
        tournament.desc = "TEST DESC"
        tournament.title = "TEST TITLE"
        tournamentService.insert(tournament).subscribe()
        recoveryService.saveAllToFile(tournament.id!!)
        tournamentService.existsById("TEST").subscribe{ assert(it) }
    }
}