package at.birnbaua.tournament.data.lifecycle

import at.birnbaua.tournament.data.document.template.TournamentTemplate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.data.mongodb.core.mapping.event.*
import org.springframework.stereotype.Component

@Order(1)
@Component
class TournamentTemplateLifecycle : AbstractMongoEventListener<TournamentTemplate>() {

    private val log: Logger = LoggerFactory.getLogger(TournamentTemplateLifecycle::class.java)
    @Autowired private lateinit var gameroundTemplateLifecycle: GameroundTemplateLifecycle

    override fun onBeforeConvert(event: BeforeConvertEvent<TournamentTemplate>) {
        log.trace("Before-Tournament-Convert called")
        event.source.gameroundTemplates.forEach { template ->
            gameroundTemplateLifecycle.onBeforeConvert(template.value)
        }
        super.onBeforeConvert(event)
    }

    override fun onAfterConvert(event: AfterConvertEvent<TournamentTemplate>) {
        log.trace("After-Tournament-Convert called")
        event.source.gameroundTemplates.forEach { template ->
            gameroundTemplateLifecycle.onAfterConvert(template.value)
        }
        super.onAfterConvert(event)
    }

}