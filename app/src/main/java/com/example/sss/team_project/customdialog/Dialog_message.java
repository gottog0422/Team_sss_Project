package com.example.sss.team_project.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sss.team_project.GlideApp;
import com.example.sss.team_project.R;
import com.example.sss.team_project.model.MessageFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dialog_message extends Dialog {
    Context context;
    MessageFile item;
    int type;

    @BindView(R.id.tv_member_id)
    TextView tv_member_id;
    @BindView(R.id.iv_member_pic)
    ImageView iv_member_pic;
    @BindView(R.id.tv_m_date)
    TextView tv_m_date;
    @BindView(R.id.tv_change)
    TextView tv_change;
    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.bt_replay)
    Button bt_replay;
    @BindView(R.id.bt_del)
    Button bt_del;
    @BindView(R.id.bt_close)
    Button bt_close;

    public interface CallBack {
        void click_replay(String member_id);

        void click_close();

        void click_del();
    }

    CallBack callback;

    public Dialog_message(@NonNull Context context, MessageFile item, int type) {
        super(context);
        setContentView(R.layout.dialog_see_message);
        ButterKnife.bind(this);

        this.context = context;
        this.item = item;
        this.type = type;

        setContext();
    }

    public void setContext() {
        String url = "http://10.0.2.2:8090/sss/resources/profilepic/";

        if (type == 0) {
            tv_change.setText("보낸 사람");
        } else if (type == 1) {
            tv_change.setText("받은 사람");
            bt_replay.setText("쪽지");
        }

        if (item.getMember_pic() == null) {
            Drawable memberpic_null = context.getResources().getDrawable(R.drawable.memberpic_null);
            iv_member_pic.setImageDrawable(memberpic_null);
        } else {
            GlideApp.with(context).load(url + item.getMember_pic()).centerCrop().into(iv_member_pic);
        }

        tv_member_id.setText(item.getMember_nick());
        tv_m_date.setText(item.getMessages().getM_date());
        tv_content.setText(item.getMessages().getContent());
    }

    @OnClick(R.id.bt_replay)
    public void onClick_replay(View view) {
        if (callback != null) {
            if (type == 0) {
                callback.click_replay(item.getMessages().getW_id());
            } else {
                callback.click_replay(item.getMessages().getR_id());
            }
        }
    }

    @OnClick(R.id.bt_del)
    public void onClick_del(View view) {
        if (callback != null) {
            callback.click_del();
        }
    }

    @OnClick(R.id.bt_close)
    public void onClick_bt_close(View view) {
        if (callback != null) {
            callback.click_close();
        }
    }

    public CallBack getCallBack() {
        return callback;
    }

    public void setCallBack(CallBack callBack) {
        this.callback = callBack;
    }
}