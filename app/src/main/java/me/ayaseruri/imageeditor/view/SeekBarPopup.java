package me.ayaseruri.imageeditor.view;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.ViewById;

import me.ayaseruri.imageeditor.R;

/**
 * Created by wufeiyang on 16/5/27.
 */
@EViewGroup(R.layout.seek_bar_popup)
public class SeekBarPopup extends RelativeLayout{

    public static final int TYPE_BRIGHTESS = 0;
    public static final int TYPE_CONTRAST = 1;
    public static final int TYPE_SATURATION = 2;
    public static final int TYPE_TMP = 3;

    @ViewById(R.id.seek_bar)
    AppCompatSeekBar mSeekBar;
    @ViewById(R.id.cancle)
    Button mCancleBtn;
    @ViewById(R.id.ok)
    Button mOkBtn;
    @ViewById(R.id.progress_hint)
    TextView mProgressHint;

    private IOnSeekPopupAction mIOnSeekPopupAction;
    private int mType;

    public SeekBarPopup(Context context) {
        super(context);
    }

    public SeekBarPopup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){

    }

    @SeekBarProgressChange(R.id.seek_bar)
    void onSeekBarChange(android.widget.SeekBar seekBar, int progress, boolean fromUser){
        if(null != mIOnSeekPopupAction){
            mProgressHint.setText(String.valueOf(progress));
            mIOnSeekPopupAction.onSeekBar(mType, progress);
        }
    }

    @Click(R.id.cancle)
    void onCancleBtn(){
        setVisibility(GONE);
        if(null != mIOnSeekPopupAction){
            mIOnSeekPopupAction.onCancel();
        }
    }

    @Click(R.id.ok)
    void onOkBtn(){
        setVisibility(GONE);
        if(null != mIOnSeekPopupAction){
            mIOnSeekPopupAction.onOk();
        }
    }

    public void init(float value){
        setVisibility(VISIBLE);
        mSeekBar.setProgress((int) value);
    }

    public void setType(int type) {
        mType = type;
    }

    public void setIOnSeekPopupAction(IOnSeekPopupAction IOnSeekPopupAction) {
        mIOnSeekPopupAction = IOnSeekPopupAction;
    }

    public interface IOnSeekPopupAction{
        void onSeekBar(int type, int value);
        void onCancel();
        void onOk();
    }
}
