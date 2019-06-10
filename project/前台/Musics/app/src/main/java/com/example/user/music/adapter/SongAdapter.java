package com.example.user.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.music.R;
import com.example.user.music.activity.ManageSongActivity;
import com.example.user.music.entity.MusicList;
import com.example.user.music.lib.Cont;
import com.example.user.music.lib.MyRequest;
import com.example.user.music.lib.MyResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SongAdapter extends BaseAdapter {
    private Context context;
    private List<MusicList> songList;
    private int itemLayoutId;
    private int userId;

    public SongAdapter(Context context, List<MusicList> songList, int itemLayoutId) {
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
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(
                    itemLayoutId, null);
            vh = new ViewHolder();
            vh.songName = convertView.findViewById(R.id.song_sheet_name);
            vh.songMusicCount = convertView.findViewById(R.id.song_sheet_count);
            vh.delete = convertView.findViewById(R.id.delete);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
//        Glide.with(context).load(songList.get(position).getSongImage()).into(vh.songImage);
        vh.songName.setText(songList.get(position).getListName());
        vh.songMusicCount.setText(songList.get(position).getCount()+"首");
        //删除歌单
        vh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用删除歌单的异步任务
                DeleteSongAsyncTask task = new DeleteSongAsyncTask();
                Map<String,String> map = new HashMap<>();
                map.put("listId",songList.get(position).getListId()+"");
                task.setMap(map);
                task.setPosition(position);
                task.execute();
            }
        });

        return convertView;
    }
    class ViewHolder{
        private TextView songName;
        private TextView songMusicCount;
        private TextView delete;
    }

    class DeleteSongAsyncTask extends AsyncTask {
        private int position;
        private Map<String,String> map;
        @Override
        protected Object doInBackground(Object[] objects) {
            MyRequest.sendRequest(MyRequest.createPostRequest(Cont.url+"music_list/remove", map), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            ((ManageSongActivity)context).reflash(songList.get(position));
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }
}
