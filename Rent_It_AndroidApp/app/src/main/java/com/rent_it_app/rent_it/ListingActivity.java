package com.rent_it_app.rent_it;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rent_it_app.rent_it.firebase.Config;
import com.rent_it_app.rent_it.json_models.Item;
import com.rent_it_app.rent_it.json_models.ItemEndpoint;
import com.rent_it_app.rent_it.json_models.Review;
import com.rent_it_app.rent_it.json_models.ReviewEndpoint;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by malhan on 3/8/17.
 */

public class ListingActivity extends BaseActivity{

    Item myItem;
    Retrofit retrofit;
    ReviewEndpoint reviewEndpoint;
    private Review rList;
    Gson gson;
    private TextView txtTitle, txtDescription, txtCondition, txtCity, txtRate;
    private TextView rTitle, rReviewer, rComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //this.getSupportActionBar().setTitle("EDIT LISTING");

        myItem = (Item) getIntent().getSerializableExtra(Config.MORE_DATA);

        //Define
        txtTitle = (TextView) findViewById(R.id.title);
        txtDescription = (TextView)findViewById(R.id.description);
        txtCondition = (TextView)findViewById(R.id.condition);
        txtCity = (TextView)findViewById(R.id.city);
        txtRate = (TextView)findViewById(R.id.rate);
        rTitle = (TextView)findViewById(R.id.rTitle);
        rReviewer = (TextView)findViewById(R.id.rReviewer);
        rComment = (TextView)findViewById(R.id.rComment);

        //populate fields
        txtTitle.setText(myItem.getTitle());
        txtDescription.setText(myItem.getDescription());
        txtCity.setText("Location : " + myItem.getCity());
        txtCondition.setText("Condition : " + myItem.getCondition());
        txtRate.setText("$" + myItem.getRate() + " /day");
        //txtRate.setText("$" + String.format("%.2f", myItem.getRate()));

        String itemId = myItem.getId();

        gson = new Gson();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        reviewEndpoint = retrofit.create(ReviewEndpoint.class);
        //rList = new ArrayList<Review>();

        Call<Review> call = reviewEndpoint.getLatestReviewByItemId(itemId);
        //Call<ArrayList<Review>> call = reviewEndpoint.getLatestReviewByItemId(itemId);
        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                int statusCode = response.code();
                rList = response.body();
                rTitle.setText(rList.getTitle());
                rReviewer.setText("by " + rList.getReviewer());
                rComment.setText(rList.getItemComment());

                Log.d("retrofit.call.enqueue", ""+statusCode);
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Log.d("retrofit.call.enqueue", "failed "+t);
            }
        });

    }

}
