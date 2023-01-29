package com.yousef_shora.omniaclient.pojo;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yousef_shora.omniaclient.modal.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FireStoreManager {
    private final FirebaseFirestore db;

    public interface GetOffersCallback {
        void onCallback(List<Offer> offers_array);
    }

    public void observe_offers(GetOffersCallback callback) {
        db.collection(Constants.OFFERS_COLLECTION).document(Constants.OFFERS_ARRAY).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(Constants.FireStore_Log, "Listen failed.", error);
                callback.onCallback(null);
                return;
            }

            String source = value != null && value.getMetadata().hasPendingWrites()
                    ? "Local" : "Server";

            if (value != null && value.exists()) {
                Log.d(Constants.FireStore_Log, source + " data: " + value.getData());
                List<Offer> offers = Objects.requireNonNull(value.toObject(OfferDocument.class)).offers_array;
                callback.onCallback(offers);
            } else {
                Log.d(Constants.FireStore_Log, source + " data: null");
                callback.onCallback(null);
            }

        });
    }

    public FireStoreManager() {
        db = FirebaseFirestore.getInstance();
    }
}
