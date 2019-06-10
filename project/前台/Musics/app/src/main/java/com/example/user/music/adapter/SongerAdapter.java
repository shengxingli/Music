package com.example.user.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.music.R;
import com.example.user.music.activity.SingerDetailActivity;
import com.example.user.music.entity.Singer;

import java.util.List;

public class SongerAdapter extends BaseAdapter {
    private List<Singer> singerList;
    private Context context;
    private int itemLayoutId;
    private int userId;

    public SongerAdapter(List<Singer> singerList, Context context, int itemLayoutId, int userId) {
        this.singerList = singerList;
        this.context = context;
        this.itemLayoutId = itemLayoutId;
        this.userId = userId;
    }

    @Override
    public int getCount() {
        return singerList.size();
    }

    @Override
    public Object getItem(int position) {
        return singerList.get(position);
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
            vh.singerImage = convertView.findViewById(R.id.singer_image1);
            vh.singerName = convertView.findViewById(R.id.singer_name1);
            vh.goTo = convertView.findViewById(R.id.go_to1);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(singerList.get(position).getSingerAvatar()).into(vh.singerImage);
        vh.singerName.setText(singerList.get(position).getSingerName());
        vh.goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingerDetailActivity.class);
                Log.e("测试",singerList.get(position).getId()+"");
                intent.putExtra("singerId",singerList.get(position).getId());
                intent.putExtra("singerName",singerList.get(position).getSingerName());
                intent.putExtra("userId",userId);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        private ImageView singerImage;
        private TextView singerName;
        private ImageView goTo;
    }
}
