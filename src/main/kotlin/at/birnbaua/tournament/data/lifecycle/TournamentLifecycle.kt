package at.birnbaua.tournament.data.lifecycle

import at.birnbaua.tournament.data.document.Tournament
import at.birnbaua.tournament.data.document.template.TournamentTemplate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component

@Component
class TournamentLifecycle : AbstractMongoEventListener<Tournament>() {
    private val log: Logger = LoggerFactory.getLogger(TreeBeforeConvertCallback::class.java)
    @Autowired
    private lateinit var gameroundTemplateLifecycle: GameroundTemplateLifecycle

    override fun onBeforeConvert(event: BeforeConvertEvent<Tournament>) {
        log.debug("Before-Tournament-Convert called")
        event.source.gameroundTemplates.forEach { template ->
            gameroundTemplateLifecycle.onBeforeConvert(template.value)
        }
        super.onBeforeConvert(event)
    }

    override fun onAfterConvert(event: AfterConvertEvent<Tournament>) {
        log.debug("After-Tournament-Convert called")
        event.source.gameroundTemplates.forEach { template ->
            gameroundTemplateLifecycle.onAfterConvert(template.value)
        }
        super.onAfterConvert(event)
    }
}