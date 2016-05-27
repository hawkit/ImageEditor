package me.ayaseruri.imageeditor.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.io.File;

import me.ayaseruri.imageeditor.R;
import me.ayaseruri.imageeditor.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by wufeiyang on 16/5/26.
 */
@EViewGroup(R.layout.main_image_item)
public class MainImageListItem extends FrameLayout {

    @ViewById(R.id.image)
    ImageView mImageView;
    @ColorRes(R.color.colorPrimary)
    int mColorChecked;
    @ColorRes(android.R.color.transparent)
    int mColorNormal;

    private boolean isChecked;
    private String mPath;

    public MainImageListItem(Context context) {
        super(context);
    }

    public MainImageListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(final String path){
        mPath = path;
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                File imageFile = new File(mPath);
                if(imageFile.exists()){
                    subscriber.onNext(BitmapFactory.decodeFile(mPath));
                    subscriber.onCompleted();
                }else {
                    subscriber.onError(new Exception("图片不存在"));
                }
            }
        }).compose(RxUtils.<Bitmap>applySchedulers()).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                mImageView.setImageBitmap(bitmap);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }

    public void setchecked(boolean checked){
        isChecked = checked;
        int color;
        if(isChecked){
            color = mColorChecked;
        }else {
            color = mColorNormal;
        }
        setBackgroundColor(color);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getPath() {
        return mPath;
    }
}
