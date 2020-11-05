package pl.mazurprzenioslo.tilegame.service

data class User(
    var login: String,
    var very_easy: Int,
    var easy: Int,
    var medium: Int,
    var hard: Int,
    var money: Long
)

data class RankValue(var login: String, var score: Long)
