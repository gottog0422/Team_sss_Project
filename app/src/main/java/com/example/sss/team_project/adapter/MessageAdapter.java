package com.example.sss.team_project.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sss.team_project.GlideApp;
import com.example.sss.team_project.R;
import com.example.sss.team_project.model.MessageFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends BaseAdapter {

    ArrayList<MessageFile> items = new ArrayList<>();
    Context context;

    public MessageAdapter(ArrayList<MessageFile> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        String url = "http://10.0.2.2:8090/sss/resources/profilepic/";

        if (items.get(i).getMember_pic() == null) {
            Drawable memberpic_null = context.getResources().getDrawable(R.drawable.memberpic_null);
            holder.iv_member_pic.setImageDrawable(memberpic_null);
        } else {
            GlideApp.with(viewGroup).load(url + items.get(i).getMember_pic()).centerCrop().into(holder.iv_member_pic);
        }

        holder.tv_member_nick.setText(items.get(i).getMember_nick());
        holder.tv_content.setText(items.get(i).getMessages().getContent());
        holder.tv_mdate.setText(items.get(i).getMessages().getM_date());

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_member_nick)
        TextView tv_member_nick;
        @BindView(R.id.iv_member_pic)
        ImageView iv_member_pic;
        @BindView(R.id.tv_mdate)
        TextView tv_mdate;
        @BindView(R.id.tv_content)
        TextView tv_content;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
