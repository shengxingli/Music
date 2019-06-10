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
import android.widget.Toast;

import com.example.user.music.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgetPwdNextActivity extends AppCompatActivity{
    private EditText newPwd,reNewPwd;
    private Button btnRePwd;
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pwd_next);
        newPwd = findViewById(R.id.new_pwd);
        reNewPwd = findViewById(R.id.re_new_pwd);
        btnRePwd = findViewById(R.id.btn_rePwd);
        //获取密码和确认密码
        final String pwd = newPwd.getText().toString();
        final String rePwd = reNewPwd.getText().toString();
        //获取手机号，通过手机号修改密码
        Intent intent = getIntent();
        final String phone = intent.getStringExtra("phone");
        btnRePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pwd.equals(rePwd)){
                    Toast.makeText(ForgetPwdNextActivity.this,"密码与确认密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
                }else{
//                    RePwdAsyncTask rePwdAsyncTask = new RePwdAsyncTask();
//                    rePwdAsyncTask.setPwd(pwd);
//                    rePwdAsyncTask.setPhone(phone);
//                    rePwdAsyncTask.execute();
                    Log.e("myMusic",phone+"   "+pwd);
                    Intent intent = new Intent();
                    intent.setClass(ForgetPwdNextActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    class RePwdAsyncTask extends AsyncTask{
        private String pwd;
        private String phone;
        @Override
        protected Object doInBackground(Object[] objects) {
            okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url(getResources().getString(R.string.url)+"rePwd?pwd="+pwd+"&phone="+phone);
            Request request = builder.build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String json = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }
}
