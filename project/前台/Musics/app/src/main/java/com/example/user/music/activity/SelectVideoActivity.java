package com.example.user.music.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.music.R;

import java.io.File;
import java.io.IOException;

public class SelectVideoActivity extends AppCompatActivity {
    private Button startVideo, stopVideo,playVideo,stopPlayVideo,uploadVideo,pauseVideo;
    private MediaRecorder mediaRecorder;
    private boolean isRecording;
    private MediaPlayer mediaPlayer;
    private File file = new File("/sdcard/mediarecorder.amr");
    private int userId;
    private ImageView goBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_video);
        mediaPlayer = new MediaPlayer();
        goBack = findViewById(R.id.go_back13);
        mediaRecorder = new MediaRecorder();
        startVideo = findViewById(R.id.startVideo);
        stopVideo = findViewById(R.id.stopVideo);
        playVideo = findViewById(R.id.playVideo);
        stopPlayVideo = findViewById(R.id.stopPlayVideo);
        uploadVideo = findViewById(R.id.uploadVideo);
        pauseVideo = findViewById(R.id.pauseVideo);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectVideoActivity.this,UploadMusicActivity.class);
                startActivity(intent);
            }
        });
        //获取userId
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",0);
        stopVideo.setOnClickListener(click);
        startVideo.setOnClickListener(click);
        playVideo.setOnClickListener(click);
        stopPlayVideo.setOnClickListener(click);
        uploadVideo.setOnClickListener(click);
        pauseVideo.setOnClickListener(click);
    }
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startVideo:
                    start();
                    break;
                case R.id.stopVideo:
                    stop();
                    break;
                case R.id.playVideo:
                    try {
                        // 重置MediaPlayer对象，使之处于空闲状态
                        mediaPlayer.reset();
                        // 设置要播放的文件的路径
                        mediaPlayer.setDataSource(String.valueOf(file));//已通过DDMS把mp3文件导入sdcard
                        // 准备播放
                        mediaPlayer.prepare();
                        // 开始播放
                        mediaPlayer.start();}
                    catch (IOException e) {}
                    break;
                case R.id.stopPlayVideo:
                    // 是否正在播放
                    if (mediaPlayer.isPlaying())
                    {
                        //重置MediaPlayer到初始状态
                        mediaPlayer.reset();
                        mediaPlayer.release();
                    }
                    break;
                case R.id.uploadVideo:
                    Intent intent = new Intent(SelectVideoActivity.this,UploadVideoActivity.class);
                    intent.putExtra("videoAddress","/sdcard/mediarecorder.amr");
                    intent.putExtra("userId",userId);
                    startActivity(intent);
                    break;
                case R.id.pauseVideo:
                    if (mediaPlayer.isPlaying()) {
                        // 暂停
                        mediaPlayer.pause();
                    } else {
                        // 开始播放
                        mediaPlayer.start();
                    }
                    break;
            }
        }
    };
    /**
     * 开始录音
     */
    protected void start() {
        try {
            File file = new File("/sdcard/mediarecorder.amr");
            if (file.exists()) {
                // 如果文件存在，删除它，演示代码保证设备上只有一个录音文件
                file.delete();
            }
            mediaRecorder = new MediaRecorder();
            // 设置音频录入源
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置录制音频的输出格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // 设置音频的编码格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // 设置录制音频文件输出文件路径
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    // 发生错误，停止录制
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    isRecording = false;
                    startVideo.setEnabled(true);
                    stopVideo.setEnabled(false);
                    Toast.makeText(SelectVideoActivity.this, "录音发生错误", Toast.LENGTH_SHORT).show();
                }
            });
            // 准备、开始
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording=true;
            startVideo.setEnabled(false);
            stopVideo.setEnabled(true);
            Toast.makeText(SelectVideoActivity.this, "开始录音", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 录音结束
     **/
    protected void stop() {
        if (isRecording) {
            // 如果正在录音，停止并释放资源
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording=false;
            startVideo.setEnabled(true);
            stopVideo.setEnabled(false);
            Toast.makeText(SelectVideoActivity.this, "录音结束", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        if (isRecording) {
            // 如果正在录音，停止并释放资源
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        super.onDestroy();
    }
}

