package pl.mazurprzenioslo.tilegame.ui.game

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import pl.mazurprzenioslo.tilegame.R

class GameFinishedDialogFragment : DialogFragment() {
    private lateinit var listener: GameFinishedDialogListener

    interface GameFinishedDialogListener {
        fun onReturnToMainMenu(dialog: DialogFragment)
        fun onPlayAgain(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as GameFinishedDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement GameFinishedDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val array =
                arrayOf(getString(R.string.return_to_main_menu), getString(R.string.play_again))
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.game_finished))
                .setCancelable(false)
                .setItems(array) { _, checkedItemIndex ->
                    when (checkedItemIndex) {
                        0 -> listener.onReturnToMainMenu(this)
                        1 -> listener.onPlayAgain(this)
                    }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}