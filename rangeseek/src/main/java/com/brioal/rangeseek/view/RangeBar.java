package com.brioal.rangeseek.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.brioal.rangeseek.R;
import com.brioal.rangeseek.entity.RangeEntity;
import com.brioal.rangeseek.interfaces.OnRangeChangedListener;
import com.brioal.rangeseek.util.SizeUtil;

import java.util.List;

/**
 * Range Select Bar
 * Created by Brioal on 2016/9/14.
 */
public class RangeBar extends View {

    private List<RangeEntity> mList;
    private int mHeight = 0;
    private int mWidth = 0;
    private int mLineWidth; //线条宽度
    private int mLineColor; //线条颜色

    private int mCircleRadius; //圆形的半径
    private int mCircleWidth; //圆形的线条宽度
    private int mCircleColor; //圆形的线条颜色

    private int mCenterColor; //选中圆形的颜色

    private int mPointRadius = 0; //角标半径
    private int mMaxPointRadius; //角标半径
    private int mPointColor;//角标颜色

    private Paint mPaint;
    private int mTextSize; //文字大小
    private int mTextColor; //文字颜色

    private int mStartIndex; //起始点下标
    private int mEndIndex;//终止点下标
    private float mStartX; //起始点坐标
    private float mEndX; //终止点坐标
    private float mSingleWidth = 0; //单个Point所占的宽度
    private OnRangeChangedListener mListener;


    public RangeBar(Context context) {
        this(context, null);
    }

    public RangeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    //设置事件监听器
    public void addOnRangeChangedListener(OnRangeChangedListener listener) {
        mListener = listener;
    }

