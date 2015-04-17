package com.example.administrator.qq50damo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Administrator on 2015/4/16 0016.
 */
public class MyHrizontal extends HorizontalScrollView {

    private int SreenWidth;
    private int menuRight_pad;
    private LinearLayout mLinearLayout;
    private ViewGroup menu_group;
    private ViewGroup content_group;
    public boolean isopen;

    private VelocityTracker mVelocityTracker = null;

    private boolean once = true;

    private ArrayList<Integer> list = null;



    public MyHrizontal(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyHrizontal);

        menuRight_pad = (int) array.getDimension(R.styleable.MyHrizontal_rightpadding,
                menuRight_pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
        Log.e("===============得到的menupadding", menuRight_pad + "");
        array.recycle();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        SreenWidth = displayMetrics.widthPixels;

        // menuRight_pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, menuRight_pad, context.getResources().getDisplayMetrics());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (once) {
            mLinearLayout = (LinearLayout) getChildAt(0);
            menu_group = (ViewGroup) mLinearLayout.getChildAt(0);
            content_group = (ViewGroup) mLinearLayout.getChildAt(1);
            menu_group.getLayoutParams().width = SreenWidth - menuRight_pad;
            content_group.getLayoutParams().width = SreenWidth;
            once = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(SreenWidth - menuRight_pad, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }


        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (list != null) {
                    list = null;
                }
                list = new ArrayList<>();
                break;


            case MotionEvent.ACTION_UP:
                Collections.sort(list);
                int scollx = getScrollX();
                Log.d("=====================最大的数", list.get(list.size() - 1) + "px/s");
                Log.d("=====================最小的数", list.get(0) + "px/s");
                if (list.get(list.size() - 1) >= 2300) {
                    this.smoothScrollTo(0, 0);
                    isopen = true;
                } else if (scollx >= (SreenWidth - menuRight_pad) / 2 || list.get(0) <= -2300) {
                    this.smoothScrollTo(SreenWidth - menuRight_pad, 0);
                    isopen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isopen = true;
                }
                mVelocityTracker.recycle();
                mVelocityTracker = null;

                return true;

            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                Log.e("=========>>>>>>>>>>>>>>>>", mVelocityTracker.getXVelocity() + "px/s");
                list.add((int) mVelocityTracker.getXVelocity());
                break;
        }
        return super.onTouchEvent(event);
    }

    public void openmenu() {
        if (isopen) {
           return;
        }
          else {
            this.smoothScrollTo(0, 0);
            isopen = true;
        }
    }

    public void closemenu(){

        if (isopen){
            MyHrizontal.this.smoothScrollTo(SreenWidth - menuRight_pad, 0);
            Log.e("=========>>>>>>>>>>>>>>>>>>>我被调用了哟","closemenu"+(SreenWidth - menuRight_pad));
            isopen = false;
        }
        return;
    }

    public void tog(){
        if (isopen){
            closemenu();
        }
        else{
            openmenu();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f /(SreenWidth - menuRight_pad); // 1 ~ 0

        float rightScale = 0.7f + 0.3f * scale;
        float leftScale = 1.0f - scale * 0.3f;
        float leftAlpha = 0.6f + 0.4f * (1 - scale);

        ViewHelper.setTranslationX(menu_group, (SreenWidth - menuRight_pad) * scale * 0.8f);

        ViewHelper.setScaleX(menu_group, leftScale);
        ViewHelper.setScaleY(menu_group, leftScale);
        ViewHelper.setAlpha(menu_group, leftAlpha);

        ViewHelper.setPivotX(content_group, 0);
        ViewHelper.setPivotY(content_group, content_group.getHeight() / 2);
        ViewHelper.setScaleX(content_group, rightScale);
        ViewHelper.setScaleY(content_group, rightScale);



    }
}
