package com.example.myapplication.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.Activity.JoinActivity;
import com.example.myapplication.Activity.LoginActivity;
import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.DTO.LoginData;
import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String email;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            email = getArguments().getString("email");
            Log.d("email from activity:", email);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("email from activity2:", email);

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        Switch swc_push = (Switch) view.findViewById(R.id.swc_push);

        Log.d("swc_push:", String.valueOf(swc_push));
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared", 0);

        String push = sharedPreferences.getString("push", "");
        boolean bool = false;
        if (push.equals("true")) {
            Log.e("swc_push: On", push);
            bool = true;
        } else {
            Log.e("swc_push: Off", push);
        }

        swc_push.setChecked(bool);

        swc_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("push", String.valueOf(b));
                editor.commit();
                Log.e("b:", String.valueOf(b));

            }
        });


        Retrofit retrofit;
        ApiService service;
        final String URL = "http://10.0.2.2:5000";

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);


        Button btn_logout = (Button) view.findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("email:",email);

                Call<ResponseBody> call_post = service.getLogoutFunc(email);
                call_post.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            String result = null;
                            try {
                                result = response.body().string();
                                JsonParser jsonParser = new JsonParser();
                                JsonElement element = jsonParser.parse(result);

                                String msg = element.getAsJsonObject().get("msg").getAsString();
                                String status = element.getAsJsonObject().get("status") != null ? element.getAsJsonObject().get("status").getAsString() : null;
                                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                System.out.println("msg : " + msg);


                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);



                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "error = " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), "Response Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
//        return inflater.inflate(R.layout.fragment_settings, container, false);


    }
}