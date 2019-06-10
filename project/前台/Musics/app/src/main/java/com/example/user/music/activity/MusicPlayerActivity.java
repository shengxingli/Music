package com.example.user.music.activity;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.adapter.MusicAdapter1;
import com.example.user.music.entity.ListMusic;
import com.example.user.music.entity.Music;
import com.example.user.music.entity.MusicList;
import com.example.user.music.entity.User;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MusicPlayerActivity extends AppCompatActivity {
    private ImageView goBack;
    private ImageView addLove;
    private TextView musicName;
    private TextView singerName;
    private ImageView musicBefore;
    private ImageView play;
    private ImageView musicAfter;
    private MusicPlayerListener listener;
    private TextView musicLength, musicCur;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Timer timer;
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    private SimpleDateFormat format;//进度条时间显示
    private int i = 0;//为0时音乐开始播放，为1是暂停播放，为2时恢复播放
    private Music music;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music);
        //获取userId
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);
        String str = intent.getStringExtra("music");
        music = new Gson().fromJson(str,Music.class);
        currentPosition = 0;
        //获取控件
        goBack = findViewById(R.id.go_back6);
        addLove = findViewById(R.id.add_love);
        play = findViewById(R.id.music_play1);
        musicName = findViewById(R.id.music_name1);
        musicBefore = findViewById(R.id.before_music);
        musicAfter = findViewById(R.id.after_music);
        singerName = findViewById(R.id.singer_name1);
        musicLength = findViewById(R.id.music_length);
        musicCur = findViewById(R.id.music_cur);
        seekBar = findViewById(R.id.seekBar);
        //获取music数据

        musicName.setText(music.getMusicName());
        singerName.setText(music.getMusicSingerName());

        //初始化监听事件
        listener = new MusicPlayerListener();
        //给控件添加监听事件
        goBack.setOnClickListener(listener);
        addLove.setOnClickListener(listener);
        musicBefore.setOnClickListener(listener);
        musicAfter.setOnClickListener(listener);
        play.setOnClickListener(listener);

        aaa();
    }

    class MusicPlayerListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.go_back6: {
                    finish();
                }
                break;
                case R.id.add_love: {
                    new Thread(){
                        @Override
                        public void run() {
                            Map<String,String> map = new HashMap<>();
                            map.put("userId",userId+"");
                            map.put("musicId",music.getMusicId()+"");
                            map.put("musicName",music.getMusicName());
                            MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url + "music_list/like", map), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Toast.makeText(MusicPlayerActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }.start();
                }
                break;
                case R.id.before_music: {

                }
                break;
                case R.id.after_music: {

                }
                break;
                case R.id.music_play1: {
                    if (i == 0) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }
                        mediaPlayer.start();//开始播放
                        // 监听播放时回调函数
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            Runnable updateUI = new Runnable() {
                                @Override
                                public void run() {
                                    musicCur.setText(format.format(currentPosition)+"");
                                }
                            };
                            @Override
                            public void run() {
                                if (!isSeekBarChanging) {
                                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                    runOnUiThread(updateUI);
                                }
                            }
                        }, 0, 50);
                        play.setImageResource(R.drawable.playbar_btn_pause);
                        i = 1;
                    } else if (i == 1) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        }
                        play.setImageResource(R.drawable.playbar_btn_play);
                        i = 2;
                    } else if (i == 2) {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                        }
                        play.setImageResource(R.drawable.playbar_btn_pause);
                        i = 1;
                    }
//                mediaPlayer.setLooping(true);
                }
                break;
            }
        }
    }

    private void initMediaPlayer() {
        try {
            mediaPlayer.setDataSource(music.getMusicUrl());
            mediaPlayer.prepare();//让mediaplayer进入准备状态
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    musicLength.setText(format.format(mediaPlayer.getDuration()) + "");
                    musicCur.setText(format.format(currentPosition) + "");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("测试","权限被允许");
                    initMediaPlayer();
                } else {
                    Toast.makeText(MusicPlayerActivity.this, "denied access", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSeekBarChanging = true;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        play.setImageResource(R.drawable.play_btn);
    }

    /*进度条处理*/
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;

        }

        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
            isSeekBarChanging = false;
        }
    }

    public void aaa(){
        format = new SimpleDateFormat("mm:ss");
        seekBar.setOnSeekBarChangeListener(new MySeekBar());
        if (ContextCompat.checkSelfPermission(MusicPlayerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MusicPlayerActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Log.e("测试","播放");
            initMediaPlayer();//初始化mediaplayer
        }

        mediaPlayer.start();//开始播放
        // 监听播放时回调函数
        timer = new Timer();
        timer.schedule(new TimerTask() {
            Runnable updateUI = new Runnable() {
                @Override
                public void run() {
                    musicCur.setText(format.format(mediaPlayer.getCurrentPosition()) + "");
                }
            };

            @Override
            public void run() {
                if (!isSeekBarChanging) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    runOnUiThread(updateUI);
                }
            }
        }, 0, 50);
        play.setImageResource(R.drawable.playbar_btn_pause);
        i = 1;
    }
}
