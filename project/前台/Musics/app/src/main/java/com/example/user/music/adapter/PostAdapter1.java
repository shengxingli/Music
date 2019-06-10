package com.example.user.music.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.user.music.R;
import com.example.user.music.entity.Post;
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
import okhttp3.Response;

public class PostAdapter1 extends BaseAdapter {
    private Context context;
    private List<Post> posts;//数据类型
    private int itemLayoutId;
    private MediaController controller;
    private ViewHolder viewHolder;

    public PostAdapter1(Context context, List<Post> posts, int itemLayoutId) {
        this.context = context;
        this.posts = posts;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    itemLayoutId, null);
            viewHolder = new ViewHolder();
            viewHolder.userName = convertView.findViewById(R.id.user_name11);
            viewHolder.description = convertView.findViewById(R.id.message_content);
            viewHolder.like = convertView.findViewById(R.id.like);
            viewHolder.videoView = convertView.findViewById(R.id.video1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.videoView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        viewHolder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = posts.get(position).getPostUrl();
                Uri uri = Uri.parse(url);
                viewHolder.videoView.setVideoURI(uri);
                //创建MediaController对象
                controller = new MediaController(context);
                //VideoView与MediaController建立关联
                viewHolder.videoView.setMediaController(controller);
                //让VideoView获取焦点
                viewHolder.videoView.requestFocus();
                viewHolder.videoView.start();
            }
        });
        //传过来的数据要是post的用户名
        viewHolder.userName.setText(posts.get(position).getUserId() + "号用户");
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //传到数据库中count++
                Map<String, String> map = new HashMap<>();
                map.put("postId", posts.get(position).getPostId() + "");
                Like like = new Like();
                like.setMap(map);
                like.execute();
                viewHolder.like.setImageResource(R.drawable.like_red);
            }
        });
        viewHolder.description.setText(posts.get(position).getDescription());
        return convertView;
    }

    class ViewHolder {
        private TextView userName;
        private VideoView videoView;
        private TextView description;
        private ImageView like;
    }

    class Like extends AsyncTask {

        private Map<String, String> map;
        private MyResponse<?> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url + "post/like", map), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<?>>(){}.getType());
                }
            });
            return null;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
