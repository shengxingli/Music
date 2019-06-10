package com.example.user.music.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgetPasswordActivity extends AppCompatActivity{
    private Button send;
    private EditText phone,number;
    private String temp="";
    private String phonenum="";
    private TextView sendnumber;
    private TimeCount1 timeco;
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        final Intent intent=getIntent();
        sendnumber = findViewById(R.id.getCode1);
        send = findViewById(R.id.btn_forget_next);
        phone = findViewById(R.id.et_phone1);
        number = findViewById(R.id.code1);//激活码
        timeco = new TimeCount1(60000, 1000);//时间为120秒
        sendnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<5;i++){
                    int k=(int)(Math.random()*10);
                    temp+=k;
                }
                phonenum=phone.getText().toString().trim();
                SmsManager smsManager=SmsManager.getDefault();
                if(isPhoneNumberValid(phonenum)){
                    PendingIntent mPI=PendingIntent.getBroadcast(ForgetPasswordActivity.this,0,new Intent(),0);
                    smsManager.sendTextMessage(phonenum,null,"您的验证码是："+temp,mPI,null);
                    Toast.makeText(ForgetPasswordActivity.this,"验证码发送成功",Toast.LENGTH_LONG).show();
                    timeco.start();
                }else{
                    Toast.makeText(ForgetPasswordActivity.this,"手机号格式不正确！",Toast.LENGTH_LONG).show();

                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenum = phone.getText().toString().trim();
                if (!isPhoneNumberValid(phonenum)) {
                    Toast.makeText(ForgetPasswordActivity.this, "电话格式不正确", Toast.LENGTH_LONG).show();
                } else if (phonenum.length() == 0) {
                    Toast.makeText(ForgetPasswordActivity.this,
                            "请输入电话号",
                            Toast.LENGTH_LONG).show();
                } else if (number.getText().toString().length() == 0) {
                    Toast.makeText(ForgetPasswordActivity.this,
                            "请输入验证码",
                            Toast.LENGTH_LONG).show();
                } else if (!number.getText().toString().equals(temp)) {
                    Toast.makeText(ForgetPasswordActivity.this,
                            "验证码不正确",
                            Toast.LENGTH_LONG).show();
                } else if (number.getText().toString().equals(temp)) {
                //验证码验证成功,发送手机号到forgetPassword?phone="+phone上确认此手机已注册，已注册返回1，未注册返回0
                    String sPhone = phone.getText().toString();
//                    ForgetPasswordAsyncTask forgetPasswordAsyncTask = new ForgetPasswordAsyncTask();
//                    forgetPasswordAsyncTask.setPhone(sPhone);
//                    forgetPasswordAsyncTask.execute();
//                    int mes = forgetPasswordAsyncTask.getMes();
                    int mes = 1;
                    if(mes == 0){
                        Toast.makeText(ForgetPasswordActivity.this,"此手机号未注册，请直接注册",Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(ForgetPasswordActivity.this, ForgetPwdNextActivity.class);
                        intent.putExtra("phone", sPhone);
                        startActivity(intent);
                    }

                }
            }
        });
    }
    public static boolean isPhoneNumberValid(String mobiles){
        Matcher m=null;
        if(mobiles.trim().length()>0){
            Pattern p= Pattern.compile("^((13[0-9])|(15[0-3])|(15[7-9])|(18[0,5-9]))\\d{8}$");
            m=p.matcher(mobiles);
        }else{
            return false;
        }
        return m.matches();
    }
    public class TimeCount1 extends CountDownTimer {
        public TimeCount1(long millisInFuture,long countDownInterval){
            super(millisInFuture,countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            sendnumber.setText(millisUntilFinished /1000+"秒");
        }
        @Override
        public void onFinish() {
            temp="";
            Toast.makeText(ForgetPasswordActivity.this,"超时，重新发送验证码!",Toast.LENGTH_LONG).show();
        }
    }
    class ForgetPasswordAsyncTask extends AsyncTask{
        private String phone;
        private int mes;
        @Override
        protected Object doInBackground(Object[] objects) {
            okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            builder.url(getResources().getString(R.string.url)+"forgetPassword?phone="+phone);
            Request request = builder.build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String json = response.body().string();
                Gson gson = new Gson();
                mes = gson.fromJson(json,Integer.class);
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

        public int getMes() {
            return mes;
        }

        public void setMes(int mes) {
            this.mes = mes;
        }
    }
}
