package com.rent_it_app.rent_it.json_models;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Miz on 2/1/17.
 */

public interface CategoryEndpoint {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("api/categories")
    Call<ArrayList<Category>> getCategories(/*@Path("username") String username*/);

    /*@GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);*/

    /*@POST("api/genres")
    Call<GenrePost> createUser(@Body GenrePost genrePost);*/

}
