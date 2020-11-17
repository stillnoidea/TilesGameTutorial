package pl.mazurprzenioslo.tilegame.ui.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.mazurprzenioslo.tilegame.R
import pl.mazurprzenioslo.tilegame.data.RankValue
import pl.mazurprzenioslo.tilegame.databinding.LeaderboardItemBinding

class LeaderboardItemAdapter(private val dataSet: List<RankValue>) :
    RecyclerView.Adapter<LeaderboardItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = LeaderboardItemBinding.bind(view)
        val rankTextView: TextView
        val loginTextView: TextView
        val scoreTextView: TextView

        init {
            rankTextView = binding.rankTextView
            loginTextView = binding.loginTextView
            scoreTextView = binding.scoreTextView
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.leaderboard_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.rankTextView.text = "${position + 1}."
        viewHolder.loginTextView.text = dataSet[position].login
        viewHolder.scoreTextView.text = dataSet[position].score.toString()
    }

    override fun getItemCount() = dataSet.size
}
