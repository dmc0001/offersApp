package com.yousef_shora.omniaclient.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.yousef_shora.omniaclient.modal.Utils;
import com.yousef_shora.omniaclient.pojo.Filter;
import com.yousef_shora.omniaclient.pojo.Offer;
import com.yousef_shora.omniaclient.pojo.Repo;

import java.util.ArrayList;
import java.util.Objects;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Offer>> offersMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<Offer>> shownoffersMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<Filter>> filtersMutableLiveData = new MutableLiveData<>();
    private final Repo repo;

    private final Observer<ArrayList<Offer>> observer = offers -> {
        offersMutableLiveData.setValue(offers);
        shownoffersMutableLiveData.setValue(offersMutableLiveData.getValue());
    };

    public void selected_filter(Filter filter) {
        ArrayList<Offer> new_offers = new ArrayList<>();

        if (filter.getName().equalsIgnoreCase("all")) {
            new_offers.addAll(Objects.requireNonNull(offersMutableLiveData.getValue()));
        } else {
            String category = filter.getName();
            for (Offer offer : Objects.requireNonNull(offersMutableLiveData.getValue())) {
                if (offer.getCategory().equalsIgnoreCase(category))
                    new_offers.add(offer);
            }
        }
        shownoffersMutableLiveData.setValue(new_offers);
    }

    public MainViewModel() {
        repo = new Repo();
        repo.observe_data();
        repo.mutableLiveData.observeForever(observer);
        filtersMutableLiveData.setValue(Utils.get_filters());
    }

    @Override
    protected void onCleared() {
        // assuming it's the same LiveData
        repo.mutableLiveData.removeObserver(observer);
        super.onCleared();

    }
}