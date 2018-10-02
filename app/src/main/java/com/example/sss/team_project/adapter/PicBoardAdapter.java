package com.example.sss.team_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sss.team_project.GlideApp;
import com.example.sss.team_project.R;
import com.example.sss.team_project.model.BoardList;
import com.example.sss.team_project.model.PicBoardList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicBoardAdapter extends BaseAdapter {
    ArrayList<PicBoardList> board_items = new ArrayList<>();

    public PicBoardAdapter(ArrayList<PicBoardList> board_items) {
        this.board_items = board_items;
    }

    @Override
    public int getCount() {
        return board_items.size();
    }

    @Override
    public Object getItem(int i) {
        return board_items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_picboard, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.tv_nick.setText(board_items.get(i).getWriter_nick());

        String url = "http://10.0.2.2:8090/sss/resources/upload/";
        GlideApp.with(viewGroup).load(url + board_items.get(i).getFirst_pic()).centerCrop().into(holder.iv_pic);

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_pic)
        ImageView iv_pic;
        @BindView(R.id.tv_nick)
        TextView tv_nick;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
