package com.example.user.music.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.adapter.MusicAdapter;
import com.example.user.music.entity.Music;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RankListActivity extends AppCompatActivity {
    private ImageView goBack;
    private ListView listView;
    private MusicAdapter adapter;
    private int userId;
    private List<Music> musics;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank);
        goBack = findViewById(R.id.go_back5);
        userId = getIntent().getIntExtra("userId",0);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = findViewById(R.id.rank_list);
        initData();
    }
    public void initData(){
        FindMusicsTask task = new FindMusicsTask();
        task.execute();
    }

    class FindMusicsTask extends AsyncTask {

        private MyResponse<List<Music>> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            String result =null;
            try {
                result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.rUrl+"api/index/hotMusic", null)).body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<List<Music>>>(){}.getType());

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                musics  = myResponse.getData();
                adapter = new MusicAdapter(getBaseContext(),musics,R.layout.music_item,userId);
                listView.setAdapter(adapter);
            }else {
                Toast.makeText(RankListActivity.this,"获取排行失败",Toast.LENGTH_LONG).show();
            }
        }

    }
}
