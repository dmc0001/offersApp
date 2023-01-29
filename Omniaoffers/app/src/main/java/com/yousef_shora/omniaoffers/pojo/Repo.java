package com.yousef_shora.omniaoffers.pojo;

import androidx.lifecycle.MutableLiveData;

import com.yousef_shora.omniaoffers.modal.Constants;
import com.yousef_shora.omniaoffers.modal.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Repo {
    com.yousef_shora.omniaoffers.pojo.FireStoreManager fireStoreManager = new com.yousef_shora.omniaoffers.pojo.FireStoreManager();
    public MutableLiveData<ArrayList<com.yousef_shora.omniaoffers.pojo.Offer>> mutableLiveData = new MutableLiveData<>();

    public Repo() {
        mutableLiveData.setValue(new ArrayList<>());
    }

    public void insert_offers(ArrayList<com.yousef_shora.omniaoffers.pojo.Offer> offers_array_to_insert, com.yousef_shora.omniaoffers.pojo.FireStoreManager.InsertOffersCallback callback) {
        Map<String, ArrayList<com.yousef_shora.omniaoffers.pojo.Offer>> offerMap = new HashMap<>();
        offerMap.put(Constants.OFFERS_ARRAY, offers_array_to_insert);
        fireStoreManager.insert_offer_query(callback, offerMap);
    }

    public void insert_offer(com.yousef_shora.omniaoffers.pojo.Offer offer, com.yousef_shora.omniaoffers.pojo.FireStoreManager.InsertOffersCallback callback) {

        Objects.requireNonNull(mutableLiveData.getValue()).add(offer);

        HashMap<String, ArrayList<com.yousef_shora.omniaoffers.pojo.Offer>> hashMap = new HashMap<>();

        hashMap.put(Constants.OFFERS_ARRAY, mutableLiveData.getValue());

        fireStoreManager.insert_offer_query(callback, hashMap);
    }

    public void push_notification(com.yousef_shora.omniaoffers.pojo.Offer offer, com.yousef_shora.omniaoffers.pojo.FireStoreManager.PostNotificationCallback callback){
        fireStoreManager.post_notification(callback,offer);
    }

    public void upload_image(byte[] byte_array, String image_id, com.yousef_shora.omniaoffers.pojo.FireStoreManager.UploadImageCallback callback) {
        fireStoreManager.upload_image_query(callback, byte_array, image_id);
    }

    public void get_image(com.yousef_shora.omniaoffers.pojo.FireStoreManager.GetImageUrlCallback callback, String offer_id) {
        fireStoreManager.get_image_query(callback, Utils.getimagereferece(fireStoreManager.storageReference, offer_id));
    }

    public void get_offers(com.yousef_shora.omniaoffers.pojo.FireStoreManager.GetOffersCallback callback) {
        fireStoreManager.getOffers_query(callback);
    }

    public void observe_data() {
        fireStoreManager.observe_offers((offers_array) -> {
            if (offers_array != null) {
                //empty the array
                mutableLiveData.setValue(new ArrayList<>());

                mutableLiveData.setValue((ArrayList<com.yousef_shora.omniaoffers.pojo.Offer>) offers_array);
            }
        });
    }
}