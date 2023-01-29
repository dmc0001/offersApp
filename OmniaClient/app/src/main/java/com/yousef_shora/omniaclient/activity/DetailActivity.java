package com.yousef_shora.omniaclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.yousef_shora.omniaclient.R;
import com.yousef_shora.omniaclient.pojo.Offer;

public class DetailActivity extends AppCompatActivity {
    ImageView detailImageView;
    TextView description;
    TextView title;
    Offer offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detailImageView = findViewById(R.id.detailImage);
        description = findViewById(R.id.description);
        title = findViewById(R.id.detail_title);
        Intent intent = getIntent();
        offer = (Offer) intent.getSerializableExtra("offer"); //if it's a string you stored.
        if (offer.getPicture_url() != null)
            Picasso.get().load(offer.getPicture_url()).into(detailImageView);
        title.setText(offer.getTitle());
       if(offer.getDescription() != null)
           description.setText(offer.getDescription());
    }
}