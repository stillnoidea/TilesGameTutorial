package pl.mazurprzenioslo.tilegame.data

data class User(
    val login: String = "",
    var unlockedTileColors: MutableList<TileColor> = ArrayList(),
    var selectedTileColor: TileColor = TileColor.PURPLE,
    var very_easy: Int = 0,
    var easy: Int = 0,
    var medium: Int = 0,
    var hard: Int = 0,
    var money: Long = 0
)

data class RankValue(val login: String, var score: Long)