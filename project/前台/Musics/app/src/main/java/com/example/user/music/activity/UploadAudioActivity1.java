package com.example.user.music.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.entity.User;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadAudioActivity1 extends AppCompatActivity{
    private EditText audioName,audioDescription;
    private TextView audioPath;
    private Button btnUpload,scan;
    private User user;
    private String path;
    private ImageView goBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_audio1);
        goBack = findViewById(R.id.go_back10);
        audioName = findViewById(R.id.audio_name1);
        audioDescription = findViewById(R.id.audio_description1);
        audioPath = findViewById(R.id.audio_path1);
        btnUpload = findViewById(R.id.uploadAudio1);
        scan = findViewById(R.id.scan1);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadAudioActivity1.this,UploadMusicActivity.class);
                startActivity(intent);
            }
        });
        //获取数据从输入框
        final String name = audioName.getText().toString();
        final String description = audioDescription.getText().toString();
        final Intent intent = getIntent();
        String str = intent.getStringExtra("user");
        user = new Gson().fromJson(str,User.class);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadAudioAsyncTask uploadAudioAsyncTask = new UploadAudioAsyncTask(
                        description,name,user.getUserId(),path);
                uploadAudioAsyncTask.execute();
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("myMusic","ScanActivity");
                Intent intent1 = new Intent(UploadAudioActivity1.this,ScanActivity.class);
                startActivityForResult(intent1,888);
            }
        });
    }
    class UploadAudioAsyncTask extends AsyncTask {
        private String description;
        private String name;
        private int userId;
        private String path;
        private MyResponse<String> myResponse;
        @Override
        protected Object doInBackground(Object[] objects) {
            Map<String,String> map = new HashMap<>();
            map.put("postName",name);
            map.put("description",description);
            map.put("userId",userId+"");
            try {
                String result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"post/upload", map)).body().string();
                myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<String>>(){}.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public UploadAudioAsyncTask(String description, String name, int userId, String path) {
            this.description = description;
            this.name = name;
            this.userId = userId;
            this.path = path;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                UploadManager uploadManager = new UploadManager();
                uploadManager.put(new File(path), name, myResponse.getData(), new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        // 完成之后干点啥,info.isOk()true 上传成功
                        finish();
                    }
                },null);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 888 && resultCode == 888) {
            path = data.getStringExtra("path");
            audioPath.setText(path);
        }
    }
}
