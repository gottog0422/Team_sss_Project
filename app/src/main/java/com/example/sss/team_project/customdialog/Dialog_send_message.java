package com.example.sss.team_project.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sss.team_project.GlideApp;
import com.example.sss.team_project.R;
import com.example.sss.team_project.retrofit.RetrofitService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dialog_send_message extends Dialog {
    Context context;
    String member_id;
    String id;

    public interface CallBack {
        void onClick_close();
    }

    CallBack callBack;

    public Dialog_send_message(@NonNull Context context, String member_id, String id) {
        super(context);
        setContentView(R.layout.dialog_send_message);
        ButterKnife.bind(this);
        this.context = context;
        this.member_id = member_id;
        this.id = id;

        set_memberInfo();
    }

    @OnClick(R.id.bt_send)
    public void send_message() {
        String content = et_message.getText().toString();
        String temp = content.replaceAll(" ", "");
        if (temp == null || temp.equals("")) {

        } else {

            long mNow;
            Date mDate;
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

            mNow = System.currentTimeMillis();
            mDate = new Date(mNow);
            final String date = mFormat.format(mDate);

            Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().send_message(id, member_id, content,date);
            observ.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        if (callBack != null) {
                            callBack.onClick_close();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }

    }

    @OnClick(R.id.bt_close)
    public void do_close() {
        if (callBack != null) {
            callBack.onClick_close();
        }
    }

    public void set_memberInfo() {
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
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    @BindView(R.id.bt_close)
    Button bt_close;
    @BindView(R.id.bt_send)
    Button bt_send;
    @BindView(R.id.iv_member_pic)
    ImageView iv_member_pic;
    @BindView(R.id.tv_member_nick)
    TextView tv_member_nick;
    @BindView(R.id.et_message)
    EditText et_message;
}