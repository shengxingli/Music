package com.example.user.music.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.adapter.SongAdapter2;
import com.example.user.music.entity.MusicList;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddSongActivity extends AppCompatActivity {
    private TextView addSongCancel;
    private LinearLayout addSong;
    private ListView listView;
    private String musicName;
    private int userId;
    private int musicId;
    private List<MusicList> lists;
    private SongAdapter2 adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_song);
        addSongCancel = findViewById(R.id.cancel_add_song);
        addSong = findViewById(R.id.add_song1);
        listView = findViewById(R.id.list_song);
        //获取上个页面传的数据
        Intent intent = getIntent();
        musicName = intent.getStringExtra("musicName");
        userId = intent.getIntExtra("userId",0);
        musicId = intent.getIntExtra("musicId",0);
        //点击事件
        addSongCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        addSong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AddSongActivity.this,NewSongActivity.class);
//                intent.putExtra("userId",userId);
//                startActivity(intent);
//            }
//        });
        //获取用户个人歌单的所有数据
        initData();
    }
    public void initData(){
        Map<String,String> map = new HashMap<>();
        map.put("userId",userId+"");
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
//            MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"musicList/list", map), new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    String result = response.body().string();
//                    myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<List<MusicList>>>(){}.getType());
//                }
//            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                lists  = myResponse.getData();
                adapter = new SongAdapter2(getBaseContext(),lists,R.layout.song_item1,musicId,musicName);
                listView.setAdapter(adapter);
            }else {
                Toast.makeText(AddSongActivity.this,"获取用户歌单失败",Toast.LENGTH_LONG).show();
            }
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
