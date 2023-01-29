package com.yousef_shora.omniaoffers.pojo;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yousef_shora.omniaoffers.BuildConfig;
import com.yousef_shora.omniaoffers.modal.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FireStoreManager {
    private final FirebaseFirestore db;
    public StorageReference storageReference;

    public interface GetOffersCallback {
        void onCallback(List<Offer> offers_array);
    }

    public interface InsertOffersCallback {
        void onCallback(boolean inserted);
    }

    public interface UploadImageCallback {
        void onCallback(boolean uploaded);
    }

    public interface GetImageUrlCallback {
        void onCallback(String image_url);
    }

    public interface PostNotificationCallback {
        void onCallback(boolean posted);
    }

    public void getOffers_query(GetOffersCallback callback) {

        db.collection(Constants.OFFERS_COLLECTION).document(Constants.OFFERS_ARRAY)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<Offer> offers = Objects.requireNonNull(document.toObject(OfferDocument.class)).offers_array;
                            callback.onCallback(offers);
                        } else {
                            Log.w(Constants.FireStore_Log, "Error getting offers." + task.getException(), task.getException());
                            callback.onCallback(null);
                        }
                    }
                });
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

    public void insert_offer_query(InsertOffersCallback callback, Map<String, ArrayList<Offer>> offers_array_to_insert) {
        db.collection(Constants.OFFERS_COLLECTION).document(Constants.OFFERS_ARRAY).set(offers_array_to_insert)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(Constants.FireStore_Log, "Error inserting offers.", task.getException());
                        callback.onCallback(false);
                    } else {
                        Log.v(Constants.FireStore_Log, "Inserted offers successfully. " + offers_array_to_insert);
                        callback.onCallback(true);
                    }
                });
    }

    public void get_image_query(GetImageUrlCallback callback, StorageReference reference) {
        reference.getDownloadUrl().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(Constants.FireStore_Log, "Error uploading image.", task.getException());
                callback.onCallback(null);

            } else {
                Log.v(Constants.FireStore_Log, "got image url successfully.");
                callback.onCallback(task.getResult().toString());
            }
        });
    }

    public void upload_image_query(UploadImageCallback callback, byte[] image, String offer_id) {
        StorageReference ref = storageReference.child(Constants.OFFERS_COLLECTION + "/" + offer_id);
        ref.putBytes(image).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(Constants.FireStore_Log, "Error uploading image.", task.getException());
                callback.onCallback(false);
            } else {
                Log.v(Constants.FireStore_Log, "Uploaded image successfully.");
                callback.onCallback(true);
            }
        });
    }

    public void post_notification(PostNotificationCallback callback, Offer offer) {
        new Thread(() -> {
            String fcm_api_key = BuildConfig.FCM_API_KEY;
            try {

                Response notification_to_post = post("{\n" +
                        "    \"to\": \"/topics/all\",\n" +
                        "    \"notification\": {\n" +
                        "        \"body\":" + "\"Category: " + offer.getCategory() + "\",\n" +
                        "        \"title\":" + "\"" + offer.getTitle() + "\",\n" +
                        "        \"image\":" + "\"" + ((offer.getPicture_url() == null) ? Constants.LIMITED_OFFER_IMAGE : offer.getPicture_url()) + "\"\n" +
                        "    }\n" +
                        "}", fcm_api_key);

                if (notification_to_post.body() != null) {
                    Log.v(Constants.FireStore_Log,
                            "posting notification" + Objects.requireNonNull(notification_to_post.body()).string());
                }
                else{
                    Log.v(Constants.FireStore_Log,
                            "posting notification failed");
                }

                callback.onCallback(notification_to_post.code() == 200);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private Response post(String json, String key) throws IOException {
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, JSON); // new
        // RequestBody body = RequestBody.create(JSON, json); // old
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .header("Authorization", "key=" + key)
                .build();
        return client.newCall(request).execute();
    }

    public FireStoreManager() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = storage.getReference();
        storageReference = storage.getReference();
    }
}
