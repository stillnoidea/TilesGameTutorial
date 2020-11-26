package pl.mazurprzenioslo.tilegame.ui.shop

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.mazurprzenioslo.tilegame.data.TileColor
import pl.mazurprzenioslo.tilegame.databinding.ActivityShopBinding
import pl.mazurprzenioslo.tilegame.service.GameService

class ShopActivity : AppCompatActivity(), ShopItemAdapter.ShopItemListener {
    private lateinit var binding: ActivityShopBinding
    private var userMoney: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.leaderboardsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.unlockablesRecyclerView.addItemDecoration(GridSpacingItemDecoration(2, 32, true, 0))
        GameService.getLoggedPlayer {
            userMoney = it.money
            binding.moneyTextView.text = it.money.toString()
            binding.unlockablesRecyclerView.adapter =
                ShopItemAdapter(it.unlockedTileColors, it.selectedTileColor, this)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onUnlockTileColor(tileColorToUnlock: TileColor): Boolean {
        val canAfford = userMoney >= tileColorToUnlock.unlockCost
        if (canAfford) {
            GameService.unlockTileColor(tileColorToUnlock)
            userMoney -= tileColorToUnlock.unlockCost
            binding.moneyTextView.text = userMoney.toString()
            Toast.makeText(this@ShopActivity, "Kupiono kolor", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@ShopActivity, "Masz za mało monet!", Toast.LENGTH_SHORT).show()
        }
        return canAfford
    }

    override fun onSelectTileColor(tileColor: TileColor) {
        GameService.selectTileColor(tileColor)
    }
}