package pl.mazurprzenioslo.tilegame.data

import pl.mazurprzenioslo.tilegame.R

enum class TileColor(val colorResourceId: Int, val unlockCost: Int) {
    PURPLE(R.color.purple_tile, 0),
    BLUE(R.color.blue_tile, 30),
    GREEN(R.color.green_tile, 50),
    AMBER(R.color.amber_tile, 100)
}
