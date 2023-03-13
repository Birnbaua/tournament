package at.birnbaua.tournament.data.service.feizi

class SimpleOrderConfig {
    var pointsPerSetWon: Int = 0
    var pointsPerSetDraw: Int = 0
    var pointsPerMatchWon: Int = 0
    var pointsPerMatchDraw: Int = 0
    var groupInternalOrdering: List<OrderProperty> = arrayListOf(OrderProperty.POINTS, OrderProperty.GAME_POINTS, OrderProperty.EXTERNAL_CORRECTION, OrderProperty.INTERNAL_CORRECTION)
    var groupExternalOrdering: List<OrderProperty> = arrayListOf(OrderProperty.INTERNAL_RANK, OrderProperty.POINTS, OrderProperty.GAME_POINTS, OrderProperty.EXTERNAL_CORRECTION)
}