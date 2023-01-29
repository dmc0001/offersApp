package com.yousef_shora.omniaclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yousef_shora.omniaclient.R;
import com.yousef_shora.omniaclient.modal.FilterAdapter;
import com.yousef_shora.omniaclient.modal.OnFilterClickListener;
import com.yousef_shora.omniaclient.modal.OnOfferClickListener;
import com.yousef_shora.omniaclient.modal.RecyclerViewAdapter;
import com.yousef_shora.omniaclient.modal.Utils;
import com.yousef_shora.omniaclient.pojo.Filter;
import com.yousef_shora.omniaclient.pojo.Offer;
import com.yousef_shora.omniaclient.viewmodel.MainViewModel;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView filtersRecyclerView;
    MainViewModel mainViewModel;
    RecyclerViewAdapter myAdapter;
    FilterAdapter filterAdapter;
    ArrayList<Offer> offers = new ArrayList<>();
    ArrayList<Filter> filters = new ArrayList<>();
    RelativeLayout parent_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.subscribe_to_notifications();

        parent_layout = findViewById(R.id.parent_relativelayout);
        recyclerView = findViewById(R.id.rvoffers);
        filtersRecyclerView = findViewById(R.id.rv_filter_offers);
        setup_rvs();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.shownoffersMutableLiveData.observe(this, shown_offers -> {
            offers.clear();
            offers.addAll(shown_offers);
            Log.v("updated",shown_offers.toString());
            myAdapter.notifyDataSetChanged();
        });
        mainViewModel.filtersMutableLiveData.observe(this, new_filters -> {
            filters.clear();
            filters.addAll(new_filters);
            filterAdapter.notifyDataSetChanged();
        });

    }

    OnOfferClickListener offer_clickListener = offer -> {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("offer", offer);
        startActivity(intent);
    };

    OnFilterClickListener filter_clickListener = filter -> mainViewModel.selected_filter(filter);

    public void setup_rvs() {
        filtersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new RecyclerViewAdapter(offers, offer_clickListener, this);
        filterAdapter = new FilterAdapter(getApplicationContext(), filters, filter_clickListener);
        recyclerView.setAdapter(myAdapter);
        filtersRecyclerView.setAdapter(filterAdapter);
    }
}