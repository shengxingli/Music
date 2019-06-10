package com.example.user.music;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.user.music.activity.MusicPlayerActivity;
import com.example.user.music.activity.ReverseUserActivity;

public class SelectPopupWindow extends Activity{

    private TextView attire,message,setting,cancelSoftware;
    private int userId;
    private Click listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        //获取userId
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId",userId);
        attire = findViewById(R.id.attire);
        message = findViewById(R.id.messageCenter);
        setting = findViewById(R.id.setting);
        cancelSoftware = findViewById(R.id.cancelSoftware);
        listener = new Click();
        attire.setOnClickListener(listener);
        message.setOnClickListener(listener);
        setting.setOnClickListener(listener);
        cancelSoftware.setOnClickListener(listener);
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
    class Click implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.attire://个性装扮
//                    Intent intent = new Intent();
//                    intent.setClass(SelectPopupWindow.this, MusicPlayerActivity.class);
//                    startActivity(intent);
                    break;
                case R.id.cancelSoftware://退出软件
                    finish();
                    break;
                case R.id.setting://修改用户信息
                    Intent i = new Intent(SelectPopupWindow.this, ReverseUserActivity.class);
                    i.putExtra("userId",userId);
                    startActivity(i);

                    break;
                case R.id.messageCenter://信息中心
                    break;
            }
        }
    }

}
