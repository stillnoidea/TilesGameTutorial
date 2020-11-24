package pl.mazurprzenioslo.tilegame.data

data class User(
    var login: String = "",
    var very_easy: Int = 0,
    var easy: Int = 0,
    var medium: Int = 0,
    var hard: Int = 0,
    var money: Long = 0
)

data class RankValue(var login: String, var score: Long)
