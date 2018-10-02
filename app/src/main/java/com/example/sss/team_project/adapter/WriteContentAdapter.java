package com.example.sss.team_project.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sss.team_project.GlideApp;
import com.example.sss.team_project.R;
import com.example.sss.team_project.model.WriteContent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WriteContentAdapter extends BaseAdapter {
    ArrayList<WriteContent> items = new ArrayList<>();
    Context context;

    public WriteContentAdapter(ArrayList<WriteContent> items, Context context) {
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
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.writeboard_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if (items.get(i).getItem_iv() != null) {
            holder.et_item.setVisibility(View.GONE);
            holder.bt_item_del.setVisibility(View.VISIBLE);
            holder.iv_item.setVisibility(View.VISIBLE);


            Glide.with(viewGroup)
                    .load(items.get(i).getItem_iv())
                    .into(holder.iv_item);

        } else if (items.get(i).getItem_iv() == null) {
            holder.et_item.setVisibility(View.VISIBLE);
            holder.bt_item_del.setVisibility(View.GONE);
            holder.iv_item.setVisibility(View.GONE);
        }


        holder.et_item.setText(items.get(i).getItem_str());
        holder.bt_item_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.et_item.clearFocus();
                if (items.size() == 1) {

                } else {
                    PopupMenu p = new PopupMenu(context.getApplicationContext(), holder.bt_item_move);
                    ((AppCompatActivity) context).getMenuInflater().inflate(R.menu.up_down_menu, p.getMenu());

                    if (i == 0) {
                        p.getMenu().findItem(R.id.up).setVisible(false);
                    } else if (i == items.size() - 1) {
                        p.getMenu().findItem(R.id.down).setVisible(false);
                    }
                    p.show();

                    p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.up:
                                    Collections.swap(items, i, i - 1);
                                    notifyDataSetChanged();
                                    break;
                                case R.id.down:
                                    Collections.swap(items, i, i + 1);
                                    notifyDataSetChanged();
                                    break;
                            }
                            return false;
                        }
                    });
                }
            }
        });

        holder.bt_item_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.remove(i);
                notifyDataSetChanged();
            }
        });

        holder.et_item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    items.get(i).setItem_str(holder.et_item.getText().toString());
                }
            }
        });

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.bt_item_move)
        ImageView bt_item_move;
        @BindView(R.id.iv_item)
        ImageView iv_item;
        @BindView(R.id.et_item)
        EditText et_item;
        @BindView(R.id.bt_item_del)
        ImageView bt_item_del;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