    //设置线条颜色
    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
        invalidate();
    }

    //设置线条宽度
    public void setLineWidth(int lineWidth) {
        mLineWidth = lineWidth;
        invalidate();
    }

    //设置外圆颜色
    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        invalidate();

    }

    //设置圆形半径
    public void setCircleRadius(int circleRadius) {
        mCircleRadius = circleRadius;
        invalidate();

    }

    //设置圆形的线条宽度
    public void setCircleWidth(int circleWidth) {
        mCircleWidth = circleWidth;
        invalidate();

    }

    //设置中心填充颜色
    public void setCenterColor(int centerColor) {
        mCenterColor = centerColor;
        invalidate();

    }

    //设置游标颜色
    public void setPointColor(int pointColor) {
        mPointColor = pointColor;
        invalidate();

    }

    //设置下标显示颜色
    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidate();

    }

    public void setValues(List<RangeEntity> list) {
        mList = list;
        mStartIndex = 0;
        mEndIndex = mList.size() - 1;
        int count = mList.size();
        mSingleWidth = (float) (mWidth * 1.0 / count);
        mStartX = mStartIndex * mSingleWidth + mSingleWidth / 2;
        mEndX = mEndIndex * mSingleWidth + mSingleWidth / 2;
    }


    private void init() {
        mLineWidth = (int) SizeUtil.Dp2Px(getContext(), 5);
        mLineColor = Color.BLACK;

        mCircleColor = Color.BLACK;
        mCircleRadius = (int) SizeUtil.Dp2Px(getContext(), 11);
        mCircleWidth = (int) SizeUtil.Dp2Px(getContext(), 1);

        mCenterColor = getResources().getColor(R.color.colorPoint);

        mMaxPointRadius = (int) SizeUtil.Dp2Px(getContext(), 16);
        mPointColor = getResources().getColor(R.color.colorPoint);

        mTextSize = (int) SizeUtil.Sp2Px(getContext(), 15);
        mTextColor = Color.BLACK;

        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showPoint();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                hidePoint();
                getRightX();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((Math.abs(mStartX - x) <= mSingleWidth / 2) && (Math.abs(mHeight * 2 / 3 - y) <= mHeight / 3)) {
                    //处于点击开始点的状态
                    mStartX = x;
                }
                if ((Math.abs(mEndX - x) <= mSingleWidth / 2) && (Math.abs(mHeight * 2 / 3 - y) <= mHeight / 3)) {
                    //处于点击开始点的状态
                    mEndX = x;
                }
                getRightIndex();
                invalidate();
                break;
        }

        return true;
    }

    //显示Point
    public void showPoint() {
        final ValueAnimator animator = ValueAnimator.ofInt(0, mMaxPointRadius);
        animator.setDuration(200);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPointRadius = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    //显示Point
    public void hidePoint() {
        final ValueAnimator animator = ValueAnimator.ofInt(mMaxPointRadius, 0);
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPointRadius = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    public void moveToRightX(final boolean isStart, float start, float end) {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isStart) {
                    mStartX = (float) animation.getAnimatedValue();
                } else {
                    mEndX = (float) animation.getAnimatedValue();
                }
                invalidate();
            }
        });
        animator.start();
    }


    //获取正确的X坐标
    public void getRightX() {
        getRightIndex();
        moveToRightX(true, mStartX, mStartIndex * mSingleWidth + mSingleWidth / 2);
        moveToRightX(false, mEndX, mEndIndex * mSingleWidth + mSingleWidth / 2);
    }

    //获取正确的下标
    public void getRightIndex() {
        if (mStartX % mSingleWidth > mSingleWidth) {
            mStartIndex = (int) (mStartX / mSingleWidth) + 1;
        } else {
            mStartIndex = (int) (mStartX / mSingleWidth);
        }
        if (mEndX % mSingleWidth > mSingleWidth) {
            mEndIndex = (int) (mEndX / mSingleWidth) + 1;
        } else {
            mEndIndex = (int) (mEndX / mSingleWidth);
        }
        if (mStartIndex == mList.size() - 1) {
            mStartIndex = mList.size() - 2;
        }
        if (mStartIndex == mEndIndex) {
            mEndIndex = mStartIndex + 1;
        }
        if (mListener != null) {
            mListener.selected(mStartIndex, mEndIndex);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = (int) (SizeUtil.Dp2Px(getContext(), 100) + getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(mWidth, mHeight);
        int count = mList.size();
        mSingleWidth = (float) (mWidth * 1.0 / count);

        mStartX = mStartIndex * mSingleWidth + mSingleWidth / 2;
        mEndX = mEndIndex * mSingleWidth + mSingleWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mList != null && mList.size() > 2) { //存在内容,则绘制
            //绘制黑色线条
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mLineWidth);
            mPaint.setColor(mLineColor);
            canvas.drawLine(mSingleWidth / 2, mHeight * 2 / 3, mWidth - mSingleWidth / 2, mHeight * 2 / 3, mPaint);
            for (int i = 0; i < mList.size(); i++) {
                RangeEntity entity = mList.get(i);
                canvas.save();
                canvas.translate(i * mSingleWidth + mSingleWidth / 2, mHeight * 2 / 3);
                //绘制空白圆点
                mPaint.setColor(Color.WHITE);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(0, 0, mCircleRadius, mPaint);

                //绘制空心圆
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(mCircleWidth);
                mPaint.setColor(mCircleColor);
                canvas.drawCircle(0, 0, mCircleRadius, mPaint);

                //绘制文字
                Rect rect = new Rect();
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setTextSize(mTextSize);
                mPaint.setColor(mTextColor);
                String text = entity.getTitle();
                mPaint.getTextBounds(entity.getTitle(), 0, entity.getTitle().length(), rect);
                canvas.drawText(text, -(rect.right + rect.left) / 2, mCircleRadius + 18 + (rect.bottom - rect.top) / 2, mPaint);
                canvas.restore();
            }
            for (int i = 0; i < mList.size(); i++) {
                if (i == mStartIndex || i == mEndIndex) {
                    float x = 0;
                    if (i == mStartIndex) {
                        x = mStartX;
                    } else if (i == mEndIndex) {
                        x = mEndX;
                    }
                    //如果是起始点或者终止点,则绘制圆点
                    if (i == mStartIndex || i == mEndIndex) {
                        String text = mList.get(i).getTitle();
                        mPaint.setStyle(Paint.Style.FILL);
                        mPaint.setColor(mCenterColor);
                        canvas.drawCircle(x, mHeight * 2 / 3, mCircleRadius - 5, mPaint);
                    }
                    String text = mList.get(i).getTitle();
                    //绘制角标
                    canvas.save();
                    canvas.translate(x, 10 + mPointRadius);
                    mPaint.setColor(mPointColor);
                    canvas.drawCircle(0, 0, mPointRadius, mPaint);
                    //绘制文字
                    mPaint.setTextSize(mPointRadius - 5 < 0 ? 0 : mPointRadius - 5);
                    Rect rect = new Rect();
                    mPaint.getTextBounds(text, 0, text.length(), rect);
                    mPaint.setColor(Color.WHITE);
                    canvas.drawText(text, -(rect.right + rect.left) / 2, (rect.bottom - rect.top) / 2, mPaint);
                    canvas.restore();
                    //绘制三角
                    mPaint.setColor(mPointColor);
                    canvas.save();
                    canvas.translate(x, 10 + mPointRadius * 2 - 8);
                    Path path = new Path();
                    path.moveTo(-mPointRadius * 2 / 3, 0);
                    path.lineTo(0, mPointRadius * 4 / 3 / 2);
                    path.lineTo(mPointRadius * 2 / 3, 0);
                    path.close();
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawPath(path, mPaint);
                    canvas.restore();
                }
            }

        }
    }
}
