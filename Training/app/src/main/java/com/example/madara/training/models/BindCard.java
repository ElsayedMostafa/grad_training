package com.example.madara.training.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 3/27/18.
 */

public class BindCard {
    @SerializedName("user_id")
    public int id;
    @SerializedName("password")
    public String password;
    @SerializedName("qrcode")
    public String qrcode;
}
