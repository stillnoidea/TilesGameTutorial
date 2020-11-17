package pl.mazurprzenioslo.tilegame.ui.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import pl.mazurprzenioslo.tilegame.R
import pl.mazurprzenioslo.tilegame.data.Difficulty

class DifficultySelectionDialogFragment : DialogFragment() {
    private lateinit var listener: DifficultySelectionDialogListener

    interface DifficultySelectionDialogListener {
        fun onDifficultySelected(dialog: DialogFragment, difficulty: Difficulty)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DifficultySelectionDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement GameFinishedDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val array =
                Difficulty.values().map { difficulty -> getString(difficulty.nameResourceId) }
                    .toTypedArray()
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.select_difficulty_title))
                .setItems(array) { _, checkedItemIndex ->
                    listener.onDifficultySelected(
                        this,
                        Difficulty.values().get(checkedItemIndex)
                    )
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}