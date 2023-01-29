package com.yousef_shora.omniaclient.pojo;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class Repo {
    FireStoreManager fireStoreManager = new FireStoreManager();
    public MutableLiveData<ArrayList<Offer>> mutableLiveData = new MutableLiveData<>();

    public Repo() {
        mutableLiveData.setValue(new ArrayList<>());
    }


    public void observe_data() {
        fireStoreManager.observe_offers((offers_array) -> {
            if (offers_array != null) {
                //empty the array
                mutableLiveData.setValue(new ArrayList<>());

                mutableLiveData.setValue((ArrayList<Offer>) offers_array);
            }
        });
    }
}