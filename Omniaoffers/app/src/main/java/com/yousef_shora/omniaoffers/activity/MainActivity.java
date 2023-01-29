package com.yousef_shora.omniaoffers.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.yousef_shora.omniaoffers.R;
import com.yousef_shora.omniaoffers.modal.RecyclerViewAdapter;
import com.yousef_shora.omniaoffers.modal.SwipeToDeleteCallback;
import com.yousef_shora.omniaoffers.pojo.Offer;
import com.yousef_shora.omniaoffers.viewmodel.MainViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    MainViewModel mainViewModel;
    RecyclerViewAdapter myAdapter;
    ArrayList<Offer> recycler_data = new ArrayList<>();
    RelativeLayout parent_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.add_fab);
        parent_layout = findViewById(R.id.parent_relativelayout);
        recyclerView = findViewById(R.id.rvoffers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,

                DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.offersMutableLiveData.observe(this, offers -> {
            recycler_data.clear();
            recycler_data.addAll(offers);
            myAdapter.notifyDataSetChanged();
        });

        myAdapter = new RecyclerViewAdapter(recycler_data);
        recyclerView.setAdapter(myAdapter);

        mainViewModel.deletedDataMutableLiveData.observe(this, position -> {
            if (position != -1) {
                Snackbar snackbar = Snackbar
                        .make(parent_layout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar
                        .make(parent_layout, "Couldn't delete offer.", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }

        });
        SwipeToDeleteCallback swipecallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                new AlertDialog.Builder(mContext)
                        .setTitle("Delete Item")
                        .setMessage("Are you sure you want to delete this Item?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> myAdapter.removeItem(position, mainViewModel))
                        .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> myAdapter.notifyItemChanged(position))
                        .setOnCancelListener(dialogInterface -> myAdapter.notifyItemChanged(position))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(swipecallback);
        helper.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(view ->

        {
            Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
            startActivity(intent);
        });
    }


}