package com.example.user.music.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.VideoView;

import com.example.user.music.R;
import com.example.user.music.entity.User;

import java.io.FileNotFoundException;

public class ReverseUserActivity extends AppCompatActivity {
    private ImageView back;
    private EditText etUserName,etUserPwd,etUserPhone;
    private ImageView userImage;
    private Button btn;
    private Button photo;
    private Button select;
    private Button cancel;
    private HeadListener listener;
    private PopupWindow popupWindow;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        back = findViewById(R.id.iv_set_back);
        btn = findViewById(R.id.btn_back);
        etUserName = findViewById(R.id.et_userName1);
        etUserPhone = findViewById(R.id.et_userPhone);
        etUserPwd = findViewById(R.id.et_userPwd);
        userImage = findViewById(R.id.image_user);
        userImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        final int userId = getIntent().getIntExtra("userId",0);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改用户信息
                User user = new User();
                user.setUserName(etUserName.getText().toString());
                user.setUserPassword(etUserPwd.getText().toString());
//                user.setUserHeadImage(userImage.getId());
//                user.setUserPhone(etUserPhone.getText().toString());
//                user.setUserId(userId);
//                UpdateUserAsyncTask updateUserAsyncTask = new UpdateUserAsyncTask(user,getResources().getString(R.string.url));
//                updateUserAsyncTask.execute();
                Intent intent = new Intent(ReverseUserActivity.this,TabActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReverseUserActivity.this,TabActivity.class);
                startActivity(intent);
            }
        });

    }
    private void showPopupWindow() {
        View view=getLayoutInflater().inflate( R.layout.popupwindow,null );

        popupWindow=new PopupWindow( this );
        popupWindow.setContentView( view );
        popupWindow.setWidth( ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight( ViewGroup.LayoutParams.WRAP_CONTENT );

        photo=view.findViewById( R.id.btn_photo );
        select=view.findViewById( R.id.btn_select );
        cancel=view.findViewById( R.id.btn_cancel );
        listener = new HeadListener();
        photo.setOnClickListener( listener);
        select.setOnClickListener( listener );
        cancel.setOnClickListener( listener );

        //后期加弹出效果
//        popupWindow.setAnimationStyle();
        popupWindow.showAsDropDown(userImage);
    }
    private class HeadListener implements  OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_photo:
                {
                    Intent intentCamera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE
                    );
                    startActivityForResult(intentCamera, 1);
                }
                break;
                case R.id.btn_select:
                {
                    Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                    albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(albumIntent, 2);

                }
                break;
                case R.id.btn_cancel:
                {
                    popupWindow.dismiss();
                }
                break;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK)
                {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    userImage.setImageBitmap(bitmap);
                }
                break;
            case 2: {
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    try {
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        userImage.setImageBitmap(bit);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }
}
