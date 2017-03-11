package com.rent_it_app.rent_it;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rent_it_app.rent_it.firebase.Config;
import com.rent_it_app.rent_it.json_models.Item;
import com.rent_it_app.rent_it.json_models.Review;
import com.rent_it_app.rent_it.json_models.ReviewEndpoint;

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
    private Button readMore;
    private RatingBar itemRating;
    private ProgressDialog progress;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        final ProgressDialog dia = ProgressDialog.show(this, null, "Loading...");

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
        readMore = (Button)findViewById(R.id.readMoreButton);
        itemRating = (RatingBar) findViewById(R.id.rRating);
        //progress = ProgressDialog.show(this, "dialog title","dialog message", true);

        //progress.show();
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

                    Log.d("response ",""+response);
                Log.d("response.body() ",""+response.body());
                Log.d("rList ",""+rList);
                Log.d("response.raw()",""+response.raw());
                Log.d("response.toString() ",""+response);

                //Log.d("nullCheck",rList.toString());
                    rTitle.setText(rList.getTitle());
                    itemRating.setRating(rList.getItemRating());
                    Log.d("getItemRating() ","" + rList.getItemRating());
                    //rReviewer.setText("by " + rList.getReviewer());
                    //till we create user model
                    rReviewer.setText("by James L");
                    String s = rList.getItemComment();
                    if (s.length() > 100) {
                        s = s.substring(0, 100) + "...";
                    }
                    rComment.setText(s);
                Log.d("retrofit.call.enqueue", ""+statusCode);
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        dia.dismiss();
                    }
                }, 1000);
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                rComment.setText("No review available");
                readMore.setVisibility(View.GONE);
                rTitle.setVisibility(View.GONE);
                rReviewer.setVisibility(View.GONE);
                itemRating.setVisibility(View.GONE);

                Log.d("retrofit.call.enqueue", "failed "+t);
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        dia.dismiss();
                    }
                }, 1000);
            }

        });

        //progress.dismiss();
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myFancyMethod(v);
                Intent myIntent = new Intent(ListingActivity.this, ShowItemReviewsActivity.class);
                myIntent.putExtra("ITEM_ID", rList.getItem());
                ListingActivity.this.startActivity(myIntent);
            }
        });

    }



}
