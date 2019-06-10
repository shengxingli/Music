package com.example.user.music;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.activity.LoginActivity;
import com.example.user.music.activity.ManageSongActivity;
import com.example.user.music.activity.MyWatchActivity;
import com.example.user.music.activity.NewSongActivity;
import com.example.user.music.activity.ReplenishActivity;
import com.example.user.music.activity.SongDetailActivity;
import com.example.user.music.activity.UploadMusicActivity;
import com.example.user.music.adapter.SongAdapter1;
import com.example.user.music.entity.MusicList;
import com.example.user.music.entity.User;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentPage1 extends Fragment {
    private LinearLayout uploadMusic;//上传音乐
    private LinearLayout myLove;//我喜欢
    private LinearLayout myAttention;//我的关注
    private ImageView addSongSheet;
    private ImageView songSheetManage;//管理歌单
    private ListView listView;
    private User user;
    private String str;
    private SongAdapter1 adapter;
    private Gson gson;
    private List<MusicList> songs;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page1, container, false);
        uploadMusic = view.findViewById(R.id.upload_music);
        myLove = view.findViewById(R.id.my_love);
        myAttention = view.findViewById(R.id.my_attention);
        songSheetManage = view.findViewById(R.id.manage);
        addSongSheet = view.findViewById(R.id.add);
        listView = view.findViewById(R.id.song_sheet);
        gson = new Gson();
        //获取user
        Intent intent = getActivity().getIntent();
        str = intent.getStringExtra("user");
        user = gson.fromJson(str, User.class);
        //获取歌单数据
        initData();
        //上传视频
        uploadMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadMusicActivity.class);
                intent.putExtra("user", str);
                startActivity(intent);
            }
        });
        //新建歌单
        addSongSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), NewSongActivity.class);
                intent1.putExtra("user", str);
                startActivity(intent1);
            }
        });
        //我喜欢跳转到我喜欢界面
        myLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), SongDetailActivity.class);
                for (MusicList list : songs) {
                    if (list.getListName().equals("我喜欢")) {
                        Log.e("测试","我喜欢");
                        intent1.putExtra("list", gson.toJson(list));
                    }
                }
                startActivity(intent1);
            }
        });
        //我的关注跳转到我的关注界面
        myAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //我的关注的用户列表，新建一个activity
                Intent intent1 = new Intent(getActivity(), MyWatchActivity.class);
                intent1.putExtra("userId", user.getUserId());
                startActivity(intent1);
            }
        });
        //管理歌单跳转界面
        songSheetManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), ManageSongActivity.class);
                intent1.putExtra("user", str);
                intent1.putExtra("lists", gson.toJson(songs));
                startActivity(intent1);
            }
        });
        return view;
    }

    public void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", user.getUserId() + "");
        FindMusicListTask task = new FindMusicListTask();
        task.setMap(map);
        task.execute();
    }

    class FindMusicListTask extends AsyncTask {

        private Map<String, String> map;
        private MyResponse<List<MusicList>> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Response response = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url + "musicList/list", map));
                String result = response.body().string();
                myResponse = new Gson().fromJson(result, new TypeToken<MyResponse<List<MusicList>>>() {
                }.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200) {
                songs = myResponse.getData();
                adapter = new SongAdapter1(str, getActivity(), songs, R.layout.song_item1);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "获取用户歌单失败", Toast.LENGTH_LONG).show();
            }
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }

}
