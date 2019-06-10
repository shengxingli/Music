package com.example.user.music.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScanActivity extends AppCompatActivity {

    private ListView listview;
    private TextView textView;
    //记录当前路径下 的所有文件的数组
    private File currentParent;
    //记录当前路径下的所有文件的文件数组
    private File[] currentFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Log.e("myMusic","到达ScanActivity");
        //获取列出全部文件发ListView
        listview = findViewById(R.id.list);
        textView = findViewById(R.id.path);
        //获取系统的SD卡目录
        //路径 我的设定直接找到这个软件的路径
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/mymusic");
        Log.e("myMusic",root.getPath());
        //如果SD卡存在
        if (root.exists()) {
            currentParent = root;
            currentFiles = root.listFiles();//获取root目录下的所有文件
            //使用当前目录下的全部文件，文件夹来填充ListView
            inflateListView(currentFiles);
        }//if
        //为ListView的列表项的单击事件绑定监视器
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //用户点击了文件，则调用手机已安装软件操作该文件
                if (currentFiles[position].isFile()) {
                    File file = new File(currentFiles[position].getPath());
                    String end = file.getName().substring(file.getName().lastIndexOf(".") +
                            1,file.getName().length()).toLowerCase();
                    if(end.equals("mp3")){
                        Intent intent = new Intent(ScanActivity.this,UploadVideoActivity1.class);
                        intent.putExtra("path",currentFiles[position].getPath());
                        setResult(999,intent);
                        finish();
                    }else if(end.equals("mp4")){
                        Intent intent = new Intent(ScanActivity.this,UploadAudioActivity1.class);
                        intent.putExtra("path",currentFiles[position].getPath());
                        setResult(888,intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getBaseContext(),"必须是map3格式或者mp4格式",Toast.LENGTH_LONG);
                    }
                } else {
                    //获取currentFiles[position]路径下的所有文件
                    File[] tmp = currentFiles[position].listFiles();
                    if (tmp == null || tmp.length == 0) {
                        Toast.makeText(ScanActivity.this, "空文件夹!", Toast.LENGTH_SHORT).show();
                    }//if
                    else {
                        //获取用户单击的列表项对应的文件夹，设为当前的父文件夹
                        currentParent = currentFiles[position];
                        //保存当前文件夹内的全部问价和文件夹
                        currentFiles = tmp;
                        inflateListView(currentFiles);
                    }//else
                }//else
            }//onItemClick
        });
        Button parent = (Button) findViewById(R.id.parent);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onbey();
            }//onClick
        });
    }//onCraete

    //返回上层菜单
    private void onbey() {
        try {
            if (!"/mnt".equals(currentParent.getCanonicalPath())) {
                //获取上一层目录
                currentParent = currentParent.getParentFile();
                //列出当前目录下的所有文件
                currentFiles = currentParent.listFiles();
                //再次更新ListView
                inflateListView(currentFiles);
            } else {
                new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("提示")
                        .setMessage("确定要退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }

    //更新列表
    private void inflateListView(File[] files) {
        if (files.length == 0)
            Toast.makeText(ScanActivity.this, "sd卡不存在", Toast.LENGTH_SHORT).show();
        else {
            //创建一个List集合,List集合的元素是Map
            List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < files.length; i++) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                //如果当前File是文件夹，使用folder图标；否则使用file图标
                if (files[i].isDirectory()) listItem.put("icon", R.mipmap.folder1);
                    //else if(files[i].isFi)
                else listItem.put("icon", R.mipmap.file1);
                listItem.put("fileName", files[i].getName());
                //添加List项
                listItems.add(listItem);
            }//for
            //创建一个SimpleAdapter
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.layout_scan_item, new String[]{"icon", "fileName"},
                    new int[]{R.id.icon, R.id.filename});
            //位ListView设置Adpter
            listview.setAdapter(simpleAdapter);
            try {
                textView.setText("当前路径为：" + currentParent.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }//catch
        }//esle
    }//inflateListView

    //监听手机自带返回键
    public void onBackPressed() {
        onbey();
    }
}
