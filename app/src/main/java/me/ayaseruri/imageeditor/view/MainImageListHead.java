package me.ayaseruri.imageeditor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;

import me.ayaseruri.imageeditor.R;

/**
 * Created by wufeiyang on 16/5/26.
 */
@EViewGroup(R.layout.main_image_head)
public class MainImageListHead extends LinearLayout {
    public MainImageListHead(Context context) {
        super(context);
    }

    public MainImageListHead(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
    }
}
