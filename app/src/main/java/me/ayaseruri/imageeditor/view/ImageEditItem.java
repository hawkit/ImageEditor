package me.ayaseruri.imageeditor.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import me.ayaseruri.imageeditor.R;
import me.ayaseruri.imageeditor.utils.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/27.
 */
@EViewGroup(R.layout.image_edit_item)
public class ImageEditItem extends FrameLayout{

    @ViewById(R.id.icon)
    ImageView mIcon;
    @ViewById(R.id.title)
    TextView mTitle;

    public ImageEditItem(Context context) {
        super(context);
    }

    public ImageEditItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LocalDisplay.dp2px(100), ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        setBackgroundColor(Color.parseColor("#4A4A4D"));
    }

    public void bind(String title){
        mTitle.setText(title);
    }

}
