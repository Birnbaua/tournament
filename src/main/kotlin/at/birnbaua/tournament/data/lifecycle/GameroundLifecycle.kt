package at.birnbaua.tournament.data.lifecycle

import at.birnbaua.tournament.data.document.Gameround
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component

@Component
class GameroundLifecycle : AbstractMongoEventListener<Gameround>() {

    override fun onBeforeConvert(event: BeforeConvertEvent<Gameround>) {
        onBeforeConvert(event.source)
        return super.onBeforeConvert(event)
    }

    fun onBeforeConvert(gameround: Gameround) {
        if (gameround.groupBinding.entries.containsKey(null)) {
            val value = gameround.groupBinding.entries[null]!!
            gameround.groupBinding.entries.remove(null)
            gameround.groupBinding.entries["null"] = value
        }
        if (gameround.matchMakingConfig.containsKey(null)) {
            val value = gameround.matchMakingConfig[null]!!
            gameround.matchMakingConfig.remove(null)
            gameround.matchMakingConfig["null"] = value
        }
        if (gameround.groupMakingConfig.containsKey(null)) {
            val value = gameround.groupMakingConfig[null]!!
            gameround.groupMakingConfig.remove(null)
            gameround.groupMakingConfig["null"] = value
        }
    }

    override fun onAfterConvert(event: AfterConvertEvent<Gameround>) {
        onAfterConvert(event.source)
        super.onAfterConvert(event)
    }

    fun onAfterConvert(gameround: Gameround) {
        if(gameround.groupBinding.entries.containsKey("null")) {
            val value = gameround.groupBinding.entries["null"]!!
            gameround.groupBinding.entries.remove("null")
            gameround.groupBinding.entries[null] = value
        }
        if (gameround.matchMakingConfig.containsKey("null")) {
            val value = gameround.matchMakingConfig["null"]!!
            gameround.matchMakingConfig.remove("null")
            gameround.matchMakingConfig[null] = value
        }
        if (gameround.groupMakingConfig.containsKey("null")) {
            val value = gameround.groupMakingConfig["null"]!!
            gameround.groupMakingConfig.remove("null")
            gameround.groupMakingConfig[null] = value
        }
    }
}