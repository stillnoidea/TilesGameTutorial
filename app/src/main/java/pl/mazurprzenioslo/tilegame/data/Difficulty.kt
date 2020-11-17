package pl.mazurprzenioslo.tilegame.data

import pl.mazurprzenioslo.tilegame.R

enum class Difficulty(
    val nameResourceId: Int,
    val initialTimeToFillMs: Long,
    val timeToFillDecreaseMs: Long,
    val periodOfTimeToFillDecreaseMs: Long,
) {
    VERY_EASY(R.string.very_easy_difficulty, 1500L, 30L, 6500L),
    EASY(R.string.easy_difficulty, 750L, 15L, 5400L),
    MEDIUM(R.string.medium_difficulty, 500L, 12L, 4200L),
    HARD(R.string.hard_difficulty, 350L, 8L, 3000L)
}