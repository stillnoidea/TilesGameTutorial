package pl.mazurprzenioslo.tilegame.ui.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import pl.mazurprzenioslo.tilegame.R
import pl.mazurprzenioslo.tilegame.data.TileColor
import pl.mazurprzenioslo.tilegame.databinding.UnlockableTileBinding

class ShopItemAdapter(
    private val unlockedTileColors: MutableList<TileColor>,
    private val selectedTileColor: TileColor,
    private val shopItemListener: ShopItemListener
) : RecyclerView.Adapter<ShopItemAdapter.ViewHolder>() {
    private var selectedItem: ViewHolder? = null

    interface ShopItemListener {
        fun onUnlockTileColor(tileColorToUnlock: TileColor): Boolean
        fun onSelectTileColor(tileColor: TileColor)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = UnlockableTileBinding.bind(view)
        val tileColorTextView: TextView
        val colorLockedImageView: ImageView
        val unlockStatusChip: Chip

        init {
            tileColorTextView = binding.tileColorTextView
            colorLockedImageView = binding.colorLockedImageView
            unlockStatusChip = binding.unlockStatusChip
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.unlockable_tile, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val tileColor = TileColor.values()[position]
        viewHolder.tileColorTextView.setBackgroundResource(tileColor.colorResourceId)
        if (unlockedTileColors.contains(tileColor)) {
            setItemToUnlocked(viewHolder)
        } else {
            viewHolder.unlockStatusChip.text = "Odblokuj za ${tileColor.unlockCost}$"
            viewHolder.unlockStatusChip.setOnClickListener() {
                val unlocked = shopItemListener.onUnlockTileColor(tileColor)
                if (unlocked) {
                    setItemToUnlocked(viewHolder)
                    unlockedTileColors.add(tileColor)
                }
            }
        }

        viewHolder.tileColorTextView.setOnClickListener {
            if(unlockedTileColors.contains(tileColor)) {
                shopItemListener.onSelectTileColor(tileColor)
                changeSelectedItem(viewHolder)
            }
        }
        if (tileColor == selectedTileColor) {
            changeSelectedItem(viewHolder)
        }
    }

    override fun getItemCount() = TileColor.values().size

    private fun setItemToUnlocked(viewHolder: ViewHolder) {
        viewHolder.colorLockedImageView.visibility = View.INVISIBLE
        viewHolder.unlockStatusChip.setChipIconResource(R.drawable.unlocked)
        viewHolder.unlockStatusChip.text = "Posiadany"
    }

    private fun changeSelectedItem(viewHolder: ViewHolder) {
        if (selectedItem != null) {
            selectedItem!!.tileColorTextView.text = ""
        }
        selectedItem = viewHolder
        viewHolder.tileColorTextView.text = "Wybrany"
    }
}
