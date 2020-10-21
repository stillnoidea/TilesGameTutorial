package pl.mazurprzenioslo.tilegame

import Tile
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import pl.mazurprzenioslo.tilegame.databinding.ActivityGameBinding
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val tiles = Array(GRID_COLUMNS * GRID_ROWS) { Tile() }
    private lateinit var tilesGridView: GridView
    private lateinit var counterTextView: TextView
    var filledBoxesCount = AtomicInteger()
    var clearedBoxesCounter = AtomicInteger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tilesGridView = binding.tilesGridView
        counterTextView = binding.pointsCounter

        tilesGridView.numColumns = GRID_COLUMNS;

        val tilesAdapter = ArrayAdapter(this, R.layout.tile, tiles)

        tilesGridView.adapter = tilesAdapter
        tilesGridView.setOnItemClickListener { _, _, position, _ ->
            if (tiles[position].filled) {
                onTileClicked(position)
            }
        }

        tilesGridView.post {
            Log.d("Log", "Height: " + tilesGridView.height)
            tilesGridView.children.iterator()
                .forEach { tile ->
                    tile.layoutParams.height = tilesGridView.height / GRID_ROWS - 6
                }

            startGame()
        }
    }


    private fun onTileClicked(position: Int) {
        filledBoxesCount.decrementAndGet()
        tiles[position].filled = false
        tilesGridView.getChildAt(position).setBackgroundColor(getColor(R.color.white))
        counterTextView.text = clearedBoxesCounter.incrementAndGet().toString()
    }

    private fun startGame(): Thread {
        var gameFinished = false

        return thread(start = true) {
            var timeToFillMs = INITIAL_TIME_TO_FILL_MS

            while (!gameFinished) {
                if (filledBoxesCount.get() == tiles.size) {
                    gameFinished = true
//                    val intent = Intent()
//                    intent.putExtra(CLEARED_BOXES_KEY, clearedBoxesCounter)
//                    setResult(CLEARED_BOXES_RESULT_OK, intent)
//                    finish()
                    return@thread
                } else {
                    fillRandomTile()
                }

                if (clearedBoxesCounter.get() % 9 == 0) {
                    timeToFillMs -= 15
                }
                Thread.sleep(timeToFillMs)
            }
        }
    }

    private fun fillRandomTile() {
        val randomUnfilledTile = tiles.filterNot { tile -> tile.filled }.random();

        randomUnfilledTile.filled = true
        filledBoxesCount.getAndIncrement()
        runOnUiThread {
            tilesGridView.getChildAt(tiles.indexOf(randomUnfilledTile))
                .setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
        }
    }

    companion object {
        const val GRID_COLUMNS = 4
        const val GRID_ROWS = 7
        const val INITIAL_TIME_TO_FILL_MS = 500L;
        const val CLEARED_BOXES_REQUEST_CODE = 666
        const val CLEARED_BOXES_RESULT_OK = 4321
        const val CLEARED_BOXES_KEY = "clearedBoxes"
    }
}