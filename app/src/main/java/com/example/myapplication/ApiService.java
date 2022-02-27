package com.example.myapplication;

import com.example.myapplication.DTO.JoinData;
import com.example.myapplication.DTO.JoinResponse;
import com.example.myapplication.DTO.LoginData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    @Headers({"Content-Type: application/json"})
    @POST("/user/login")
    Call<ResponseBody> postLoginFunc(@Body LoginData data);

    @Headers({"Content-Type: application/json"})
    @POST("/user/join")
    Call<ResponseBody> postJoinFunc(@Body JoinData data);



    @GET("/retrofit/get")
    Call<ResponseBody> getFunc(@Query("data") String data);

    @FormUrlEncoded
    @POST("/retrofit/post")
    Call<ResponseBody> postFunc(@Field("data") String data);

    @FormUrlEncoded
    @PUT("/retrofit/put/{id}")
    Call<ResponseBody> putFunc(@Path("id") String id, @Field("data") String data);

    @DELETE("/retrofit/delete/{id}")
    Call<ResponseBody> deleteFunc(@Path("id") String id);
}