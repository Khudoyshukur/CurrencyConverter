package uz.androdev.currencyconverter.data.repository.impl

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import uz.androdev.currencyconverter.data.exception.NoInternetConnectionException
import uz.androdev.currencyconverter.data.repository.ConversionHistoryRepository
import uz.androdev.currencyconverter.model.ConvertResult
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 10:25 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class FirebaseConversionHistoryRepositoryImpl @Inject constructor(
) : ConversionHistoryRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val auth by lazy { Firebase.auth }
    private val database by lazy { Firebase.database.reference }
    private val conversionHistory = MutableStateFlow<List<FirebaseConvertResult>>(emptyList())

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val history = snapshot.children.mapNotNull {
                it.getValue(FirebaseConvertResult::class.java)
            }
            conversionHistory.update { history }
        }

        override fun onCancelled(error: DatabaseError) {}
    }

    init {
        scope.launch { listenConversionHistory() }
    }

    @kotlin.jvm.Throws(NoInternetConnectionException::class)
    override suspend fun saveConversionResult(convertResult: ConvertResult) {
        val currentUser =
            getCurrentUser() ?: throw NoInternetConnectionException("Check connection")

        database.child(currentUser.uid).push().setValue(convertResult.toFirebaseConvertResult())

        if (conversionHistory.value.isEmpty()) {
            listenConversionHistory()
        }
    }

    override fun getConversionResults(): Flow<List<ConvertResult>> {
        return conversionHistory.map { list -> list.map { it.toConvertResult() } }
    }

    private suspend fun listenConversionHistory() {
        val currentUser = getCurrentUser() ?: return
        try {
            database.child(currentUser.uid).removeEventListener(valueEventListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        database.child(currentUser.uid).addValueEventListener(valueEventListener)
    }

    private suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser ?: suspendCoroutine { continuation ->
            auth.signInAnonymously().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resumeWith(Result.success(auth.currentUser!!))
                } else {
                    continuation.resumeWith(Result.success(null))
                }
            }
        }
    }
}

private data class FirebaseConvertResult(
    val dateTimeMillis: Long = 0,
    val result: String = ""
)

private fun FirebaseConvertResult.toConvertResult() = ConvertResult(
    dateTime = Instant.ofEpochMilli(dateTimeMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime(),
    result = result
)

private fun ConvertResult.toFirebaseConvertResult() = FirebaseConvertResult(
    dateTimeMillis = dateTime.atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli(),
    result = result
)