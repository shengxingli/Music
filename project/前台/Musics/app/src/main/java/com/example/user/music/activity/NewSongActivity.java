package com.example.user.music.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.entity.MusicList;
import com.example.user.music.entity.User;
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

public class NewSongActivity extends AppCompatActivity {
    private TextView cancel,save;
    private EditText songName;
    private NewSongListener listener;
    private String newSongName;
    private String str;
    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_song);
        cancel = findViewById(R.id.cancel);
        save = findViewById(R.id.save);
        songName = findViewById(R.id.et_song_name);
        listener = new NewSongListener();
        //接收数据
        save.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        Intent intent = getIntent();
        str = intent.getStringExtra("user");
        user = new Gson().fromJson(str,User.class);

    }
    class NewSongListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cancel:
                {
                    finish();
                }
                    break;
                case R.id.save:
                {
                    newSongName = songName.getText().toString();
                    AddMusicList task = new AddMusicList();
                    Map<String,String> map = new HashMap<>();
                    map.put("userId",user.getUserId()+"");
                    map.put("listName",newSongName);
                    task.setMap(map);
                    task.execute();
                }
                    break;
            }
        }
    }

    class AddMusicList extends AsyncTask {

        private Map<String,String> map;
        private MyResponse<?> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"musicList/insert", map)).body().string();
                myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<?>>(){}.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                Intent intent = new Intent(NewSongActivity.this,TabActivity.class);
                intent.putExtra("user",str);
                startActivity(intent);
            }else {
                Toast.makeText(NewSongActivity.this,"新建歌单失败",Toast.LENGTH_LONG).show();
            }
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
