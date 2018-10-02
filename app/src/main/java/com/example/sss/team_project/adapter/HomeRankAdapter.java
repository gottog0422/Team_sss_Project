package com.example.sss.team_project.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sss.team_project.R;
import com.example.sss.team_project.model.Board;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeRankAdapter extends BaseAdapter {

    ArrayList<Board> items = new ArrayList<>();

    public HomeRankAdapter(ArrayList<Board> items) {
        this.items = items;
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        int type = items.get(i).getType();
        String type_str = "";
        if (type == 1) {
            type_str = "[자유 게시판] ";
        } else if (type == 2) {
            type_str = "[분양 게시판] ";
        }

        holder.title.setText(type_str + items.get(i).getTitle());
        Drawable drawable;
        if (i == 0) {
            drawable = viewGroup.getContext().getResources().getDrawable(R.drawable.one);
        } else if (i == 1) {
            drawable = viewGroup.getContext().getResources().getDrawable(R.drawable.two);
        } else {
            drawable = viewGroup.getContext().getResources().getDrawable(R.drawable.three);
        }
        holder.iv_lank.setImageDrawable(drawable);

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.iv_lank)
        ImageView iv_lank;
        @BindView(R.id.type)
        TextView type;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
