package ca.tetervak.friendlyeats.firestore

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class FirestoreCollectionLiveData<T>(
        private val query: Query,
        private val type: Class<T>
) : LiveData<List<T>>() {

    companion object {
        private const val TAG = "FirestoreCollectionLiveData"
    }

    private var registration: ListenerRegistration? = null
    private val listener = EventListener<QuerySnapshot> { snapshot, error ->

        if (error != null) {
            Log.d(TAG, "Listen Failed.")
        }

        val list = ArrayList<T>()
        snapshot?.forEach { doc ->
            list.add(doc.toObject(type))
        }

        value = list
    }

    override fun onActive() {
        super.onActive()
        // add listener
        registration = query.addSnapshotListener(listener)
    }

    override fun onInactive() {
        super.onInactive()
        // remove listener
        registration?.remove()
    }
}