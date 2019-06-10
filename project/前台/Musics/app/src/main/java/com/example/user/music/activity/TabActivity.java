package com.example.user.music.activity;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.music.FragmentPage1;
import com.example.user.music.FragmentPage2;
import com.example.user.music.FragmentPage3;
import com.example.user.music.R;
import com.example.user.music.SelectPopupWindow;
import com.example.user.music.entity.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class TabActivity extends AppCompatActivity {
    private FragmentTabHost tabHost;
    private String[] tabHostTest = {"我的", "音乐部", "发现"};
    private Class[] fragmentArr = {FragmentPage1.class,
            FragmentPage2.class, FragmentPage3.class};
    private ImageView image, search;
    private LinearLayout musicDetail;
    private ImageView musicPlay;
    private ImageView musicImage;
    private TextView musicName;
    private MediaPlayer mediaPlayer;
    private int userId;
    private int i;
    //进度条
    private SeekBar seekBar;
    private Timer timer;
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    private SimpleDateFormat format;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);
        //获取数据userId
        final Intent intent = getIntent();
        User user = new Gson().fromJson(intent.getStringExtra("user"),User.class);
        userId = user.getUserId();
        musicImage = findViewById(R.id.music_image2);
        musicName = findViewById(R.id.music_name2);
        //获取数据通过userId找到正在播放的musicId 通过musicId查找正在播放的歌曲数据
//        SelectMusicMyUserIdAsyncTask musicMyUserIdAsyncTask = new SelectMusicMyUserIdAsyncTask(userId,getResources().getString(R.string.url));
//        musicMyUserIdAsyncTask.execute();
//        Music music = musicMyUserIdAsyncTask.getMusic();
//        musicName.setText(music.getMusicName());
//        String musicImage1 = music.getMusicImage();
//        Glide.with(TabActivity.this).load(musicImage1).into(musicImage);
        //设置图片
        Glide.with(TabActivity.this).load("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2465306030,2173755961&fm=58&bpow=1024&bpoh=1536").into(musicImage);
        image = findViewById(R.id.popup);//弹出菜单
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabActivity.this, SelectPopupWindow.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        musicDetail = findViewById(R.id.music_detail);
        musicPlay = findViewById(R.id.music_plays);
        //跳转到音乐详情界面
//        musicDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TabActivity.this, MusicPlayerActivity.class);
//                intent.putExtra("userId", userId);
//                intent.putExtra("musicName",musicName.getText().toString());
//                mediaPlayer.pause();
//                musicPlay.setImageResource(R.drawable.play_btn);
//                intent.putExtra("current",mediaPlayer.getCurrentPosition());
//                startActivity(intent);
//            }
//        });
        //调转到搜索界面
//        search = findViewById(R.id.search);
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(TabActivity.this, SearchActivity.class);
//                intent1.putExtra("userId",userId);
//                startActivity(intent1);
//            }
//        });
        //音乐播放加进度条
//        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
//        seekBar = (SeekBar) findViewById(R.id.playSeekBar);
//        seekBar.setOnSeekBarChangeListener(new MySeekBar());
//        if (ContextCompat.checkSelfPermission(TabActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(TabActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//        }else {
//            initMediaPlayer();//初始化mediaplayer
//        }
//        i = 0;
//        musicPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (i == 0) {
//                    if (mediaPlayer.isPlaying()) {
//                        mediaPlayer.reset();
//                    }
//                    mediaPlayer.start();//开始播放
//                    mediaPlayer.seekTo(currentPosition);
//                    //监听播放时回调函数
//                    timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        Runnable updateUI = new Runnable() {
//                            @Override
//                            public void run() {
////                              musicCur.setText(format.format(mediaPlayer.getCurrentPosition())+"");
//                            }
//                        };
//
//                        @Override
//                        public void run() {
//                            if (!isSeekBarChanging) {
//                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
//                                runOnUiThread(updateUI);
//                            }
//                        }
//                    }, 0, 50);
//                    musicPlay.setImageResource(R.drawable.pause_btn);
//                    i = 1;
//                } else if (i == 1) {
//                    if (mediaPlayer.isPlaying()) {
//                        mediaPlayer.pause();
//                    }
//                    musicPlay.setImageResource(R.drawable.play_btn);
//                    i = 2;
//                } else if (i == 2) {
//                    if (!mediaPlayer.isPlaying()) {
//                        mediaPlayer.start();
//                    }
//                    musicPlay.setImageResource(R.drawable.pause_btn);
//                    i = 1;
//                }
////                mediaPlayer.setLooping(true);
//            }
//        });
        //初始化FragmentTabHost
        initTabHost();

    }


    private void initTabHost() {
        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup(this,
                getSupportFragmentManager(),
                android.R.id.tabhost);
        for (int i = 0; i < fragmentArr.length; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabHostTest[i])
                    .setIndicator(getTabHostView(i));
            tabHost.addTab(tabSpec, fragmentArr[i], null);
        }
    }

    private View getTabHostView(int index) {
        View view = getLayoutInflater().inflate(R.layout.fragment_tab, null);
        TextView textView = view.findViewById(R.id.tv_text);
        textView.setText(tabHostTest[index]);
        return view;
    }

    private void initMediaPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            try {
                //设置路径
//                mediaPlayer.setDataSource("http://192.168.137.1:8080/music/music/"+musicName.getText().toString()+".mp3");
                mediaPlayer.setDataSource("http://192.168.1.101:8080/music/music/追光者.mp3");
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mediaPlayer.getDuration());
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
                    initMediaPlayer();
                } else {
                    Toast.makeText(TabActivity.this, "denied access", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    @Override protected void onDestroy() {
        super.onDestroy();
        isSeekBarChanging = true;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        } if (timer != null){
            timer.cancel();
            timer = null;
        }
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
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

}

