package me.ayaseruri.imageeditor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.ayaseruri.imageeditor.R;
import me.ayaseruri.imageeditor.utils.LocalDisplay;

/**
 * Created by Matt
 */
public class AnimatedPathView extends View {

    private Paint mPaint;
    private Paint mShadowPaint;

    private Path mPath;
    private int mStrokeColor;
    private float mStrokeWidth;

    private float mProgress = 0f;
    private float mPathLength = 0f;
    private float mTextSize = 0f;

    private RectF mStartRectF;

    private List<DrawTextInfo> mDrawTextInfos;
    private PathEffect mPathEffect;

    public AnimatedPathView(Context context) {
        this(context, null);
        init();
    }

    public AnimatedPathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public AnimatedPathView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedPathView);
        mStrokeColor = a.getColor(R.styleable.AnimatedPathView_strokeColor, Color.WHITE);
        mStrokeWidth = a.getFloat(R.styleable.AnimatedPathView_strokeWidth, 4.0f);
        a.recycle();

        init();
    }

    private void init(){
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mDrawTextInfos = new ArrayList<>();
        mTextSize = LocalDisplay.dp2px(20);

        mPaint = new Paint();
        mPaint.setColor(mStrokeColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);

        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.parseColor("#999999"));
        mShadowPaint.setMaskFilter(new BlurMaskFilter(4, BlurMaskFilter.Blur.SOLID));
        mShadowPaint.setTextSize(mTextSize);
        mShadowPaint.setAntiAlias(true);

        setPath(new Path());
    }

    public void setPath(Path p){
        mPath = p;
        PathMeasure measure = new PathMeasure(mPath, false);
        mPathLength = measure.getLength();
    }

    /**
     * Set the drawn path using an array of array of floats. First is x parameter, second is y.
     * @param points The points to set on
     */
    public void setPath(float[]... points){
        if(points.length == 0)
            throw new IllegalArgumentException("Cannot have zero points in the line");

        Path p = new Path();
        p.moveTo(points[0][0], points[0][1]);

        for(int i=1; i < points.length; i++){
            p.lineTo(points[i][0], points[i][1]);
        }

        setPath(p);
    }

    public void setPercentage(float percentage){
        if(percentage < 0.0f || percentage > 1.0f)
            throw new IllegalArgumentException("setPercentage not between 0.0f and 1.0f");

        mProgress = percentage;
        mPathEffect = new DashPathEffect(new float[]{mPathLength, mPathLength}, (mPathLength - mPathLength * mProgress));
        invalidate();
    }

    public void scalePathBy(float x, float y){
        Matrix m = new Matrix();
        m.postScale(x, y);
        mPath.transform(m);
        PathMeasure measure = new PathMeasure(mPath, false);
        mPathLength = measure.getLength();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStyle(Paint.Style.STROKE);
        mShadowPaint.setStyle(Paint.Style.STROKE);

        mPaint.setStrokeWidth(mStrokeWidth);
        mShadowPaint.setStrokeWidth(mStrokeWidth);

        mPaint.setPathEffect(mPathEffect);
        mShadowPaint.setPathEffect(mPathEffect);

        canvas.clipRect(0, 0, getWidth() * mProgress, getHeight());

        canvas.translate(getPaddingLeft(), getPaddingTop());
        canvas.drawPath(mPath, mShadowPaint);
        canvas.drawPath(mPath, mPaint);

        //开始画文字
        for (DrawTextInfo drawTextInfo : mDrawTextInfos){
            mPaint.setStyle(Paint.Style.FILL);
            mShadowPaint.setStyle(Paint.Style.FILL);

            mPaint.setStrokeWidth(mStrokeWidth/2);
            mShadowPaint.setStrokeWidth(mStrokeWidth/2);

            canvas.drawText(drawTextInfo.getTitle(), drawTextInfo.getX(), drawTextInfo.getBaseLine() - LocalDisplay.dp2px(4), mShadowPaint);
            canvas.drawText(drawTextInfo.getTitle(), drawTextInfo.getX(), drawTextInfo.getBaseLine() - LocalDisplay.dp2px(4), mPaint);
        }

        //开始画初始点
        if(null != mStartRectF){
            mPaint.setStrokeWidth(mStrokeWidth);
            mShadowPaint.setStrokeWidth(mStrokeWidth);

            canvas.drawOval(mStartRectF, mShadowPaint);
            canvas.drawOval(mStartRectF, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(widthMeasureSpec);

        int measuredWidth, measuredHeight;

        if(widthMode == MeasureSpec.AT_MOST)
            throw new IllegalStateException("AnimatedPathView cannot have a WRAP_CONTENT property");
        else
            measuredWidth = widthSize;

        if(heightMode == MeasureSpec.AT_MOST)
            throw new IllegalStateException("AnimatedPathView cannot have a WRAP_CONTENT property");
        else
            measuredHeight = heightSize;

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void addText(String s, float x, float baseline){
        mDrawTextInfos.add(new DrawTextInfo(s, x, baseline));
        invalidate();
    }

    public void clearnText(){
        mDrawTextInfos.clear();
        invalidate();
    }

    public void setStartPoint(int x, int y) {
        mStartRectF = new RectF(x - 8, y - 8, x + 8, y + 8);
    }

    public static class DrawTextInfo{
        private String title;
        private float x;
        private float baseLine;

        public DrawTextInfo(String title, float x, float baseLine) {
            this.title = title;
            this.x = x;
            this.baseLine = baseLine;
        }

        public String getTitle() {
            return title;
        }

        public float getX() {
            return x;
        }

        public float getBaseLine() {
            return baseLine;
        }
    }
}
