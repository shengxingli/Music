package com.example.user.music.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.adapter.SongerAdapter;
import com.example.user.music.entity.Singer;
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

public class SingerActivity extends AppCompatActivity {

    private ListView listView;
    private SongerAdapter songerAdapter;
    private List<Singer> singers;
    private int userId;
    private ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer);
        listView = findViewById(R.id.all_singer);
        goBack = findViewById(R.id.go_back5);
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);
        initData();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initData() {
        FindSingersTask task = new FindSingersTask();
        task.execute();
    }

    class FindSingersTask extends AsyncTask {

        private MyResponse<List<Singer>> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Response response = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.rUrl+"api/index/newSinger", null));

                String result = response.body().string();
                myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<List<Singer>>>(){}.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                singers  = myResponse.getData();
                songerAdapter = new SongerAdapter(singers,getBaseContext(),R.layout.songer_item,userId);
                listView.setAdapter(songerAdapter);
            }else {
                Toast.makeText(SingerActivity.this,"获取歌手失败",Toast.LENGTH_LONG).show();
            }
        }
    }

}