package com.example.user.music.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.FragmentPage1;
import com.example.user.music.R;
import com.example.user.music.adapter.MusicAdapter1;
import com.example.user.music.adapter.SongAdapter1;
import com.example.user.music.entity.ListMusic;
import com.example.user.music.entity.Music;
import com.example.user.music.entity.MusicList;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SongDetailActivity extends AppCompatActivity {
    private ImageView goBack;
    private TextView songName;
    private ListView listView;
    private MusicAdapter1 adapter;
    private MusicList musicList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_detail);
        goBack = findViewById(R.id.go_back3);
        songName = findViewById(R.id.song_name);
        listView = findViewById(R.id.song_detail_list);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        String list = intent.getStringExtra("list");
        musicList = new Gson().fromJson(list,MusicList.class);
        songName.setText(musicList.getListName());
        initData();

    }
    public void initData(){
        Map<String,String> map = new HashMap<>();
        map.put("listId",musicList.getListId()+"");
        FindMusicTask task = new FindMusicTask();
        task.setMap(map);
        task.execute();
    }

    class FindMusicTask extends AsyncTask {

        private Map<String,String> map;
        private MyResponse<List<ListMusic>> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"musicList/music", map)).body().string();
                myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<List<ListMusic>>>(){}.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                List<ListMusic> songs  = myResponse.getData();
                adapter = new MusicAdapter1(SongDetailActivity.this,songs,
                        R.layout.music_item,musicList.getUserId());
                listView.setAdapter(adapter);
            }else {
                Toast.makeText(SongDetailActivity.this,"获取歌单详情失败",Toast.LENGTH_LONG).show();
            }
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
