package com.example.user.music.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.entity.User;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity {
    private Button send;
    private EditText phone,number;
    private String temp="";
    private String phonenum="";
    private TextView sendnumber;
    private TimeCount timeco;
    private OkHttpClient okHttpClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        final Intent intent=getIntent();
        sendnumber = findViewById(R.id.getCode);
        send = findViewById(R.id.btn_register);
        phone = findViewById(R.id.et_phone);
        number = findViewById(R.id.code);//激活码
        timeco = new TimeCount(60000, 1000);//时间为120秒
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
                    Log.e("验证码",temp);
//                    PendingIntent mPI=PendingIntent.getBroadcast(RegisterActivity.this,0,new Intent(),0);
//                    smsManager.sendTextMessage(phonenum,null,"您的验证码是："+temp,mPI,null);
                    Toast.makeText(RegisterActivity.this,"验证码发送成功",Toast.LENGTH_LONG).show();
                    timeco.start();
                }else{
                    Toast.makeText(RegisterActivity.this,"手机号格式不正确！",Toast.LENGTH_LONG).show();
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenum=phone.getText().toString().trim();
                if(!isPhoneNumberValid(phonenum)){
                    Toast.makeText(RegisterActivity.this,"电话格式不正确",Toast.LENGTH_LONG).show();
                }else if(phonenum.length()==0){
                    Toast.makeText(RegisterActivity.this,
                            "请输入电话号",
                            Toast.LENGTH_LONG).show();
                }else if(number.getText().toString().length()==0){
                    Toast.makeText(RegisterActivity.this,
                            "请输入验证码",
                            Toast.LENGTH_LONG).show();
                }else if(!number.getText().toString().equals(temp)){
                    Toast.makeText(RegisterActivity.this,
                            "验证码不正确",
                            Toast.LENGTH_LONG).show();
                }else if(number.getText().toString().equals(temp)){
                    //验证码验证成功之后，将手机号添加到数据库里，若数据库以存在该手机号，则返回userId=0，不存在，返回新建立的userId
                    String sPhone = phone.getText().toString();
                    RegisterPhoneAsyncTask registerPhoneAsyncTask = new RegisterPhoneAsyncTask();
                    registerPhoneAsyncTask.setPhone(sPhone);
                    Log.e("测试","测试");
                    registerPhoneAsyncTask.execute();
                }

            }
        });
    }
    class RegisterPhoneAsyncTask extends AsyncTask {
        private String phone;
        private MyResponse<?> myResponse;
        @Override
        protected Object doInBackground(Object[] objects) {
            Map<String,String> map = new HashMap();
            map.put("userTel",phone);
            String result = null;
            try {
                result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"user/register", map)).body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<?>>(){}.getType());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(myResponse.getCode() == 200 ){
                Log.e("测试","测试");
                Intent intent = new Intent(RegisterActivity.this,ReplenishActivity.class);
                intent.putExtra("userTel",phone);
                startActivity(intent);
            }else{
                Toast.makeText(RegisterActivity.this,"此手机号已注册，请直接登录",Toast.LENGTH_SHORT).show();
            }
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

    }
    public static boolean isPhoneNumberValid(String mobiles){
        Matcher m=null;
        if(mobiles.trim().length()>0){
            Pattern p= Pattern.compile("^((13[0-9])|(15[0-3])|(15[7-9])|(18[0-9]))\\d{8}$");
            m=p.matcher(mobiles);
        }else{
            return false;
        }
        return m.matches();
    }
    public class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture,long countDownInterval){
            super(millisInFuture,countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            sendnumber.setText(millisUntilFinished /1000+"秒");
        }

        @Override
        public void onFinish() {
            temp="";
            Toast.makeText(RegisterActivity.this,"超时，重新发送验证码!",Toast.LENGTH_LONG).show();
        }
    }
}
