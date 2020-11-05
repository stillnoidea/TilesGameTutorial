package pl.mazurprzenioslo.tilegame

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pl.mazurprzenioslo.tilegame.databinding.ActivityMainBinding

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
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    override fun onDifficultySelected(dialog: DialogFragment, difficulty: Difficulty) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(DIFFICULTY_KEY, difficulty)
        startActivity(intent)
    }

    companion object {
        const val DIFFICULTY_KEY = "selectedDifficulty";
        const val IS_SIGNED_OUT = "isSignedOut";
    }

    fun signOut(v: View){
        Firebase.auth.signOut()
        val intent = Intent(this, Login::class.java)
        intent.putExtra("isSignedOut", true)
        startActivity(intent)
    }
}