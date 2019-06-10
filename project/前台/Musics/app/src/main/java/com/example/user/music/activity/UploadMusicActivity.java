package com.example.user.music.activity;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.user.music.R;

public class UploadMusicActivity extends AppCompatActivity {
    private Button btnVideo;
    private Button selectAudio;
    private UploadListener listener;
    private String user;
    private ImageView goBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_musics);
        goBack = findViewById(R.id.go_back8);
        btnVideo = findViewById(R.id.btn_video);
        selectAudio = findViewById(R.id.btn_select_audio);
        //获取userId
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        listener = new UploadListener();
        btnVideo.setOnClickListener(listener);
        selectAudio.setOnClickListener(listener);
        goBack.setOnClickListener(listener);
    }
//如果可以最后再加上我的上传
    private class UploadListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_video: {
                    Intent intent = new Intent(UploadMusicActivity.this,UploadAudioActivity1.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
                break;
                case R.id.btn_select_audio: {
                    //录制视频
                    Intent intent = new Intent(UploadMusicActivity.this,SelectAudioActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
                break;
                case R.id.go_back8:
                {
                    finish();
                }
                    break;

            }
        }
    }


}
