package com.example.sss.team_project.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.sss.team_project.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dialog_Pic extends Dialog {
    private Context context;

    public interface CallBack {
        void onClickBt1();

        void onClickBt2();
    }

    CallBack callBack;


    @BindView(R.id.bt1)
    RelativeLayout bt1;
    @BindView(R.id.bt2)
    RelativeLayout bt2;

    public Dialog_Pic(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_pic);
        ButterKnife.bind(this);
        this.context = context;
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

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
