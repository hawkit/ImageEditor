package me.ayaseruri.imageeditor.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import me.ayaseruri.imageeditor.R;

/**
 * Created by wufeiyang on 16/5/27.
 */
public class ImageTagView extends FrameLayout{

    private float mX, mY;
    private boolean mEnbale = true;
    private int[] mScreenPos;
    private ImageTag mSelectImageTag;

    public ImageTagView(Context context) {
        super(context);
    }

    public ImageTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mEnbale){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mX = event.getX();
                    mY = event.getY();
                    if(null == mSelectImageTag){
                        showInfoPop();
                    }

                    return true;
                case MotionEvent.ACTION_MOVE:
                    if(null != mSelectImageTag){
                        mSelectImageTag.setX(event.getX());
                        mSelectImageTag.setY(event.getY() - mSelectImageTag.getCenterY());
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mSelectImageTag = null;
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    public void showInfoPop(){
        PopupWindow pop = getPop(new IPopupOk() {
            @Override
            public void onOk(String title, String price, String location) {
                initTag(title, price, location);
            }
        });
        mScreenPos = new int[2];
        getLocationOnScreen(mScreenPos);
        pop.showAtLocation(getRootView(), Gravity.BOTTOM, 0, 0);
    }

    private void initTag(String title, String price, String location){
        final ImageTag imageTag = new ImageTag(getContext());
        imageTag.createTag(title, price, location);
        imageTag.setX(mX);
        imageTag.setY(mY - imageTag.getCenterY());

        imageTag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getPop(new IPopupOk() {
                    @Override
                    public void onOk(String title, String price, String location) {
                        imageTag.clearText();
                        imageTag.setTitle(title);
                        imageTag.setPrice(price);
                        imageTag.setLocation(location);
                    }
                }).showAtLocation(getRootView(), Gravity.BOTTOM, 0, 0);
            }
        });

        imageTag.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeView(imageTag);
                return true;
            }
        });

        imageTag.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mX = event.getX();
                        mY = event.getY();
                        mSelectImageTag = imageTag;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        imageTag.setX(event.getRawX() - mX);
                        imageTag.setY(event.getRawY() - mScreenPos[1] - mY);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        addView(imageTag, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ObjectAnimator anim = ObjectAnimator.ofFloat(imageTag, "percentage", 0.0f, 1.0f);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(800);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    private PopupWindow getPop(final IPopupOk popupOk){
        final PopupWindow pop = new PopupWindow(LayoutInflater.from(getContext()).inflate(R.layout.info_pop, null)
                , WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        pop.getContentView().findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != popupOk){
                    EditText titleET = (EditText) pop.getContentView().findViewById(R.id.brand);
                    EditText priceET = (EditText) pop.getContentView().findViewById(R.id.price);
                    EditText locationET = (EditText) pop.getContentView().findViewById(R.id.location);

                    popupOk.onOk(titleET.getText().toString(), priceET.getText().toString(), locationET.getText().toString());
                }
            }
        });

        return pop;
    }

    public void setEnbale(boolean enbale) {
        mEnbale = enbale;
    }

    interface IPopupOk{
        void onOk(String title, String price, String location);
    }
}
