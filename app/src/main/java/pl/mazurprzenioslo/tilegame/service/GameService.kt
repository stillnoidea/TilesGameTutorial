package pl.mazurprzenioslo.tilegame.service

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pl.mazurprzenioslo.tilegame.data.Difficulty
import pl.mazurprzenioslo.tilegame.data.RankValue
import pl.mazurprzenioslo.tilegame.data.TileColor
import pl.mazurprzenioslo.tilegame.data.User

object GameService {
    private const val USERS_COLLECTION_PATH = "users"
    private const val RANKS_COLLECTION_PATH = "ranks"
    private var auth = Firebase.auth
    private val database = Firebase.firestore

    fun addNewPlayer() {

    }

    private fun saveNewPlayer() {

    }

    fun processNewPlayerScore(difficulty: Difficulty, score: Int) {

    }

    private fun updatePlayerScore(difficulty: Difficulty, newScore: Int) {

    }

    private fun addMoneyToPlayer(additionalMoney: Long) {

    }

    fun calculateGainedMoney(difficulty: Difficulty, score: Int): Long {
        return (difficulty.gainedMoneyMultiplier * score).toLong()
    }

    fun unlockTileColor(tileColor: TileColor) {

    }

    fun selectTileColor(tileColor: TileColor) {

    }

    private fun checkAndUpdateRanks(difficulty: Difficulty, score: Int) {

    }

    private fun updateRanks(login: String, score: Int, difficulty: Difficulty) {

    }

    private fun checkAndHandleUserInRank(score: Int, difficulty: Difficulty, data: DocumentSnapshot) {

    }

    private fun getLastRankUser(data: DocumentSnapshot): RankValue? {
        val rank = getSortedRank(data)
        if (rank.size < 10) {
            return null
        }
        return rank[9]
    }

    fun getRank(difficulty: Difficulty, onDataReturnedCallback: (MutableList<RankValue>) -> Unit) {

    }

    fun getLoggedPlayer(callback: (User) -> Unit) {

    }

    private fun getSortedRank(documentSnapshot: DocumentSnapshot): MutableList<RankValue> {

    }
}
