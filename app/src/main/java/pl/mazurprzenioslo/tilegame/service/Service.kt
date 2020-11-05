package pl.mazurprzenioslo.tilegame.service

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.mazurprzenioslo.tilegame.Difficulty

object Service {
    private var auth = Firebase.auth
    private val database = Firebase.firestore

    fun addNewPlayer() {
        checkPlayerAndAdd()
    }

    private fun checkPlayerAndAdd() {
        val a = auth
        database.collection("users").document(auth.uid!!).get().addOnSuccessListener { data ->
            if (!data.exists()) {
                handleNewPlayer()
            }
        }
    }

    private fun handleNewPlayer() {
        val user: User = User(auth.currentUser!!.email!!, 0, 0, 0, 0, 0)
        database.collection("users").document(auth.uid!!).set(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
            }
    }

    fun checkAndUpdatePlayerScoreAndRank(difficulty: Difficulty, score: Int, additionalMoney: Long) {
        database.collection("users").document(auth.uid!!).get().addOnSuccessListener { doc ->
            val dbScore = doc.get(difficulty.name.toLowerCase()) as Long
            if (dbScore.compareTo(score) == -1) {
                updatePlayerScoreAndMoney(difficulty, score, additionalMoney)
                checkAndUpdateRanks(difficulty, score)
            }
        }
    }

    private fun updatePlayerScoreAndMoney(difficulty: Difficulty, newScore: Int, additionalMoney: Long) {
        database.collection("users").document(auth.uid!!)
            .update(difficulty.name.toLowerCase(), newScore)
        database.collection("users").document(auth.uid!!)
            .update("money", FieldValue.increment(additionalMoney))
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

    private fun checkAndHandleUserInRank(score: Int, difficulty: Difficulty, data: DocumentSnapshot) {
        val lastUserId = getLastRankUser(data)
        if (lastUserId != null && lastUserId.score < score) {
            updateRanks(auth.currentUser!!.email!!, score, difficulty)

            val updates = hashMapOf<String, Any>(
                lastUserId.login to FieldValue.delete())
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
        return rank[0]
    }

    fun getRank(difficulty: Difficulty, onDataReturnedCallback: (MutableList<RankValue>) -> Unit) {
        database.collection("ranks").document(difficulty.name.toLowerCase()).get()
            .addOnSuccessListener {data ->
            onDataReturnedCallback(getSortedRank(data))
        }
    }

    private fun getSortedRank(data: DocumentSnapshot): MutableList<RankValue>{
        val rank = mutableListOf<RankValue>()
        for (user: String in data.data!!.keys) {
            val value = RankValue(user, data[user] as Long)
            rank.add(value)
        }
        rank.sortBy { r -> r.score }
        return rank
    }
}
