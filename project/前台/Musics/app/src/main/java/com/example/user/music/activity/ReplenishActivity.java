package com.example.user.music.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.entity.User;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//补充信息
public class ReplenishActivity extends AppCompatActivity {
    private EditText userName;
    private EditText password;
    private Button submitData;
    private ImageView head;
    private Button photo;
    private Button select;
    private Button cancel;
    private HeadListener listener;
    private PopupWindow popupWindow;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mime_message);
//        Intent intent = getIntent();
//        int userId = intent.getIntExtra("userId",0);
//        Log.e("hida",userId+"");
        userName = findViewById(R.id.et_ruserName);
        password = findViewById(R.id.et_rpassword);
        submitData = findViewById(R.id.submit_data);
        head = findViewById(R.id.iv_rhead);
//        head.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupWindow();
//            }
//        });
        submitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString();
                String pwd = password.getText().toString();
                Intent intent = getIntent();
                String tel = intent.getStringExtra("userTel");
                //提交User信息给数据库，完成信息的完善
                Map<String,String> map = new HashMap<>();
                map.put("userName",name);
                map.put("userTel",tel);
                map.put("userPassword",pwd);
                ReplenishAsyncTask replenishAsyncTask = new ReplenishAsyncTask();
                replenishAsyncTask.setMap(map);
                replenishAsyncTask.execute();
            }
        });
    }

    class ReplenishAsyncTask extends AsyncTask {

        private Map<String,String> map;
        private MyResponse<User> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"user/update", map)).body().string();
                myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<User>>(){}.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                Intent intent = new Intent();
                intent.setClass(ReplenishActivity.this, LoginActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(getBaseContext(),"补充信息失败",Toast.LENGTH_LONG).show();
            }
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }

    private void showPopupWindow() {
        View view = getLayoutInflater().inflate(R.layout.popupwindow, null);

        popupWindow = new PopupWindow(this);
        popupWindow.setContentView(view);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        photo = view.findViewById(R.id.btn_photo);
        select = view.findViewById(R.id.btn_select);
        cancel = view.findViewById(R.id.btn_cancel);
        listener = new HeadListener();
        photo.setOnClickListener(listener);
        select.setOnClickListener(listener);
        cancel.setOnClickListener(listener);

        //后期加弹出效果
//        popupWindow.setAnimationStyle();
        popupWindow.showAsDropDown(head);
    }

    private class HeadListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_photo: {
//                    File file = new File(getExternalCacheDir(),"head.jpg");
//                    if(file.exists()){
//                        file.delete();
//                    }
//                    try {
//                        file.createNewFile();
//                        if(Build.VERSION.SDK_INT >= 24){
//                            imageUri = FileProvider.getUriForFile(ReplenishActivity.this,"com.example.user.music.fileprovider",file);
//                        }else{
//                            imageUri = Uri.fromFile(file);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                    startActivityForResult(intent, 1);
                    Intent intentCamera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE
                    );
                    startActivityForResult(intentCamera, 1);
                }
                break;
                case R.id.btn_select: {
//                    if (ContextCompat.checkSelfPermission(ReplenishActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(ReplenishActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                    } else {
//                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//                        intent.setType("image/*");
//                        startActivityForResult(intent, 2);
//                    }
                    Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                    albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(albumIntent, 2);

                }
                break;
                case R.id.btn_cancel: {
                    popupWindow.dismiss();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
//                    try {
//                        Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                        head.setImageBitmap(bm);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    head.setImageBitmap(bitmap);
                }
                break;
            case 2: {
                if (resultCode == RESULT_OK) {
//                    if (Build.VERSION.SDK_INT >= 19) {
//                        //4.4及以上的系统使用这个方法处理图片；
//                        handleImageOnKitKat(data);
//                    } else {
//                        handleImageBeforeKitKat(data);// 4.4及以下的系统使用这个方法处理图片
//                    }
                    Uri imageUri = data.getData();
                    try {
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        head.setImageBitmap(bit);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void handleImageOnKitKat(Intent data) {
//        String imagePath = null; Uri uri = data.getData();
//        if (DocumentsContract.isDocumentUri(this, uri))
//        { //如果document类型的Uri,则通过document来处理
//            String docID = DocumentsContract.getDocumentId(uri);
//            if ("com.android.providers.media.documents".equals(uri.getAuthority()))
//            { String id = docID.split(":")[1];
//                //解析出数字格式的id
//                String selection = MediaStore.Images.Media._ID + "=" + id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
//            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority()))
//            {
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/piblic_downloads"), Long.valueOf(docID));
//                imagePath = getImagePath(contentUri, null);
//            }
//        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            //如果是content类型的uri，则使用普通方式使用
//            imagePath = getImagePath(uri, null);
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            //如果是file类型的uri，直接获取路径即可
//            imagePath = uri.getPath();
//        }
//        displayImage(imagePath);
//    }
//
//    private void handleImageBeforeKitKat(Intent data){
//        Uri uri = data.getData();
//        String imagePath = getImagePath(uri, null);
//        displayImage(imagePath);
//    }
//
//    private void displayImage(String imagePath) {
//        if (imagePath != null) {
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            head.setImageBitmap(bitmap);
//        } else {
//            Toast.makeText(this, "获取照片失败", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private String getImagePath(Uri uri, String selection) {
//        String path = null; //通过Uri和selection来获取真实的图片路径
//        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            } cursor.close();
//        }
//        return path;
//    }
}
