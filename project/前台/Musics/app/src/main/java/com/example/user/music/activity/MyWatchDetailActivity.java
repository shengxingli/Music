package com.example.user.music.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.adapter.AttentionAdapter;
import com.example.user.music.adapter.AttentionDetailAdapter;
import com.example.user.music.entity.MusicList;
import com.example.user.music.entity.Post;
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

public class MyWatchDetailActivity extends AppCompatActivity {
    private ImageView goBack;
    private ListView listView;
    private AttentionDetailAdapter adapter;
    private TextView userName;
    private int watchUserId;
    private String watchUserName;
    private List<Post>posts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_watch_detail);
        userName = findViewById(R.id.my_attention_user_name);
        Intent intent = getIntent();
        watchUserName = intent.getStringExtra("watchUserName");
        watchUserId = intent.getIntExtra("watchUserId",0);
        userName.setText(watchUserName);
        goBack = findViewById(R.id.go_back18);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = findViewById(R.id.attention_user_detail);

        initData();
    }

    public void initData(){
        Map<String,String> map = new HashMap<>();
        map.put("userId",watchUserId+"");
        FindMyWatchUploadTask task = new FindMyWatchUploadTask();
        task.setMap(map);
        task.execute();
    }

    class FindMyWatchUploadTask extends AsyncTask {

        private Map<String,String> map;
        private MyResponse<List<Post>> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"post/userList", map)).body().string();
                myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<List<Post>>>(){}.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                posts  = myResponse.getData();
                adapter = new AttentionDetailAdapter(MyWatchDetailActivity.this, posts, R.layout.layout_my_watch_detail_item);
                listView.setAdapter(adapter);
            }else {
                Toast.makeText(MyWatchDetailActivity.this,"获取关注上传列表失败", Toast.LENGTH_LONG).show();
            }
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
