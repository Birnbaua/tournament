package at.birnbaua.tournament.data.lifecycle

import at.birnbaua.tournament.data.document.template.GameroundTemplate
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component

@Component
class GameroundTemplateLifecycle : AbstractMongoEventListener<GameroundTemplate>() {

    override fun onBeforeConvert(event: BeforeConvertEvent<GameroundTemplate>) {
        onBeforeConvert(event.source)
        return super.onBeforeConvert(event)
    }

    fun onBeforeConvert(template: GameroundTemplate) {
        if (template.groupBinding.entries.containsKey(null)) {
            val value = template.groupBinding.entries[null]!!
            template.groupBinding.entries.remove(null)
            template.groupBinding.entries["null"] = value
        }
        if (template.matchMakingConfig.containsKey(null)) {
            val value = template.matchMakingConfig[null]!!
            template.matchMakingConfig.remove(null)
            template.matchMakingConfig["null"] = value
        }
        if (template.groupMakingConfig.containsKey(null)) {
            val value =template.groupMakingConfig[null]!!
            template.groupMakingConfig.remove(null)
            template.groupMakingConfig["null"] = value
        }
    }

    override fun onAfterConvert(event: AfterConvertEvent<GameroundTemplate>) {
        onAfterConvert(event.source)
        super.onAfterConvert(event)
    }

    fun onAfterConvert(template: GameroundTemplate) {
        if(template.groupBinding.entries.containsKey("null")) {
            val value = template.groupBinding.entries["null"]!!
            template.groupBinding.entries.remove("null")
            template.groupBinding.entries[null] = value
        }
        if (template.matchMakingConfig.containsKey("null")) {
            val value = template.matchMakingConfig["null"]!!
            template.matchMakingConfig.remove("null")
            template.matchMakingConfig[null] = value
        }
        if (template.groupMakingConfig.containsKey("null")) {
            val value = template.groupMakingConfig["null"]!!
            template.groupMakingConfig.remove("null")
            template.groupMakingConfig[null] = value
        }
    }
}