package com.example.sss.team_project.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sss.team_project.GlideApp;
import com.example.sss.team_project.R;
import com.example.sss.team_project.retrofit.RetrofitService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dialog_member_info extends Dialog {
    Context context;
    String member_id;
    String id;

    public interface CallBack {
        void onClick_send(String member_id);

        void onClick_close();
    }

    CallBack callBack;

    public Dialog_member_info(@NonNull Context context, String member_id, String id) {
        super(context);
        setContentView(R.layout.dialog_member_info);
        ButterKnife.bind(this);
        this.context = context;
        this.member_id = member_id;
        this.id = id;

        set_member_info();
    }

    public void set_member_info() {
        Call<ArrayList<String>> observ = RetrofitService.getInstance().getRetrofitRequest().get_memberInfo(member_id);
        observ.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.isSuccessful()) {
                    ArrayList<String> items = response.body();
                    tv_member_nick.setText(items.get(0).toString());

                    if (items.get(1) == null || items.get(1).equals("")) {
                        Drawable memberpic_null = context.getResources().getDrawable(R.drawable.memberpic_null);
                        iv_member_pic.setImageDrawable(memberpic_null);
                    } else {
                        String url = "http://10.0.2.2:8090/sss/resources/profilepic/";
                        GlideApp.with(context)
                                .load(url + items.get(1)).centerCrop()
                                .into(iv_member_pic);
                    }

                    if (items.get(2) == null || items.get(2).equals("")) {
                        tv_intro.setText("소개가 없습니다.");
                    } else {
                        tv_intro.setText(items.get(2));
                    }

                    if (id.equals(member_id)) {
                        bt_write_message.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.bt_close)
    public void do_close() {
        if (callBack != null) {
            callBack.onClick_close();
        }
    }

    @OnClick(R.id.bt_write_message)
    public void do_bt_write_message() {
        if (callBack != null) {
            callBack.onClick_send(member_id);
        }
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @BindView(R.id.iv_member_pic)
    ImageView iv_member_pic;
    @BindView(R.id.tv_member_nick)
    TextView tv_member_nick;
    @BindView(R.id.tv_intro)
    TextView tv_intro;
    @BindView(R.id.bt_write_message)
    Button bt_write_message;
    @BindView(R.id.bt_close)
    Button close;
}

