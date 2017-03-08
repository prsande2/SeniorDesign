package com.rent_it_app.rent_it;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rent_it_app.rent_it.firebase.Config;
import com.rent_it_app.rent_it.json_models.Item;
import com.rent_it_app.rent_it.json_models.ItemEndpoint;

import retrofit2.Retrofit;

/**
 * Created by malhan on 3/8/17.
 */

public class ListingActivity extends BaseActivity{

    Item myItem;
    Retrofit retrofit;
    ItemEndpoint itemEndpoint;
    Gson gson;
    private TextView txtTitle, txtDescription, txtCondition, txtCity, txtRate;

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

        //populate fields
        txtTitle.setText(myItem.getTitle());
        txtDescription.setText(myItem.getDescription());
        txtCity.setText(myItem.getCity());
        txtCondition.setText(myItem.getCondition());
        Log.d("rate:"," "+myItem.getRate());
        txtRate.setText("$" + String.format("%.2f", myItem.getRate()));

    }

}
