package com.example.user.music.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class UploadVideoActivity extends AppCompatActivity{
    private EditText videoName,videoDescription;
    private TextView videoPath;
    private Button btnUpload;
    private OkHttpClient okHttpClient;
    private int userId;
    private String path;
    private ImageView goBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_video);
        goBack = findViewById(R.id.go_back11);
        videoName = findViewById(R.id.video_name);
        videoDescription = findViewById(R.id.video_description);
        videoPath = findViewById(R.id.video_path);
        btnUpload = findViewById(R.id.uploadVideo);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadVideoActivity.this,UploadMusicActivity.class);
                startActivity(intent);
            }
        });
        //获取数据从输入框
        final String name = videoName.getText().toString();
        final String description = videoDescription.getText().toString();
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",0);
        path = intent.getStringExtra("videoAddress");
        videoPath.setText(path);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideoAsyncTask uploadVideoAsyncTask = new UploadVideoAsyncTask(description,name,userId,path);
                uploadVideoAsyncTask.execute();
                Toast.makeText(UploadVideoActivity.this,"上传成功",Toast.LENGTH_SHORT);
            }
        });
    }
   class UploadVideoAsyncTask extends AsyncTask{
       private String description;
       private String name;
       private int userId;
       private String path;
        @Override
        protected Object doInBackground(Object[] objects) {
            okHttpClient = new OkHttpClient();
            MediaType mediaType = MediaType.parse("media/mp3");
            RequestBody body =RequestBody.create(mediaType, new File(path));
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

}
