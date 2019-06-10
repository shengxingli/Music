package com.example.user.music.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.music.FragmentPage1;
import com.example.user.music.R;
import com.example.user.music.adapter.SongAdapter1;
import com.example.user.music.entity.Music;
import com.example.user.music.entity.MusicList;
import com.example.user.music.entity.User;
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

public class SongActivity extends AppCompatActivity {
    private ImageView goBack;
    private ListView listView;
    private SongAdapter1 adapter;
    private String str;
    private List<MusicList> songs;
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song);
        goBack = findViewById(R.id.go_back4);
        str = getIntent().getStringExtra("user");
        user = new Gson().fromJson(str,User.class);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = findViewById(R.id.song_sheet_list);
        initData();
    }
    public void initData(){
        Map<String,String> map = new HashMap<>();
        map.put("userId",user.getUserId()+"");
        FindMusicListTask task = new FindMusicListTask();
        task.setMap(map);
        task.execute();
    }

    class FindMusicListTask extends AsyncTask {

        private Map<String,String> map;
        private MyResponse<List<MusicList>> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"musicList/list", map)).body().string();
                myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<List<MusicList>>>(){}.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                songs  = myResponse.getData();
                adapter = new SongAdapter1(str,getBaseContext(),songs,R.layout.song_item1);
                listView.setAdapter(adapter);
            }else {
                Toast.makeText(SongActivity.this,"获取用户歌单失败",Toast.LENGTH_LONG).show();
            }
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
