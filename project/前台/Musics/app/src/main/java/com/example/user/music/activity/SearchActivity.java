package com.example.user.music.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.example.user.music.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ImageView goBack;
    private EditText search;
    private Button send;
    private ImageView release;
    private GridView gridView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        goBack = findViewById(R.id.go_back15);
        search = findViewById(R.id.search);
        send = findViewById(R.id.send);
        release = findViewById(R.id.release);
        gridView = findViewById(R.id.history);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,TabActivity.class);
                startActivity(intent);
            }
        });
        final List<String> list = new ArrayList<>();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索一次gridView 内容就加一条
                String history = search.getText().toString();
                list.add(history);
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SearchActivity.this,android.R.layout.simple_list_item_1,list);
        gridView.setAdapter(arrayAdapter);
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
            }
        });
        search.getBackground().setAlpha(100);
        send.getBackground().setAlpha(100);
    }
}
