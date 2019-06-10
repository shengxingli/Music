package com.example.user.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.music.R;
import com.example.user.music.activity.SongDetailActivity;
import com.example.user.music.entity.MusicList;
import com.google.gson.Gson;

import java.util.List;

public class SongAdapter1 extends BaseAdapter {
    private Context context;
    private List<MusicList> songList;
    private int itemLayoutId;
    private String user;
    private Gson gson = new Gson();

    public SongAdapter1(String user, Context context, List<MusicList> songList, int itemLayoutId) {
        this.user = user;
        this.context = context;
        this.songList = songList;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    itemLayoutId, null);
            vh = new ViewHolder();
//            vh.songImage = convertView.findViewById(R.id.song_sheet_image);
            vh.songName = convertView.findViewById(R.id.song_sheet_name);
            vh.songMusicCount = convertView.findViewById(R.id.song_sheet_count);
            vh.goTo = convertView.findViewById(R.id.go_to2);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
//        vh.songImage.setImageResource(songList.get(position).getSongImage());
//        Glide.with(context).load(songList.get(position).getSongImage()).into(vh.songImage);
        vh.songName.setText(songList.get(position).getListName());
        vh.songMusicCount.setText(songList.get(position).getCount() + "首");
        //歌单详情
        vh.goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SongDetailActivity.class);
                intent.putExtra("list",gson.toJson(songList.get(position)));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
//        private ImageView songImage;
        private TextView songName;
        private TextView songMusicCount;
        private ImageView goTo;
    }
}
