package com.example.user.music.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.adapter.MusicAdapter;
import com.example.user.music.adapter.MusicAdapter1;
import com.example.user.music.adapter.SongerAdapter;
import com.example.user.music.entity.Music;
import com.example.user.music.entity.Singer;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyList;
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

public class SingerDetailActivity extends AppCompatActivity {

    private ImageView goBack;
    private ListView listView;
    private TextView singerName;
    private MusicAdapter adapter;
    private List<Music> musics;
    private int userId;
    private int singerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer_detail);
        goBack = findViewById(R.id.go_back3);
        singerName = findViewById(R.id.singer_name);
        listView = findViewById(R.id.singer_detail_list);
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);
        singerId = intent.getIntExtra("singerId", 0);
        String name = intent.getStringExtra("singerName");
        singerName.setText(name);
        initData();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initData() {
        Map<String,String> map = new HashMap<>();
        Log.e("测试",singerId+"");
        map.put("id",singerId+"");
        map.put("pageNum", "1");
        map.put("pageSize", "10");
        FindMusicsTask task = new FindMusicsTask();
        task.setMap(map);
        task.execute();
    }

    class FindMusicsTask extends AsyncTask {

        private Map<String,String> map;
        private MyResponse<MyList<Music>> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            String result = null;
            try {
                result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.rUrl+"api/singer/music", map)).body().string();
                Log.e("测试",result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<MyList<Music>>>(){}.getType());

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                musics  = myResponse.getData().getList();
                adapter = new MusicAdapter(SingerDetailActivity.this,musics,
                        R.layout.music_item,userId);
                listView.setAdapter(adapter);
            }else {
                Toast.makeText(SingerDetailActivity.this,"获取歌手歌曲失败",Toast.LENGTH_LONG).show();
            }
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }

}
