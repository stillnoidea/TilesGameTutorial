package pl.mazurprzenioslo.tilegame.data

import pl.mazurprzenioslo.tilegame.R

enum class TileColor(val colorResourceId: Int, val unlockCost: Int) {
    RED(R.color.red_tile, 20),
    GREEN(R.color.green_tile, 30),
    BLUE(R.color.blue_tile, 50),
    AMBER(R.color.amber_tile, 100)
}
