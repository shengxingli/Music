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

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadVideoActivity1 extends AppCompatActivity{
    private EditText videoName,videoDescription;
    private TextView videoPath;
    private Button btnUpload,scan;
    private OkHttpClient okHttpClient;
    private int userId;
    private String path;
    private ImageView goBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_video1);
        goBack = findViewById(R.id.go_back12);
        videoName = findViewById(R.id.video_name1);
        videoDescription = findViewById(R.id.video_description1);
        videoPath = findViewById(R.id.video_path1);
        btnUpload = findViewById(R.id.uploadVideo1);
        scan = findViewById(R.id.scan);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadVideoActivity1.this,UploadMusicActivity.class);
                startActivity(intent);
            }
        });
        //获取数据从输入框
        final String name = videoName.getText().toString();
        final String description = videoDescription.getText().toString();
        final Intent intent = getIntent();
        userId = intent.getIntExtra("userId",0);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideoAsyncTask uploadVideoAsyncTask = new UploadVideoAsyncTask(description,name,userId,path);
                uploadVideoAsyncTask.execute();
                Toast.makeText(UploadVideoActivity1.this,"上传成功",Toast.LENGTH_SHORT);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("myMusic","ScanActivity");
                Intent intent1 = new Intent(UploadVideoActivity1.this,ScanActivity.class);
                startActivityForResult(intent1,999);
            }
        });
    }
    class UploadVideoAsyncTask extends AsyncTask {
        private String description;
        private String name;
        private int userId;
        private String path;
        @Override
        protected Object doInBackground(Object[] objects) {
            okHttpClient = new OkHttpClient();
            MediaType mediaType = MediaType.parse("media/mp3");
            RequestBody body = RequestBody.create(mediaType, new File(path));
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.url)+"saveVideo?description=" + description + "&name=" + name + "&userId=" + userId)
                    .post(body)
                    .build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        public UploadVideoAsyncTask(String description, String name, int userId, String path) {
            this.description = description;
            this.name = name;
            this.userId = userId;
            this.path = path;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 999 && resultCode == 999) {
            path = data.getStringExtra("path");
            videoPath.setText(path);
        }
    }
}
