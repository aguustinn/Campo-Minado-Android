package com.campominado.app.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirestoreDicaDataSource(
    private val firestoreProvider: () -> FirebaseFirestore = { FirebaseFirestore.getInstance() }
) {
    suspend fun listar(): List<Dica> = suspendCancellableCoroutine { continuation ->
        firestoreProvider().collection("dicas").get()
            .addOnSuccessListener { snapshot ->
                if (continuation.isActive) continuation.resume(snapshot.documents.map { document ->
                    Dica(
                        id = document.getString("id") ?: document.id,
                        titulo = document.getString("titulo").orEmpty(),
                        descricao = document.getString("descricao").orEmpty()
                    )
                })
            }
            .addOnFailureListener { erro ->
                if (continuation.isActive) continuation.resumeWithException(erro)
            }
    }
}
