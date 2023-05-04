package at.birnbaua.tournament.data.lifecycle

import at.birnbaua.tournament.util.Tree
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback
import org.springframework.stereotype.Component

@Order(1)
@Component
class TreeBeforeConvertCallback : BeforeConvertCallback<Tree<Any?,Any?>> {

    private val log: Logger = LoggerFactory.getLogger(TreeBeforeConvertCallback::class.java)
    override fun onBeforeConvert(entity: Tree<Any?, Any?>, collection: String): Tree<Any?, Any?> {
        println("Called beforeasdfsadfasdf convert callback")
        println("do")
            entity.entries["root"] = Tree.TreeEntry()

        println("called after editing")
        return entity
    }
}