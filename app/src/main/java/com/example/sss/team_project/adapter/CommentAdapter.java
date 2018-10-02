package com.example.sss.team_project.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sss.team_project.GlideApp;
import com.example.sss.team_project.R;
import com.example.sss.team_project.model.CommentFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CommentAdapter extends BaseAdapter {

    ArrayList<CommentFile> items = new ArrayList<>();
    String my_id;
    String board_writer_id;
    Context context;

    public CommentAdapter(ArrayList<CommentFile> items, String my_id, String board_writer_id, Context context) {
        this.items = items;
        this.my_id = my_id;
        this.board_writer_id = board_writer_id;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.tv_item_comment_nick.setText(items.get(i).getMember_nick());
        holder.tv_item_date.setText(items.get(i).getMDate());
        holder.tv_item_comment.setText(items.get(i).getComment());

        String url = "http://10.0.2.2:8090/sss/resources/profilepic/";

        if (items.get(i).getMember_pic() == null) {
            Drawable memberpic_null = context.getResources().getDrawable(R.drawable.memberpic_null);
            holder.iv_member_pic.setImageDrawable(memberpic_null);
        } else {
            GlideApp.with(viewGroup).load(url + items.get(i).getMember_pic()).centerCrop().into(holder.iv_member_pic);
        }

        if (board_writer_id.equals(items.get(i).getMember_id())) {
            holder.writer.setVisibility(View.VISIBLE);
        }

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_item_comment_nick)
        TextView tv_item_comment_nick;
        @BindView(R.id.tv_item_date)
        TextView tv_item_date;
        @BindView(R.id.iv_member_pic)
        ImageView iv_member_pic;
        @BindView(R.id.tv_item_comment)
        TextView tv_item_comment;
        @BindView(R.id.writer)
        TextView writer;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
