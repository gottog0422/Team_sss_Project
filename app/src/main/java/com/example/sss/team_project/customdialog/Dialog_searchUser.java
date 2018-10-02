package com.example.sss.team_project.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class Dialog_searchUser extends Dialog {
    Context context;

    public interface CallBack {
        void onClick_user(String member_id);
    }

    CallBack callBack;
    String search_id;

    public Dialog_searchUser(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_serach_user);
        ButterKnife.bind(this);
        this.context = context;
    }

    @OnClick(R.id.bt_search)
    public void user_search() {
        String search_nick = et_search.getText().toString();
        if (!search_nick.equals("")) {
            Call<ArrayList<String>> observ = RetrofitService.getInstance().getRetrofitRequest().get_memberInfo_nick(search_nick);
            observ.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<String> items = response.body();

                        et_search.setText("");

                        if (items.size() == 0) {
                            rl_member.setVisibility(View.GONE);
                            tv_none_member.setVisibility(View.VISIBLE);
                        } else {
                            rl_member.setVisibility(View.VISIBLE);
                            tv_none_member.setVisibility(View.GONE);
                            search_id = items.get(0);

                            tv_member_nick.setText(items.get(1));

                            if (items.get(2) == null || items.get(2).equals("")) {
                                Drawable memberpic_null = context.getResources().getDrawable(R.drawable.memberpic_null);
                                iv_member_pic2.setImageDrawable(memberpic_null);
                            } else {
                                String url = "http://10.0.2.2:8090/sss/resources/profilepic/";
                                GlideApp.with(context)
                                        .load(url + items.get(2)).centerCrop()
                                        .into(iv_member_pic2);
                            }
                        }
                    }
                }

                @Override

                public void onFailure(Call<ArrayList<String>> call, Throwable t) {

                }
            });
        }
    }

    @OnClick(R.id.rl_member)
    public void user_select() {
        if (callBack != null) {
            callBack.onClick_user(search_id);
        }
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    @BindView(R.id.rl_member)
    RelativeLayout rl_member;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.bt_search)
    Button bt_search;
    @BindView(R.id.iv_member_pic2)
    ImageView iv_member_pic2;
    @BindView(R.id.tv_member_nick)
    TextView tv_member_nick;
    @BindView(R.id.tv_none_member)
    TextView tv_none_member;

}
