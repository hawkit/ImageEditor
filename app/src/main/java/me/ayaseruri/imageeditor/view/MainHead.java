package me.ayaseruri.imageeditor.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EView;

import java.io.File;

import me.ayaseruri.imageeditor.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by wufeiyang on 16/5/26.
 */
@EView
public class MainHead extends ImageView {

    private String mPath;

    public MainHead(Context context) {
        super(context);
    }

    public MainHead(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setScaleType(ScaleType.CENTER_CROP);
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
                setImageBitmap(bitmap);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }

    public String getPath() {
        return mPath;
    }
}
