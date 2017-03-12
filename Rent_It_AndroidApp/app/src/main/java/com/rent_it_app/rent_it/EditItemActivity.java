package com.rent_it_app.rent_it;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.util.StringUtils;
import com.google.gson.Gson;
import com.rent_it_app.rent_it.firebase.Config;
import com.rent_it_app.rent_it.json_models.Item;
import com.rent_it_app.rent_it.json_models.ItemEndpoint;
import com.rent_it_app.rent_it.views.AvailabeItemFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by malhan on 3/4/17.
 */

public class EditItemActivity extends BaseActivity{

    Item myItem;
    Retrofit retrofit;
    ItemEndpoint itemEndpoint;
    Gson gson;
    private EditText txtTitle, txtDescription, txtCondition, txtZipcode;
    private EditText txtTags, txtValue, txtRate, txtCity;
    private Spinner spnCategory;
    private List<String> arrayTags;
    //private String[] arrayTag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("EDIT LISTING");

        myItem = (Item) getIntent().getSerializableExtra(Config.EXTRA_DATA);

        //Define
        spnCategory = (Spinner)findViewById(R.id.spinner1);
        txtTitle = (EditText)findViewById(R.id.title);
        txtDescription = (EditText)findViewById(R.id.description);
        txtCondition = (EditText)findViewById(R.id.condition);
        txtZipcode = (EditText)findViewById(R.id.zipcode);
        txtCity = (EditText)findViewById(R.id.city);
        txtTags = (EditText)findViewById(R.id.tags);
        txtRate = (EditText)findViewById(R.id.rate);
        txtValue = (EditText)findViewById(R.id.value);

        //populate fields
        txtTitle.setText(myItem.getTitle());
        txtDescription.setText(myItem.getDescription());
        txtCity.setText(myItem.getCity());
        txtCondition.setText(myItem.getCondition());
        txtZipcode.setText(Integer.toString(myItem.getZipcode()));
        Log.d("rate:"," "+myItem.getRate());
        //String myRate = String.format("%.2f", myItem.getRate());
        //Log.d("string rate:"," "+myRate);
        txtRate.setText(String.format("%.2f", myItem.getRate()));
        //txtValue.setText(Double.toString(myItem.getValue()));
        Log.d("value:"," "+myItem.getValue());
        txtValue.setText(String.format("%.2f", myItem.getValue()));

        //Tag value
        arrayTags = myItem.getTags();
        String myTag = TextUtils.join(",", arrayTags);
        txtTags.setText(myTag);

        //set category spinner value
        String compareValue = myItem.getCategory();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spnCategory.setSelection(spinnerPosition);
        }

        //for updating data

        gson = new Gson();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itemEndpoint = retrofit.create(ItemEndpoint.class);


    }
}
