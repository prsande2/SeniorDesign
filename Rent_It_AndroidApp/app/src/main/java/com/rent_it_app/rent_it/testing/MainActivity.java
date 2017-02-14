package com.rent_it_app.rent_it.testing;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rent_it_app.rent_it.BaseActivity;
import com.rent_it_app.rent_it.R;
import com.rent_it_app.rent_it.json_models.Item;
import com.rent_it_app.rent_it.json_models.ItemEndpoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends BaseActivity {


    Retrofit retrofit;
    ItemEndpoint itemEndpoint;
    TextView txtTitle, txtDescription, txtCondition, txtCategory, txtLocation;
    TextView txtTags, txtValue, txtRate;
    Gson gson;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gson = new Gson();
        txtTitle = (TextView)findViewById(R.id.title);
        txtDescription = (TextView)findViewById(R.id.description);
        txtCondition = (TextView)findViewById(R.id.condition);
        txtCategory = (TextView)findViewById(R.id.category);
        txtLocation = (TextView)findViewById(R.id.city);
        txtTags = (TextView)findViewById(R.id.tags);
        txtValue = (TextView)findViewById(R.id.value);
        txtRate = (TextView)findViewById(R.id.rate);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.REST_API_BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itemEndpoint = retrofit.create(ItemEndpoint.class);


        Call<List<Item>> call = itemEndpoint.getItems();
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                int statusCode = response.code();
                List<Item> items = response.body();
                StringBuilder sb = new StringBuilder();
                for (Item i: items){
                    boolean first = true;
                    for (String t: i.getTags()){
                        if(first){
                            sb.append(t);
                            first = false;
                        }
                        else { sb.append(", " + t); }
                    }
                }
                //tv1.setText(sb.toString());
                Log.d("retrofit.call.enqueue", ""+statusCode);

                txtTitle.setText(items.get(0).getTitle());
                txtDescription.setText(items.get(0).getDescription());
                txtCondition.setText(items.get(0).getCondition());
                txtCategory.setText(items.get(0).getCategory());
                txtLocation.setText(items.get(0).getCity());
                txtTags.setText(sb.toString());
                txtValue.setText("$ " + items.get(0).getValue());
                txtRate.setText("$ " + items.get(0).getRate() + " /day");
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.d("retrofit.call.enq.fail", t.toString());
            }
        });
    }


}
