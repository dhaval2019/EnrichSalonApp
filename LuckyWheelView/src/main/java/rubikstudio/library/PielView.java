package rubikstudio.library;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import rubikstudio.library.model.LuckyItem;

/**
 * Created by kiennguyen on 11/5/16.
 */

public class PielView extends View {
    private RectF mRange = new RectF();
    private int mRadius;

    private Paint mArcPaint;
    private Paint mBackgroundPaint;
    private Paint mTextPaint;

    private float mStartAngle = 0;
    private int mCenter;
    private int mPadding;
    private int mTargetIndex;
    private int mRoundOfNumber = 4;
    private boolean isRunning = false;

    private int defaultBackgroundColor = -1;
    private Drawable drawableCenterImage;
    private int textColor = 0xffffffff;
    public  Animator animator;

    private List<LuckyItem> mLuckyItemList;
    Context context;

    private PieRotateListener mPieRotateListener;
    private ViewPropertyAnimator animateor;

    public interface PieRotateListener {
        void rotateDone(int index);
    }

    public PielView(Context context) {

        super(context);
        this.context=context;


    }

    public PielView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

    }

    public void setPieRotateListener(PieRotateListener listener) {
        this.mPieRotateListener = listener;
    }

    private void init() {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);

        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,26,
                getResources().getDisplayMetrics()));

        mRange = new RectF(mPadding, mPadding, mPadding+mRadius, mPadding+mRadius);
    }

    public void setData(List<LuckyItem> luckyItemList) {
        this.mLuckyItemList = luckyItemList;
        invalidate();
    }

    public void setPieBackgroundColor(int color) {
        defaultBackgroundColor = color;
        invalidate();
    }

    public void setPieCenterImage(Drawable drawable) {
        drawableCenterImage = drawable;
        invalidate();
    }

    public void setPieTextColor(int color) {
        textColor = color;
        invalidate();
    }

    private void drawPieBackgroundWithBitmap(Canvas canvas, Bitmap bitmap) {
        canvas.drawBitmap(bitmap, null, new Rect(mPadding/2,mPadding/2,
                getMeasuredWidth() - mPadding/2, getMeasuredHeight()-mPadding/2), null);
    }

    /**
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLuckyItemList == null) {
            return;
        }

        drawBackgroundColor(canvas, defaultBackgroundColor);

        init();

        float tmpAngle = mStartAngle;
        float sweepAngle = 360 / mLuckyItemList.size();
        Log.v(" sweep angle --",sweepAngle+"");
        Log.v("tmpAngle --",tmpAngle+"");



        for(int i = 0; i < mLuckyItemList.size(); i++) {
            mArcPaint.setColor(mLuckyItemList.get(i).color);
            canvas.drawArc(mRange, tmpAngle, sweepAngle, true, mArcPaint);

            drawText(canvas, tmpAngle, sweepAngle, mLuckyItemList.get(i).text);
            drawImage(canvas, tmpAngle);

            tmpAngle += sweepAngle;
        }

        //drawCenterImage(canvas, drawableCenterImage);
    }

    private void drawBackgroundColor(Canvas canvas, int color) {
        if (color == -1)
            return;
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(color);
        canvas.drawCircle(mCenter, mCenter, mCenter, mBackgroundPaint);
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());

        mPadding = getPaddingLeft() == 0 ? 10 : getPaddingLeft();
        mRadius = width - mPadding * 2;

        mCenter = width/2;

        setMeasuredDimension(width, width);
    }

    /**
     * @param canvas
     * @param tmpAngle
     */
    private void drawImage(Canvas canvas, float tmpAngle) {
        //int imgWidth = mRadius / mLuckyItemList.size();

        int imgWidth = 0;
        float angle = (float) ((tmpAngle + 360 / mLuckyItemList.size() / 2) * Math.PI / 180);

        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));

        Rect rect = new Rect(x - imgWidth/2, y - imgWidth/2, x + imgWidth/2, y + imgWidth/2);
        //canvas.drawBitmap(bitmap, null, rect, null);
    }

    private void drawCenterImage(Canvas canvas, Drawable drawable) {
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable);
        Bitmap bitmap = LuckyWheelUtils.drawableToBitmap(drawable);
        bitmap = Bitmap.createScaledBitmap(bitmap, 90, 90, false);
        canvas.drawBitmap(bitmap, getMeasuredWidth() / 2 - bitmap.getWidth() / 2, getMeasuredHeight() / 2 - bitmap.getHeight() / 2, null);
    }

    /**
     * @param canvas
     * @param tmpAngle
     * @param sweepAngle
     * @param mStr
     */
    private void drawText(Canvas canvas, float tmpAngle, float sweepAngle, String mStr) {

        Path path = new Path();
        path.addArc(mRange,tmpAngle,sweepAngle);
        float textWidth = mTextPaint.measureText(mStr);
        int hOffset = (int) (mRadius * Math.PI / mLuckyItemList.size()/2-textWidth/2);
        int vOffset = mRadius/2/4;
        Rect r = new Rect();
        mTextPaint.getTextBounds(mStr, 0, mStr.length(), r);

        int yPos = (Math.abs(r.height())) / 2;
        int xPos = (canvas.getWidth() / 2);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
        mTextPaint.setTypeface(typeface);

        canvas.drawTextOnPath(mStr, path, hOffset, vOffset, mTextPaint);

        //mTextPaint.setTextSize(45);
       //canvas.drawTextOnPath(".", path, xPos, yPos, mTextPaint);




//        Path path = new Path();
//        path.addArc(mRange,tmpAngle,sweepAngle);
//        float textWidth = mTextPaint.measureText(mStr);
//        int hOffset = (int) (mRadius * Math.PI / mLuckyItemList.size()/2-textWidth/2);
//        int vOffset = mRadius/2/4;
//        //canvas.drawTextOnPath(mStr, path, hOffset, vOffset, mTextPaint);
//        int x = 75;
//        int y = 185;
//        mTextPaint.setColor(Color.GRAY);
//        mTextPaint.setTextSize(25);
//        String rotatedtext = "Rotated helloandroid :)";
//
//
//
////Draw bounding rect before rotating text:
//
//        Rect rect = new Rect();
//        mTextPaint.getTextBounds(rotatedtext, 0, rotatedtext.length(), rect);
//        canvas.translate(x, y);
//        mTextPaint.setStyle(Paint.Style.FILL);
//
//        canvas.drawText(rotatedtext , 0, 0, mTextPaint);
//        mTextPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(rect, mTextPaint);
//
//        canvas.translate(-x, -y);
////
////
//        mTextPaint.setColor(Color.RED);
//        canvas.rotate(-45, x + rect.exactCenterX(),y + rect.exactCenterY());
//        mTextPaint.setStyle(Paint.Style.FILL);
//        canvas.drawTextOnPath(rotatedtext, path, hOffset, vOffset, mTextPaint);
//       //canvas.drawText(rotatedtext, hOffset, vOffset, mTextPaint);

    }

    /**
     * @return
     */
    private float getAngleOfIndexTarget() {
        int tempIndex = mTargetIndex == 0 ? 1 : mTargetIndex;
        return  (360 / mLuckyItemList.size()) * tempIndex;
    }

    /**
     * @param numberOfRound
     */
    public void setRound(int numberOfRound) {
        mRoundOfNumber = numberOfRound;
    }

    /**
     * @param index
     */
    public void rotateTo(int index) {
        if (isRunning) {
            return;
        }
        mTargetIndex = index;
        setRotation(0);
        float targetAngle = 360 * mRoundOfNumber + 270 - getAngleOfIndexTarget() + (360 / mLuckyItemList.size()) / 2;
        animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(mRoundOfNumber * 1000 + 900L)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunning = false;
                        if (mPieRotateListener != null) {
                            mPieRotateListener.rotateDone(mTargetIndex);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                })
                .rotation(5000)
                .start();
    }




    public void startAnimation(int index) {
        if (isRunning) {
            return;
        }
        mTargetIndex = index;
        setRotation(60000);
        animateor=animate();
        //Toast.makeText(context,getAngleOfIndexTarget()+"",Toast.LENGTH_SHORT).show();
        float targetAngle = 360 * mRoundOfNumber + 90 - getAngleOfIndexTarget() + (360 / mLuckyItemList.size()) / 2;
        //float targetAngle = 270 * mRoundOfNumber + 270 - getAngleOfIndexTarget() + (270 / mLuckyItemList.size()) / 2;

        Log.v("targetAngle--",getAngleOfIndexTarget()+"");

        animateor
                .setInterpolator(new DecelerateInterpolator())
                //.setDuration(mRoundOfNumber * 1000 + 900L)
                 .setDuration(60000)
                //.setDuration(mRoundOfNumber * 9000 + 1000L)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunning = false;
                        if (mPieRotateListener != null) {
                            mPieRotateListener.rotateDone(mTargetIndex);
                       }
                        animation.removeAllListeners();
                        clearAnimation();


                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .rotation(targetAngle)
                .start();
    }

    /**
     * @param index
     */
    public void stopTo(int index) {
        mTargetIndex = index;
        setRotation(5000);

        // mPieRotateListener.rotateDone(mTargetIndex);
        //Toast.makeText(context,getAngleOfIndexTarget()+"",Toast.LENGTH_SHORT).show();

        float targetAngle = 360 * mRoundOfNumber + 90 - getAngleOfIndexTarget() + (360 / mLuckyItemList.size()) / 2;
        Log.v("targetAngle--",getAngleOfIndexTarget()+"");
        animateor
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(mRoundOfNumber * 1000 + 900L)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunning = false;
                        if (mPieRotateListener != null) {
                            mPieRotateListener.rotateDone(mTargetIndex);
                            animation.removeAllListeners();
                            //clearAnimation();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                })
                .rotation(targetAngle)
                .start();
        //  animate().cancel();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
       return false;
    }


}

