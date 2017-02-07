package com.example.taogegou.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.taogegou.R;


/**
 * Created by 小五 on 2016/12/26.
 */
public class MySelfDialog extends Dialog{
    private Button mButtonNo,mButtonYes;
    private String mNo,mYes,mMessage,mTitle;
    private OnNoClickListener mNoListener;
    private OnYesClickListener mYesListener;
    private TextView mTextViewMessage,mTextViewTitle;
    private RelativeLayout mAliPay;
    private boolean mFlag = false;
    public void setOnNoListener(String no,OnNoClickListener noListener){
        if (no!=null){
            mNo = no;
        }
        mNoListener = noListener;
    }
    public void setOnYesListener(String yes,OnYesClickListener yesListener){
        if (yes!=null){
            mYes = yes;
        }
        mYesListener = yesListener;
    }



    public void setMessage(String message){
        if (message!=null){
            mMessage = message;
        }
    }

    public void setTitle(String title){
        if (title!=null){
            mTitle = title;
        }
    }

    public void setPay(boolean flag){
        mFlag = flag;
    }

    public MySelfDialog(Context context) {
        super(context, R.style.MySelfDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_dialog);
        setCanceledOnTouchOutside(false);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mButtonNo = (Button) findViewById(R.id.my_self_no);
        mButtonYes = (Button) findViewById(R.id.my_self_yes);
        mTextViewTitle = (TextView) findViewById(R.id.my_self_title);
        mTextViewMessage = (TextView) findViewById(R.id.my_self_message);
    }

    private void initData() {
        if (mMessage != null){
            mTextViewMessage.setText(mMessage);
        }
        if (mTitle != null){
            mTextViewTitle.setText(mTitle);
        }
        if (mNo!=null){
            mButtonNo.setText(mNo);
        }
        if (mYes!=null){
            mButtonYes.setText(mYes);
        }
        if (mFlag){
            mAliPay.setVisibility(View.VISIBLE);
        }
    }

    private void setListener() {

        mButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoListener!=null){
                    mNoListener.onClick();
                }
                dismiss();
            }
        });
        mButtonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mYesListener!=null){
                    mYesListener.onClick();
                }
                dismiss();
            }
        });


    }



    public interface OnNoClickListener{
        void onClick();
    }

    public interface OnYesClickListener{
        void onClick();
    }


}
