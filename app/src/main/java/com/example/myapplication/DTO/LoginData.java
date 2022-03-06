package com.example.myapplication.DTO;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("device")
    private String device;

    public LoginData(String password, String email, String device) {
        this.password=password;
        this.email=email;
        this.device=device;
    }
}