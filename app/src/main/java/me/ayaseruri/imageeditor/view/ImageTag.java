package me.ayaseruri.imageeditor.view;

import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import me.ayaseruri.imageeditor.utils.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/30.
 */
public class ImageTag extends FrameLayout{

    private static final int SIZE_TEXT = 22;
    private static final int SIZE_SPACE_PADDING = 4;
    private static final int SIZE_TOTAL_HEIGHT = 3 * SIZE_TEXT + 4 * SIZE_SPACE_PADDING;

    private String mTitle, mPrice, mLocation;
    private int mStartPadding;
    private AnimatedPathView animatedPathView;

    public ImageTag(Context context) {
        super(context);
        init();
    }

    public ImageTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        animatedPathView = new AnimatedPathView(getContext());
        mStartPadding = LocalDisplay.dp2px(SIZE_TEXT);
    }

    public void createTag(final String tagTitle, final String price, final String location){
        mTitle = tagTitle;
        mPrice = price;
        mLocation = location;

        int max = Math.max(Math.max(mTitle.length(), mPrice.length()/2), mLocation.length());
        final int width = max * LocalDisplay.dp2px(SIZE_TEXT) + mStartPadding;
        final int height = LocalDisplay.dp2px(SIZE_TOTAL_HEIGHT);

        addView(animatedPathView, new LayoutParams(width, height));

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);

                Path path = new Path();

                animatedPathView.setStartPoint(16, LocalDisplay.dp2px(SIZE_SPACE_PADDING) * 2 + LocalDisplay.dp2px(SIZE_TEXT) * 2);

                //第一行 title
                path.moveTo(16, LocalDisplay.dp2px(SIZE_SPACE_PADDING) * 2 + LocalDisplay.dp2px(SIZE_TEXT) * 2);
                path.lineTo(mStartPadding, LocalDisplay.dp2px(SIZE_TEXT + SIZE_SPACE_PADDING));
                path.lineTo(mStartPadding + tagTitle.length() * LocalDisplay.dp2px(SIZE_TEXT), LocalDisplay.dp2px(SIZE_TEXT + SIZE_SPACE_PADDING));

                //第二行 价格
                path.moveTo(16, LocalDisplay.dp2px(SIZE_SPACE_PADDING) * 2 + LocalDisplay.dp2px(SIZE_TEXT) * 2);
                path.lineTo(mStartPadding + price.length() * LocalDisplay.dp2px(SIZE_TEXT/2), LocalDisplay.dp2px(SIZE_SPACE_PADDING) * 2 + LocalDisplay.dp2px(SIZE_TEXT) * 2);

                //第三行 购买地
                path.moveTo(16, LocalDisplay.dp2px(SIZE_SPACE_PADDING) * 2 + LocalDisplay.dp2px(SIZE_TEXT) * 2);
                path.lineTo(mStartPadding, height - LocalDisplay.dp2px(animatedPathView.getStrokeWidth()));
                path.lineTo(mStartPadding + location.length() * LocalDisplay.dp2px(SIZE_TEXT)
                        , height - LocalDisplay.dp2px(animatedPathView.getStrokeWidth()));

                animatedPathView.setPath(path);

                animatedPathView.clearnText();
                animatedPathView.addText(tagTitle, mStartPadding, LocalDisplay.dp2px(SIZE_TEXT + SIZE_SPACE_PADDING));
                animatedPathView.addText(price, mStartPadding, LocalDisplay.dp2px(SIZE_SPACE_PADDING) * 2 + LocalDisplay.dp2px(SIZE_TEXT) * 2);
                animatedPathView.addText(location, mStartPadding, height - LocalDisplay.dp2px(animatedPathView.getStrokeWidth()));
            }
        });

        requestLayout();
    }

    public void clearText(){
        animatedPathView.clearnText();
    }

    public void setTitle(String title) {
        mTitle = title;
        animatedPathView.addText(mTitle, mStartPadding, LocalDisplay.dp2px(SIZE_TEXT + SIZE_SPACE_PADDING));
        requestLayout();
    }

    public void setPrice(String price) {
        mPrice = price;
        animatedPathView.addText(mPrice, mStartPadding, LocalDisplay.dp2px(SIZE_SPACE_PADDING) * 2 + LocalDisplay.dp2px(SIZE_TEXT) * 2);
        requestLayout();
    }

    public void setLocation(String location) {
        mLocation = location;
        animatedPathView.addText(mLocation, mStartPadding, LocalDisplay.dp2px(SIZE_TOTAL_HEIGHT) - LocalDisplay.dp2px(animatedPathView.getStrokeWidth()));
        requestLayout();
    }

    public void setPercentage(float p){
        animatedPathView.setPercentage(p);
    }

    public int getCenterY() {
        return LocalDisplay.dp2px(SIZE_SPACE_PADDING) * 2 + LocalDisplay.dp2px(SIZE_TEXT) * 2;
    }
}
