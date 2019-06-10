package com.example.user.music;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.adapter.PostAdapter;
import com.example.user.music.adapter.PostAdapter1;
import com.example.user.music.adapter.SongAdapter1;
import com.example.user.music.entity.MusicList;
import com.example.user.music.entity.Post;
import com.example.user.music.entity.User;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentPage3 extends Fragment {
    private ListView listView;
    private TextView allPost;
    private TextView myAttention;
    private List<Post> posts;
    private PostAdapter adapter;
    private PostAdapter1 adapter1;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page3, container, false);
        allPost = view.findViewById(R.id.all_post);
        myAttention = view.findViewById(R.id.my_watch_post);
        listView = view.findViewById(R.id.find_item);
        Intent intent = getActivity().getIntent();
        String str = intent.getStringExtra("user");
        user = new Gson().fromJson(str,User.class);
        initData();
//        allPost.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//                initData();
//            }
//        });
//        myAttention.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//                initWatchData();
//            }
//        });
        return view;
    }

    public void initData() {
        FindPostsTask task = new FindPostsTask();
        task.execute();
    }

    class FindPostsTask extends AsyncTask {

        private MyResponse<List<Post>> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url + "post/list", null)).body().string();
                myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<List<Post>>>(){}.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200) {
                posts = myResponse.getData();
                adapter = new PostAdapter(getActivity(), posts, R.layout.layout_post_item,
                        user.getUserId());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "获取用户歌单失败", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void initWatchData() {
        Map<String,String> map = new HashMap<>();
        map.put("userId",user.getUserId()+"");
        FindWatchPostsTask task = new FindWatchPostsTask();
        task.setMap(map);
        task.execute();
    }

    class FindWatchPostsTask extends AsyncTask {

        private Map<String,String> map;
        private MyResponse<List<Post>> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url + "post/watchList", map)).body().string();
                myResponse = new Gson().fromJson(result, MyResponse.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200) {
                posts = myResponse.getData();
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "获取用户歌单失败", Toast.LENGTH_LONG).show();
            }
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
