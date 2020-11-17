package pl.mazurprzenioslo.tilegame.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pl.mazurprzenioslo.tilegame.R
import pl.mazurprzenioslo.tilegame.data.Difficulty
import pl.mazurprzenioslo.tilegame.databinding.ActivityMainBinding
import pl.mazurprzenioslo.tilegame.ui.game.GameActivity
import pl.mazurprzenioslo.tilegame.ui.leaderboard.LeaderboardActivity
import pl.mazurprzenioslo.tilegame.ui.login.LoginActivity

class MainActivity : AppCompatActivity(),
    DifficultySelectionDialogFragment.DifficultySelectionDialogListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun runGame(view: View) {
        DifficultySelectionDialogFragment().show(
            supportFragmentManager,
            "difficultySelectionDialog"
        )
    }

    fun showLeaderboards(v: View){
        val intent = Intent(this, LeaderboardActivity::class.java)
        startActivity(intent)
    }

    fun signOut(v: View){
        Firebase.auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("isSignedOut", true)
        startActivity(intent)
    }

    override fun onDifficultySelected(dialog: DialogFragment, difficulty: Difficulty) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(DIFFICULTY_KEY, difficulty)
        startActivity(intent)
    }

    companion object {
        const val DIFFICULTY_KEY = "selectedDifficulty";
        const val IS_SIGNED_OUT = "isSignedOut";
    }
}