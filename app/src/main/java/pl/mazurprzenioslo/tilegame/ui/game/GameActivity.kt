package pl.mazurprzenioslo.tilegame.ui.game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.mazurprzenioslo.tilegame.ui.main.MainActivity
import pl.mazurprzenioslo.tilegame.R
import pl.mazurprzenioslo.tilegame.data.Difficulty
import pl.mazurprzenioslo.tilegame.data.Tile
import pl.mazurprzenioslo.tilegame.databinding.ActivityGameBinding
import pl.mazurprzenioslo.tilegame.service.Service
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.timerTask


class GameActivity : FragmentActivity(), GameFinishedDialogFragment.GameFinishedDialogListener {
    private lateinit var binding: ActivityGameBinding
    private val tiles = Array(GRID_COLUMNS * GRID_ROWS) { Tile() }
    private lateinit var tilesGridView: GridView
    private lateinit var counterTextView: TextView
    private var filledBoxesCount = AtomicInteger()
    private var clearedBoxesCounter = AtomicInteger()
    private lateinit var difficulty: Difficulty
    var gameFinished = false
//    private val service: Service = Service()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tilesGridView = binding.tilesGridView
        counterTextView = binding.pointsCounter

        difficulty = intent.getSerializableExtra(MainActivity.DIFFICULTY_KEY) as Difficulty
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
                    tile.layoutParams.height = tilesGridView.height / GRID_ROWS - 4
                }

            CoroutineScope(Main).launch {
                startGame()
            }.invokeOnCompletion {
                showGameFinishedDialog()
            }
        }
    }

    private fun onTileClicked(position: Int) {
        if (!gameFinished) {
            filledBoxesCount.decrementAndGet()
            tiles[position].filled = false
            tilesGridView.getChildAt(position).setBackgroundColor(getColor(R.color.white))
            counterTextView.text = clearedBoxesCounter.incrementAndGet().toString()
        }
    }

    private suspend fun startGame() {
        var timeToFillMs = difficulty.initialTimeToFillMs

        resetGame()
        //TODO countdown 3 2 1
        delay(DELAY_BEFORE_FIRST_FILL_MS)
        Timer().schedule(
            timerTask { timeToFillMs -= difficulty.timeToFillDecreaseMs },
            difficulty.initialTimeToFillMs,
            difficulty.periodOfTimeToFillDecreaseMs
        )

        while (!gameFinished) {
            fillRandomTile()
            if (filledBoxesCount.get() == tiles.size) {
                gameFinished = true
            }
            delay(timeToFillMs)
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

    private fun showGameFinishedDialog() {
        Service.checkAndUpdatePlayerScoreAndRank(difficulty, clearedBoxesCounter.get(), 0)
        GameFinishedDialogFragment().apply { isCancelable = false }
            .show(supportFragmentManager, "gameFinishedDialog")
    }

    private fun resetGame() {
        gameFinished = false
        tilesGridView.children.iterator()
            .forEach { tile -> tile.setBackgroundColor(getColor(R.color.white)) }
        tiles.forEach { tile -> tile.filled = false }
        filledBoxesCount.set(0)
        clearedBoxesCounter.set(0)
        counterTextView.text = "0"
    }

    companion object {
        const val GRID_COLUMNS = 4
        const val GRID_ROWS = 7
        const val DELAY_BEFORE_FIRST_FILL_MS = 1000L;
        const val FINISHED_PLAYING_RESULT_OK = 4321
        const val CLEARED_TILES_COUNT_KEY = "clearedTilesCount"
    }

    override fun onReturnToMainMenu(dialog: DialogFragment) {
        val intent = Intent()
        intent.putExtra(CLEARED_TILES_COUNT_KEY, clearedBoxesCounter)
        setResult(FINISHED_PLAYING_RESULT_OK, intent)
        finish()
    }

    override fun onPlayAgain(dialog: DialogFragment) {
        CoroutineScope(Main).launch {
            startGame()
        }.invokeOnCompletion {
            showGameFinishedDialog()
        }
    }
}