package com.example.user.music.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.user.music.R;
import com.example.user.music.activity.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private ImageView image;
    private static final long DELAY = 3500;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        image = findViewById(R.id.frame);
        AnimationDrawable animDrawable = new AnimationDrawable();
        //添加帧
        for(int i = 1; i < 4; i++){
            id = getResources().getIdentifier(
                    "p" + i,
                    "mipmap",
                    getPackageName()
            );
            Drawable d = getResources().getDrawable(id, null);
            animDrawable.addFrame(d, 1000);
        }
        //设置循环播放
        animDrawable.setOneShot(true);
        //2. 关联动画对象和图像控件
        image.setImageDrawable(animDrawable);
        //3. 开始播放动画
        if (animDrawable.isRunning()) {
            animDrawable.stop();
        } else {
            animDrawable.start();
        }
        final Intent localIntent=new Intent(this,LoginActivity.class);//你要转向的Activity
//        final Intent localIntent=new Intent(this,MusicPlayerAcvitity.class);
        Timer timer=new Timer();
        TimerTask tast = new TimerTask() {
            @Override
            public void run() {
                startActivity(localIntent);//执行
            }
        };
        timer.schedule(tast,DELAY);//3秒后

    }

}
