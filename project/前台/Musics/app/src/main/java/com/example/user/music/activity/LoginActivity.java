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
import android.widget.TextView;

import com.example.user.music.R;
import com.example.user.music.entity.MusicList;
import com.example.user.music.entity.User;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText userName;
    private EditText password;
    private TextView forgetPassword;
    private TextView register;
    private Button login;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userName = findViewById(R.id.et_userName);
        password = findViewById(R.id.et_password);
        forgetPassword = findViewById(R.id.forget_password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.btn_login);
        //跳转注册界面
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString();
                String pwd = password.getText().toString();
                //传用户名和密码到eclipse上，判断是否能登录成功，不成功，返回userId=0，成功，放回用户名对应的userId
                LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
                loginAsyncTask.setUsername(name);
                loginAsyncTask.setPassword(pwd);
                loginAsyncTask.execute();

            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    //登录的异步任务
    class LoginAsyncTask extends AsyncTask<String, Void, MyResponse<User>> {
        private String username;
        private String password;
        private String result;

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        protected MyResponse<User> doInBackground(String... strings) {
            Map<String,String> map = new HashMap();
            map.put("userName",username);
            map.put("password",password);
            try {
                Response response = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"user/login", map));
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MyResponse<User> myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<User>>(){}.getType());
            Log.e("测试",result);
            return myResponse;
        }

        @Override
        protected void onPostExecute(MyResponse<User> myResponse) {
            Log.e("测试",myResponse.getCode()+"");
            if(myResponse.getCode() == 200 ){
                Intent intent = new Intent(LoginActivity.this,TabActivity.class);
                intent.putExtra("user",new Gson().toJson(myResponse.getData()));
                startActivity(intent);
            }
        }
    }
}
