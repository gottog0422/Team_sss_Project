package com.example.sss.team_project.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.example.sss.team_project.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dialog_comment extends Dialog {
    private Context context;

    public interface CallBack {
        void onClickBt0();

        void onClickBt1();

        void onClickBt2();

        void onClickBt3();
    }

    CallBack callBack;

    Boolean my;

    @BindView(R.id.bt0)
    LinearLayout bt0;
    @BindView(R.id.bt1)
    LinearLayout bt1;
    @BindView(R.id.bt2)
    LinearLayout bt2;
    @BindView(R.id.bt3)
    LinearLayout bt3;

    public Dialog_comment(@NonNull Context context, Boolean my) {
        super(context);
        setContentView(R.layout.dialog_comment);
        ButterKnife.bind(this);
        this.context = context;
        this.my = my;

        if (my) {
            bt0.setVisibility(View.GONE);
            bt1.setVisibility(View.GONE);
        } else {
            bt2.setVisibility(View.GONE);
            bt3.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.bt0)
    public void onClickBtn0(View view) {
        if (callBack != null) {
            callBack.onClickBt0();
        }
    }

    @OnClick(R.id.bt1)
    public void onClickBt1(View view) {
        if (callBack != null) {
            callBack.onClickBt1();
        }
    }

    @OnClick(R.id.bt2)
    public void onClickBtn2(View view) {
        if (callBack != null) {
            callBack.onClickBt2();
        }
    }

    @OnClick(R.id.bt3)
    public void onClickBtn3(View view) {
        if (callBack != null) {
            callBack.onClickBt3();
        }
    }


    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
