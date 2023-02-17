package at.birnbaua.tournament.data.document.sub.result

import at.birnbaua.tournament.data.service.feizi.OrderProperty


class ResultOrderingConfig {

    class Ordering(
        var offset: Long = 0,
        var orderProperties: Array<OrderProperty> = arrayOf()
    ){var no: Int = 0}
    var reverseOrdering: Boolean = false
    var internalOrdering = arrayListOf(Ordering(0, arrayOf(OrderProperty.POINTS, OrderProperty.GAME_POINTS, OrderProperty.DIRECT_MATCHES)))
    var externalOrdering = arrayListOf(Ordering(0, arrayOf(OrderProperty.INTERNAL_RANK, OrderProperty.POINTS, OrderProperty.GAME_POINTS, OrderProperty.DIRECT_MATCHES)))

}