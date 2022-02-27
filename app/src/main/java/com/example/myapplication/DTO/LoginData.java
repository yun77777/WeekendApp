package com.example.myapplication.DTO;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    public LoginData(String password, String email) {
        this.password=password;
        this.email=email;
    }
}