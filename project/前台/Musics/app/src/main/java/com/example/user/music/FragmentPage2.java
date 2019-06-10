package com.example.user.music;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.music.activity.MusicPlayerActivity;
import com.example.user.music.activity.RankListActivity;
import com.example.user.music.activity.SingerActivity;
import com.example.user.music.activity.SongActivity;
import com.example.user.music.adapter.MusicAdapter;
import com.example.user.music.adapter.SongAdapter1;
import com.example.user.music.entity.Music;
import com.example.user.music.entity.MusicList;
import com.example.user.music.entity.User;
import com.example.user.music.image.GlideImageLoader;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentPage2 extends Fragment {
    private LinearLayout singer,song,paihang;
    private String str;
    private User user;
    private ListView listView;
    private List<Music> music;
    private MusicAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page2,container,false);
        singer = view.findViewById(R.id.singer);
        song = view.findViewById(R.id.song);
        paihang = view.findViewById(R.id.paihang);
        Intent intent = getActivity().getIntent();
        str = intent.getStringExtra("user");
        user = new Gson().fromJson(str,User.class);
        final List images = new ArrayList();
        images.add(R.drawable.first);
        images.add(R.drawable.second);
        images.add(R.drawable.third);
        images.add(R.drawable.fourth);
        images.add(R.drawable.five);
        images.add(R.drawable.six);
        images.add(R.drawable.seven);
        Banner banner = view.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                switch (position){
                    case 1: {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MusicPlayerActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 2:{
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MusicPlayerActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 3:{
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MusicPlayerActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 4:{
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MusicPlayerActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 5:{
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MusicPlayerActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 6:{
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MusicPlayerActivity.class);
                        startActivity(intent);
                    }
                        break;
                    case 7:{
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MusicPlayerActivity.class);
                        startActivity(intent);
                    }
                        break;
                }
            }
        });
        singer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SingerActivity.class);
                intent.putExtra("userId",user.getUserId());
                startActivity(intent);
            }
        });
        song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SongActivity.class);
                intent.putExtra("user",str);
                startActivity(intent);
            }
        });
        paihang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RankListActivity.class);
                intent.putExtra("userId",user.getUserId());
                startActivity(intent);
            }
        });
        listView = view.findViewById(R.id.recommend_music);
        initData();
        //获取推荐的歌单数据   随便查5,6条
        return view;
    }
    public void initData(){
        FindMusicsTask task = new FindMusicsTask();
        task.execute();
    }

    class FindMusicsTask extends AsyncTask {

        private MyResponse<List<Music>> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.rUrl+"api/index/hotMusic", null)).body().string();
                myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<List<Music>>>(){}.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                music  = myResponse.getData();
                adapter = new MusicAdapter(getActivity(),music,R.layout.music_item,user.getUserId());
                listView.setAdapter(adapter);
            }else {
                Toast.makeText(getContext(),"获取推荐音乐失败",Toast.LENGTH_LONG).show();
            }
        }

    }
}
