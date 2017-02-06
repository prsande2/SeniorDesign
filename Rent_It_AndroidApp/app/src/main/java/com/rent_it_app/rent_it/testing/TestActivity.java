package com.rent_it_app.rent_it.testing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rent_it_app.rent_it.BaseActivity;
import com.rent_it_app.rent_it.R;
import com.rent_it_app.rent_it.json_models.Genre;
import com.rent_it_app.rent_it.json_models.GenreEndpoint;
import com.rent_it_app.rent_it.json_models.GenrePost;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TestActivity extends BaseActivity {


    public static final String BASE_URL = "http://10.0.2.2:3000/";
    Retrofit retrofit;
    GenreEndpoint genreEndpoint;
    TextView tv1;
    EditText genreEditText;
    Gson gson;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        gson = new Gson();
        tv1 = (TextView)findViewById(R.id.textView1);
        genreEditText = (EditText)findViewById(R.id.genre);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        genreEndpoint = retrofit.create(GenreEndpoint.class);


        Call<List<Genre>> call = genreEndpoint.getGenres();
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                int statusCode = response.code();
                List<Genre> genres = response.body();
                StringBuilder sb = new StringBuilder();
                for (Genre g: genres){
                    sb.append(g.getName() + ",");
                }
                tv1.setText(sb.toString());
                Log.d("retrofit.call.enqueue", ""+statusCode);
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                Log.d("retrofit.call.enqueue", "failed");
            }
        });
    }

    public void postGenre(View v)
    {
        GenrePost g = new GenrePost();
        g.setName(genreEditText.getText().toString());
        Call<GenrePost> call = genreEndpoint.createUser(g);
        call.enqueue(new Callback<GenrePost>() {
            @Override
            public void onResponse(Call<GenrePost> call, Response<GenrePost> response) {
                int statusCode = response.code();

                Log.d("retrofit.call.enqueue", ""+statusCode);
            }

            @Override
            public void onFailure(Call<GenrePost> call, Throwable t) {
                Log.d("retrofit.call.enqueue", "failed");
            }
        });
    }


}
