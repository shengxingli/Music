package com.example.user.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.music.R;
import com.example.user.music.activity.AddSongActivity;
import com.example.user.music.activity.MusicPlayerActivity;
import com.example.user.music.entity.Music;
import com.google.gson.Gson;

import java.util.List;


public class MusicAdapter extends BaseAdapter {
    private Context context;
    private List<Music> musicList;
    private int itemLayoutId;
    private int userId;

    public MusicAdapter(Context context, List<Music> musicList, int itemLayoutId, int userId) {
        this.context = context;
        this.musicList = musicList;
        this.itemLayoutId = itemLayoutId;
        this.userId = userId;
    }

    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return musicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(
                    itemLayoutId, null);
            vh = new ViewHolder();
            vh.musicName = convertView.findViewById(R.id.music_name);
            vh.addSong = convertView.findViewById(R.id.music_add_song);
            vh.musicPlay = convertView.findViewById(R.id.music_play);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.musicName.setText(musicList.get(position).getMusicName());
        vh.musicPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("music",new Gson().toJson(musicList.get(position)));
                intent.putExtra("userId",userId);
                context.startActivity(intent);
            }
        });
        vh.addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddSongActivity.class);
                intent.putExtra("userId",userId);
                intent.putExtra("musicName",musicList.get(position).getMusicName());
                intent.putExtra("musicId",musicList.get(position).getMusicId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        private TextView musicName;
        private ImageView addSong;
        private ImageView musicPlay;
    }
}
