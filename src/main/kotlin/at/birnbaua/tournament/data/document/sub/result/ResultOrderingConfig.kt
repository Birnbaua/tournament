package at.birnbaua.tournament.data.document.sub.result

import at.birnbaua.mongo_tournament_service.data.document.sub.gen.GeneratingScope

class ResultOrderingConfig {
    class Ordering(
        var scope: GeneratingScope = GeneratingScope.ALL,
        var offset: Long = 0,
        var orderProperties: Array<OrderProperty> = arrayOf()
    ){var no: Int = 0}
    var reverseOrdering: Boolean = false
    var isRelative: Boolean = true
    var internalOrdering = arrayListOf(Ordering(GeneratingScope.ALL,0, arrayOf(OrderProperty.POINTS, OrderProperty.GAME_POINTS, OrderProperty.DIRECT_MATCHES)))
    var externalOrdering = arrayListOf(Ordering(GeneratingScope.ALL,0, arrayOf(OrderProperty.INTERNAL_RANK, OrderProperty.POINTS, OrderProperty.GAME_POINTS, OrderProperty.DIRECT_MATCHES)))
}