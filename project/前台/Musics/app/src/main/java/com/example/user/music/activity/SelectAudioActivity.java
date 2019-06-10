package com.example.user.music.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.user.music.R;
import com.example.user.music.entity.User;
import com.google.gson.Gson;

import java.io.File;

public class SelectAudioActivity extends AppCompatActivity{
    //视频播放
    private MediaController controller;
    private ImageView goBack;
    //录制视频
    private VideoView vVideo;
    private Button startAudio, stopAudio,uploadAudio;
    private String path;
    private String str;
    private User user;
    private boolean isRecording;
    private MediaRecorder mediaRecorder;
    private String fileName;
    private File file;
    private Camera camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_audio);
        startAudio = findViewById(R.id.startAudio);
        goBack = findViewById(R.id.go_back14);
        stopAudio = findViewById(R.id.stopAudio);
        uploadAudio = findViewById(R.id.uploadAudio);
        Intent intent = getIntent();
        str = intent.getStringExtra("user");
        user = new Gson().fromJson(str,User.class);
        fileName = Math.round(Math.random()*10000)+"";
        path = "/sdcard/"+fileName+".mp4";
        file = new File(path);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mediaRecorder = new MediaRecorder();
        stopAudio.setEnabled(false);
        //获取userId

        stopAudio.setOnClickListener(click);
        startAudio.setOnClickListener(click);
        uploadAudio.setOnClickListener(click);
        vVideo = findViewById(R.id.vVideo);
        vVideo.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startAudio:
                    startAudio();
                    break;
                case R.id.stopAudio:
                    stop();
                    //加载指定的视频文件
                    path = "/sdcard/"+fileName+".mp4";
                    vVideo.setVideoPath(path);
                    //创建MediaController对象
                    controller = new MediaController(SelectAudioActivity.this);
                    //VideoView与MediaController建立关联
                    vVideo.setMediaController(controller);
                    //让VideoView获取焦点
                    vVideo.requestFocus();
                    break;
                case R.id.uploadAudio:
                    Intent intent = new Intent(SelectAudioActivity.this,UploadAudioActivity.class);
                    intent.putExtra("audioAddress",path);
                    intent.putExtra("name",fileName);
                    intent.putExtra("user",str);
                    startActivity(intent);
                    break;
            }
        }
    };
    //开始录制
    protected void startAudio() {
        try {
            if (file.exists()) {
                // 如果文件存在，删除它，演示代码保证设备上只有一个录音文件
                file.delete();
            }
            mediaRecorder = new MediaRecorder();
            mediaRecorder.reset();
            mediaRecorder.setOrientationHint(180);
            if (camera != null) {
                camera.setDisplayOrientation(90);//摄像图旋转90度
                camera.unlock();
                mediaRecorder.setCamera(camera);
            }
            // 设置音频录入源
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置视频图像的录入源
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setOrientationHint(90);//视频旋转90度
            // 设置录入媒体的输出格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // 设置音频的编码格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            // 设置视频的编码格式
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            // 设置视频的采样率，每秒4帧
            mediaRecorder.setVideoFrameRate(4);
            // 设置录制视频文件的输出路径
            mediaRecorder.setOutputFile(file.getAbsolutePath());
//            mediaRecorder.setOutputFile(sharedFileUri.getPath());
            // 设置捕获视频图像的预览界面
            mediaRecorder.setPreviewDisplay(vVideo.getHolder().getSurface());
            mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    // 发生错误，停止录制
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    isRecording=false;
                    startAudio.setEnabled(true);
                    stopAudio.setEnabled(false);
                    Toast.makeText(SelectAudioActivity.this, "录制出错", Toast.LENGTH_SHORT).show();
                }
            });
            // 准备、开始
            mediaRecorder.prepare();
            mediaRecorder.start();
            startAudio.setEnabled(false);
            stopAudio.setEnabled(true);
            isRecording = true;
            Toast.makeText(SelectAudioActivity.this, "开始录像", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //停止录制
    protected void stop() {
        if (isRecording) {
            // 如果正在录制，停止并释放资源
            camera.lock();
            mediaRecorder.stop();
            mediaRecorder.release();
            camera.release();
            mediaRecorder = null;
            isRecording=false;
            startAudio.setEnabled(true);
            stopAudio.setEnabled(false);
            Toast.makeText(SelectAudioActivity.this, "停止录像，并保存文件", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            }
            super.onDestroy();
    }

}
