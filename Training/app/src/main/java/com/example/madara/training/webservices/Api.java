package com.example.madara.training.webservices;

import com.example.madara.training.models.LoginResponse;
import com.example.madara.training.models.MainResponse;
import com.example.madara.training.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by madara on 2/22/18.
 */

public interface Api {
    @Headers("content-type: application/json")
    //@POST("api/login")
    @POST("login-user.php")
    Call<LoginResponse> loginUser(@Body User user);

    @POST("register-user.php")
    //@POST("api/register")
    Call<MainResponse> registerUser(@Body User user);

}