package com.example.user.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.user.music.R;
import com.example.user.music.adapter.SongAdapter;
import com.example.user.music.entity.MusicList;
import com.example.user.music.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ManageSongActivity extends AppCompatActivity {
    private ImageView goBack;
    private LinearLayout addSong;
    private ListView listView;
    private SongAdapter adapter;
    private User user;
    private Gson gson;
    private List<MusicList> songs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_song);
        goBack = findViewById(R.id.go_back1);
        addSong = findViewById(R.id.add_song1);
        gson = new Gson();
        String str = getIntent().getStringExtra("user");
        user = gson.fromJson(str,User.class);
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageSongActivity.this,NewSongActivity.class);
                intent.putExtra("userId",user.getUserId());
                startActivity(intent);
            }
        });
        //为返回图标设置点击事件
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = findViewById(R.id.song_item);
        songs =initData();
        adapter = new SongAdapter(ManageSongActivity.this,songs,R.layout.song_item);
        listView.setAdapter(adapter);
    }
    public List<MusicList> initData(){
        Intent intent = getIntent();
        String str = intent.getStringExtra("lists");
        List<MusicList> songs = gson.fromJson(str,new TypeToken<List<MusicList>>(){}.getType());
        return songs;
    }

    public void reflash(MusicList musicList){
        songs.remove(musicList);
        adapter.notifyDataSetChanged();
    }
}
