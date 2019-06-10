package com.example.user.music.adapter;

import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.user.music.R;
import com.example.user.music.entity.Post;

import java.io.File;
import java.util.List;

public class AttentionDetailAdapter extends BaseAdapter {
    private Context context;
    private List<Post> posts ;//数据类型
    private int itemLayoutId;
    private MediaPlayer mediaPlayer;
    private MediaController controller;
    private ViewHolder viewHolder;

    public AttentionDetailAdapter(Context context, List<Post> posts, int itemLayoutId) {
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
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(
                    itemLayoutId, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.message_content1);
            viewHolder.videoView = convertView.findViewById(R.id.video);
            viewHolder.like = convertView.findViewById(R.id.like);
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
        viewHolder.textView.setText(posts.get(position).getDescription());

        return convertView;
    }
    class ViewHolder{
        private VideoView videoView;
        private TextView textView;
        private ImageView like;
    }
}
