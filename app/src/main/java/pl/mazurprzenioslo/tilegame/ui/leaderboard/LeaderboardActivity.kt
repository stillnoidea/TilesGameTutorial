package pl.mazurprzenioslo.tilegame.ui.leaderboard

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import pl.mazurprzenioslo.tilegame.data.Difficulty
import pl.mazurprzenioslo.tilegame.data.RankValue
import pl.mazurprzenioslo.tilegame.databinding.ActivityLeaderboardBinding
import pl.mazurprzenioslo.tilegame.service.GameService
import kotlin.math.abs

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding
    private var swipeFirstTouchX = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.leaderboardsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setupTabs()
        GameService.getRank(Difficulty.VERY_EASY) { ranks -> onRanksLoaded(ranks) }
        binding.leaderboardRecyclerView.setOnTouchListener() { _, motionEvent: MotionEvent ->
            handleUserTouchEvent(motionEvent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val chosenDifficulty = Difficulty.values()[tab.position]
                GameService.getRank(chosenDifficulty) { ranks -> onRanksLoaded(ranks) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun onRanksLoaded(ranks: List<RankValue>) {
        binding.leaderboardRecyclerView.adapter = LeaderboardItemAdapter(ranks)
    }

    private fun handleUserTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                swipeFirstTouchX = event.x
            }
            MotionEvent.ACTION_UP -> {
                val distanceX = swipeFirstTouchX - event.x
                Log.d(this.localClassName, "swipe distanceX: $distanceX")
                val currentTabPosition = binding.tabLayout.selectedTabPosition
                if (abs(distanceX) > MIN_SWIPE_DISTANCE) {
                    if (distanceX > 0 && currentTabPosition < 3) {
                        binding.tabLayout.getTabAt(currentTabPosition + 1)?.select()
                        return true
                    } else if (distanceX < 0 && currentTabPosition > 0) {
                        binding.tabLayout.getTabAt(currentTabPosition - 1)?.select()
                        return true
                    }
                }
            }
        }
        return false;
    }

    companion object {
        private const val MIN_SWIPE_DISTANCE = 150
    }
}