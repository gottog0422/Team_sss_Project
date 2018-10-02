package com.example.sss.team_project.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.sss.team_project.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dialog_change_pw extends Dialog {
    private Context context;

    public interface CallBack {
        void bt_cancel();

        void bt_change(String et_str1, String et_str2, String et_str3);
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    CallBack callBack;


    public Dialog_change_pw(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dialog_change_pw);
        ButterKnife.bind(this);
        this.context = context;
    }

    @BindView(R.id.bt_cancel)
    Button bt_cancel;
    @BindView(R.id.bt_change)
    Button bt_change;

    @BindView(R.id.et_1)
    EditText et_1;
    @BindView(R.id.et_2)
    EditText et_2;
    @BindView(R.id.et_3)
    EditText et_3;


    @OnClick(R.id.bt_cancel)
    public void cancle() {
        if (callBack != null) {
            callBack.bt_cancel();
        }
    }

    @OnClick(R.id.bt_change)
    public void bt_change() {
        if (callBack != null) {
            callBack.bt_change(et_1.getText().toString(), et_2.getText().toString(), et_3.getText().toString());
        }
    }

}
