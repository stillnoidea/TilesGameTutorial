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
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.game_finished))
                .setMessage(
                    """
                    Wynik: ${arguments?.get(GameActivity.CLEARED_TILES_COUNT_KEY)} pkt
                    Zdobyte monety: ${arguments?.get(GameActivity.GAINED_MONEY_KEY)}""".trimIndent()
                )
                .setCancelable(false)
                .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                    listener.onPlayAgain(this)
                }.setNegativeButton(getString(R.string.return_to_main_menu)) { _, _ ->
                    listener.onReturnToMainMenu(this)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}