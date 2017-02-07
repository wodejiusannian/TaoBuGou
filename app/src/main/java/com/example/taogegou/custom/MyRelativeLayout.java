package com.example.taogegou.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Hashtable;

/**
 * Created by 小五 on 2017/1/10.
 */
public class MyRelativeLayout extends RelativeLayout {

    private int mTop,mBottom,mLeft,mRight;
    private Hashtable<View,Position> map = new Hashtable<>();
    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //父布局的宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        mBottom = 0;
        mLeft = 0;
        mRight = 0;
        mTop = 0;
        int count = getChildCount();
        int j = 0;
        for (int i = 0;i<count;i++){
            Position pos = new Position();
            View view = getChildAt(i);
            mLeft = getPosition(i-j,i);
            mRight = mLeft+view.getMeasuredWidth();
            if (mRight>=width){
                j=i;
                mLeft = getPosition(i-j,i);
                mRight = mLeft+view.getMeasuredWidth();
                mTop = getChildAt(i).getMeasuredHeight()+5;
            }
            mBottom = mTop +view.getMeasuredHeight();
            pos.bottom = mBottom;
            pos.left = mLeft;
            pos.right = mRight;
            pos.top = mTop;
            map.put(view,pos);
        }
    }

    private int  getPosition(int indexInRox,int childIndex) {
        if (indexInRox>0){
            return getPosition(indexInRox-1,childIndex-1)+getChildAt(childIndex-1).getMeasuredWidth()+10;
        }
        return 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int count = getChildCount();
        for (int i = 0;i<count;i++){
            View view = getChildAt(i);
            Position pos = map.get(view);
            if (pos!=null){
                view.layout(pos.left,pos.top,pos.right,pos.bottom);
            }else{
                Log.i("TAG", "======>>>error");
            }
        }
    }



    private class Position{
        private int left,right,top,bottom;
    }
}
