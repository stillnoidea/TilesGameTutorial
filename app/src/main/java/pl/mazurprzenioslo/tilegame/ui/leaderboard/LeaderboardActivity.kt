package pl.mazurprzenioslo.tilegame.ui.leaderboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import pl.mazurprzenioslo.tilegame.data.Difficulty
import pl.mazurprzenioslo.tilegame.data.RankValue
import pl.mazurprzenioslo.tilegame.databinding.ActivityLeaderboardBinding
import pl.mazurprzenioslo.tilegame.service.GameService

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.leaderboardsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setupTabs()
        GameService.getRank(Difficulty.VERY_EASY) { ranks -> onRanksLoaded(ranks) }
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
}