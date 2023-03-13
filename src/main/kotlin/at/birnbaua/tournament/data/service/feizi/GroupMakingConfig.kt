package at.birnbaua.tournament.data.service.feizi

class GroupMakingConfig {
    var isReversed: Boolean = false
    var ordering: List<OrderProperty> = arrayListOf(OrderProperty.EXTERNAL_RANK,OrderProperty.INTERNAL_RANK,OrderProperty.TEAM_NO)
}