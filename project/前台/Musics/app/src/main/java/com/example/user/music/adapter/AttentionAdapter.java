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
import com.example.user.music.activity.MyWatchDetailActivity;
import com.example.user.music.entity.User;

import java.util.List;

public class AttentionAdapter extends BaseAdapter {
    private Context context;
    private List<User> userList;
    private int itemLayoutId;

    public AttentionAdapter(Context context,List<User> userList, int itemLayoutId) {
        this.context = context;
        this.userList = userList;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(
                    itemLayoutId, null);
            viewHolder = new ViewHolder();
            viewHolder.userName = convertView.findViewById(R.id.attention_user_name);
            viewHolder.goTo = convertView.findViewById(R.id.attention_go_to);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.userName.setText(userList.get(position).getUserName());
        viewHolder.goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyWatchDetailActivity.class);
                intent.putExtra("watchUserId",userList.get(position).getUserId());
                intent.putExtra("watchUserName",userList.get(position).getUserName());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        private TextView userName;
        private ImageView goTo;
    }
}
