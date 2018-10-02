package com.example.sss.team_project.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.sss.team_project.GlideApp;
import com.example.sss.team_project.R;
import com.example.sss.team_project.model.DetailContent;
import com.example.sss.team_project.model.WriteContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailContentAdapter extends BaseAdapter {
    ArrayList<DetailContent> items = new ArrayList<>();

    public DetailContentAdapter(ArrayList<DetailContent> items) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detailboard_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if (items.get(i).getItem_str() != null) {
            holder.tv_item.setText(items.get(i).getItem_str());
            holder.iv_item.setVisibility(View.GONE);
        }
        if (items.get(i).getItem_iv() != null) {
            holder.tv_item.setVisibility(View.GONE);

            String url = "http://10.0.2.2:8090/sss/resources/upload/";

            Glide.with(viewGroup).load(url + items.get(i).getItem_iv()).into(holder.iv_item);

        }

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_item)
        ImageView iv_item;
        @BindView(R.id.tv_item)
        TextView tv_item;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
