package com.example.madara.training.webservices;

import com.example.madara.training.models.Card;
import com.example.madara.training.models.LoginResponse;
import com.example.madara.training.models.MainResponse;
import com.example.madara.training.models.Rfid;
import com.example.madara.training.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by madara on 2/22/18.
 */

public interface Api {
    @Headers("content-type: application/json")
    //@POST("login")
    @POST("login-user.php")
    Call<LoginResponse> loginUser(@Body User user);

    //@POST("register-user.php")
    @POST("register")
    Call<MainResponse> registerUser(@Body User user);
    @POST("bindcard")
    Call<MainResponse> bindCard(@Body Card card);
    @FormUrlEncoded
    @POST("getMyCards")
    Call<List<Rfid>> getCards(@Field("user_id") int user_id);

}
