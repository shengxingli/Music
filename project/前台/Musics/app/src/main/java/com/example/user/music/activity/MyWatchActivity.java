package com.example.user.music.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.adapter.AttentionAdapter;
import com.example.user.music.entity.MusicList;
import com.example.user.music.entity.User;
import com.example.user.music.entity.WatchList;
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

import static java.security.AccessController.getContext;

public class MyWatchActivity extends AppCompatActivity {

    private ImageView goBack;
    private ListView listView;
    private AttentionAdapter adapter;
    private int userId;
    private List<User> users;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_watch);
        goBack = findViewById(R.id.go_back17);
        userId = getIntent().getIntExtra("userId",0);
        listView = findViewById(R.id.my_attention_item);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }

    public void initData(){
        Map<String,String> map = new HashMap<>();
        map.put("userId",userId+"");
        FindMyWatchTask task = new FindMyWatchTask();
        task.setMap(map);
        task.execute();
    }

    class FindMyWatchTask extends AsyncTask {

        private Map<String,String> map;
        private MyResponse<List<WatchList>> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"user/watchList", map)).body().string();
                Log.e("测试",result);
                myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<List<WatchList>>>(){}.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                for(WatchList w: myResponse.getData()) {
                    users = new ArrayList<>();
                    User user = new User();
                    user.setUserId(w.getWatchId());
                    user.setUserName(w.getWatchName());
                    users.add(user);
                }
                if (users != null){
                    adapter = new AttentionAdapter(MyWatchActivity.this,users,R.layout.layout_my_watch_item);
                    listView.setAdapter(adapter);
                }
            }else {
                Toast.makeText(MyWatchActivity.this,"获取关注列表失败", Toast.LENGTH_LONG).show();
            }
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
