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
import pl.mazurprzenioslo.tilegame.data.User

object GameService {
    private var auth = Firebase.auth
    private val database = Firebase.firestore

    fun addNewPlayer() {
        database.collection("users").document(auth.uid!!).get().addOnSuccessListener { data ->
            if (!data.exists()) {
                saveNewPlayer()
            }
        }
    }

    private fun saveNewPlayer() {
        val user = User(auth.currentUser!!.email!!, 0, 0, 0, 0, 0)
        database.collection("users").document(auth.uid!!).set(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
            }
    }

    fun processNewPlayerScore(difficulty: Difficulty, score: Int) {
        database.collection("users").document(auth.uid!!).get().addOnSuccessListener { doc ->
            addMoneyToPlayer(calculateGainedMoney(difficulty, score))
            val dbScore = doc.get(difficulty.name.toLowerCase()) as Long
            if (dbScore.compareTo(score) == -1) {
                updatePlayerScore(difficulty, score)
                checkAndUpdateRanks(difficulty, score)
            }
        }
    }

    private fun updatePlayerScore(difficulty: Difficulty, newScore: Int) {
        database.collection("users").document(auth.uid!!)
            .update(difficulty.name.toLowerCase(), newScore)
    }

    private fun addMoneyToPlayer(additionalMoney: Long) {
        database.collection("users").document(auth.uid!!)
            .update("money", FieldValue.increment(additionalMoney))
    }

    fun calculateGainedMoney(difficulty: Difficulty, score: Int): Long {
        return (difficulty.gainedMoneyMultiplier * score).toLong()
    }

    private fun checkAndUpdateRanks(difficulty: Difficulty, score: Int) {
        database.collection("ranks").document(difficulty.name.toLowerCase()).get()
            .addOnSuccessListener { data ->
                val isUserInRank = data.contains(auth.currentUser!!.email!!)
                if (isUserInRank) {
                    updateRanks(auth.currentUser!!.email!!, score, difficulty)
                } else {
                    checkAndHandleUserInRank(score, difficulty, data)
                }
            }
    }

    private fun updateRanks(login: String, score: Int, difficulty: Difficulty) {
        val path = FieldPath.of(login)
        database.collection("ranks").document(difficulty.name.toLowerCase())
            .update(path, score)
    }

    private fun checkAndHandleUserInRank(
        score: Int,
        difficulty: Difficulty,
        data: DocumentSnapshot
    ) {
        val lastUserId = getLastRankUser(data)
        if (lastUserId != null && lastUserId.score < score) {
            updateRanks(auth.currentUser!!.email!!, score, difficulty)

            val updates = hashMapOf<String, Any>(
                lastUserId.login to FieldValue.delete()
            )
            database.collection("ranks").document(difficulty.name.toLowerCase())
                .update(updates)
        } else if (lastUserId == null) {
            updateRanks(auth.currentUser!!.email!!, score, difficulty)
        }
    }

    private fun getLastRankUser(data: DocumentSnapshot): RankValue? {
        val rank = getSortedRank(data)
        if (rank.size < 10) {
            return null
        }
        return rank[9]
    }

    fun getRank(difficulty: Difficulty, onDataReturnedCallback: (MutableList<RankValue>) -> Unit) {
        database.collection("ranks").document(difficulty.name.toLowerCase()).get()
            .addOnSuccessListener { data ->
                onDataReturnedCallback(getSortedRank(data))
            }
    }

    fun getLoggedPlayer(callback: (User) -> Unit) {
        database.collection("users").document(auth.uid!!).get().addOnSuccessListener { document ->
            callback(document.toObject()!!)
        }
    }

    private fun getSortedRank(documentSnapshot: DocumentSnapshot): MutableList<RankValue> {
        val rank =
            documentSnapshot.data!!.map { userScore ->
                RankValue(
                    userScore.key,
                    userScore.value as Long
                )
            }
                .toMutableList()
        rank.sortByDescending { r -> r.score }

        return rank
    }
}
