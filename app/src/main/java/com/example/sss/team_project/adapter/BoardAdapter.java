package com.example.sss.team_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sss.team_project.R;
import com.example.sss.team_project.model.Board;
import com.example.sss.team_project.model.BoardList;
import com.example.sss.team_project.retrofit.RetrofitService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardAdapter extends BaseAdapter {
    ArrayList<BoardList> board_items = new ArrayList<>();
    String date;

    public BoardAdapter(ArrayList<BoardList> board_items, String date) {
        this.board_items = board_items;
        this.date = date;
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.board_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.title.setText(board_items.get(i).getBoard().getTitle());

        String board_date = board_items.get(i).getBoard().getMdate();
        if (date.equals(board_date.substring(0, board_date.length() - 6))) {
            //시간만
            holder.mdate.setText(board_date.substring(board_date.length() - 5, board_date.length()));
        } else {
            //날짜만
            holder.mdate.setText(board_date.substring(2, board_date.length() - 6));
        }


        holder.writer_nick.setText(board_items.get(i).getWriter_nick());
        holder.count.setText(board_items.get(i).getBoard().getCount() + "");
        holder.hit.setText(board_items.get(i).getBoard().getHit() + "");
        holder.tv_comment_count.setText(board_items.get(i).getCount()+"");

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.writer_nick)
        TextView writer_nick;
        @BindView(R.id.mdate)
        TextView mdate;
        @BindView(R.id.count)
        TextView count;
        @BindView(R.id.hit)
        TextView hit;
        @BindView(R.id.iv_preview)
        ImageView iv_preview;
        @BindView(R.id.tv_comment_count)
        TextView tv_comment_count;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
