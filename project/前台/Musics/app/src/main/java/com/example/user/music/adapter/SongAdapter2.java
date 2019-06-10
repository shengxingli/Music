package com.example.user.music.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.music.R;
import com.example.user.music.activity.AddSongActivity;
import com.example.user.music.entity.MusicList;
import com.example.user.music.entity.User;
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

public class SongAdapter2 extends BaseAdapter {
    private Context context;
    private List<MusicList> songList;
    private int itemLayoutId;
    private int musicId;
    private String musicName;

    public SongAdapter2(Context context, List<MusicList> songList, int itemLayoutId, int musicId,String musicName) {
        this.context = context;
        this.songList = songList;
        this.itemLayoutId = itemLayoutId;
        this.musicId = musicId;
        this.musicName = musicName;
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
            vh.songName = convertView.findViewById(R.id.song_sheet_name);
            vh.songMusicCount = convertView.findViewById(R.id.song_sheet_count);
            vh.selectSong = convertView.findViewById(R.id.select_song);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
//        vh.songImage.setImageResource(songList.get(position).getSongImage());
        vh.songName.setText(songList.get(position).getListName());
        vh.songMusicCount.setText(songList.get(position).getCount() + "首");
        vh.selectSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap<>();
                map.put("musicName",musicName);
                map.put("musicId",musicId+"");
                Log.e("测试",songList.get(position).getListId()+"");
                map.put("listId",songList.get(position).getListId()+"");
                AddMusicToList addMusicToList = new AddMusicToList();
                addMusicToList.setMap(map);
                addMusicToList.execute();
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView songName;
        private TextView songMusicCount;
        private LinearLayout selectSong;
    }

    class AddMusicToList extends AsyncTask {

        private Map<String,String> map;
        private MyResponse<?> myResponse;

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                String result = MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"musicList/insertMusic", map)).body().string();
                Log.e("测试",result);
                myResponse = new Gson().fromJson(result,new TypeToken<MyResponse<?>>(){}.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (myResponse.getCode() == 200){
                Toast.makeText(context,"加入成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(context,"加入失败",Toast.LENGTH_LONG).show();
            }
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
