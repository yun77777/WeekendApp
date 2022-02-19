package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SubActivity extends AppCompatActivity {

    private TextView tv_sub;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        tv_sub = (TextView)findViewById(R.id.tv_sub);
        list = (ListView)findViewById(R.id.list);

        List<String> data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);

        data.add("hello");
        data.add("android");
        data.add("list");
        adapter.notifyDataSetChanged();

        Intent intent = getIntent();
        String str = intent.getStringExtra("str");

        Log.d("get str: ", str);

        tv_sub.setText(str);
    }
}