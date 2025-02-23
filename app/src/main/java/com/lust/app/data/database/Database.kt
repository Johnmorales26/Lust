package com.lust.app.data.database

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.lust.app.data.entities.LocationData
import com.lust.app.data.entities.Locations

class Database(private val db: FirebaseFirestore) {

    private val collLocationsRef = db.collection("Locations")

    fun addNewLocation(locationData: LocationData) {
        val documentRef = collLocationsRef.document()
        documentRef.set(locationData)
            .addOnSuccessListener {
                val idGenerated = documentRef.id
                collLocationsRef.document(idGenerated)
                    .update("uid", idGenerated)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Documento actualizado con ID: $idGenerated")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error al actualizar ID", e)
                    }
            }
    }

    fun fetchLocations(onResponse: (Locations) -> Unit) {
        collLocationsRef.get()
            .addOnSuccessListener { result ->
                val locations = result.documents.mapNotNull { doc ->
                    doc.toObject(LocationData::class.java)?.copy(uid = doc.id)
                }
                onResponse(Locations(locations))
            }
            .addOnFailureListener { e -> onResponse(Locations(emptyList())) }
    }

}