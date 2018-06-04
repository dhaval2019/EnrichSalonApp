package com.enrich.salonapp.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.enrich.salonapp.R;

public class Animations {

    public static void expand(final View v, int duration) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(duration);
        v.startAnimation(a);
    }

    public static void sideExpand(final View v, int duration) {
        v.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        final int targetHeight = v.getMeasuredHeight();
        final int targetWidth = v.getMeasuredWidth();

        v.getLayoutParams().width = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().width = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetWidth * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(duration);

        v.startAnimation(a);
    }

    public static void collapse(final View v, int duration) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(duration);
        v.startAnimation(a);
    }

    public static void collapse(final View v, int duration, final UpdateListener updateListener) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            boolean updateListenerCalled = true;

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    if (updateListener != null && updateListenerCalled) {
                        updateListenerCalled = false;
                        v.getLayoutParams().height = initialHeight;
                        v.requestLayout();
                        updateListener.onUpdate();
                        return;
                    }
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(duration);
        v.startAnimation(a);
    }

    public static void sideCollapse(final View v, int duration) {
//        final int initialHeight = v.getMeasuredHeight();
        final int initialWidth = v.getMeasuredWidth();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialWidth - (int) (initialWidth * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(duration);
        v.startAnimation(a);
    }

    public static void fabButtonMinimize(final View view, int duration) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(scaleAnimation);

    }


    public static void fabButtonMaximize(final View view, int duration) {
        view.setVisibility(View.VISIBLE);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        view.startAnimation(scaleAnimation);
    }

    public static void fabMenuOpenAnim(Context context, final View view, View fabBackground, int duration) {
        ViewGroup fabContainer = (ViewGroup) view.getParent();
        for (int i = 1; i < fabContainer.getChildCount(); i++)  // -1 is to exclude Menu Icon itself
            fabContainer.getChildAt(i).setVisibility(View.VISIBLE);
        fabBackground.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fab_menu_open);
//        RotateAnimation animation = new RotateAnimation(0, 45);
        animation.setDuration(duration);
        view.startAnimation(animation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(duration - 100);
        alphaAnimation.setStartOffset(50);
        fabBackground.startAnimation(alphaAnimation);
    }

    public static void fabMenuCloseAnim(Context context, final View view, final View fabBackground, int duration) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fab_menu_close);
        animation.setDuration(duration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fabBackground.setVisibility(View.GONE);
                ViewGroup fabContainer = (ViewGroup) view.getParent();
                for (int i = 1; i < fabContainer.getChildCount(); i++)
                    fabContainer.getChildAt(i).setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(duration);
        fabBackground.startAnimation(alphaAnimation);
        view.startAnimation(animation);

    }

    public static void fabTextAppear(final View view, int duration) {
        view.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(duration);

        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animationSet.addAnimation(ta);

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setStartOffset(duration / 2);
        aa.setDuration(duration / 2);
        animationSet.addAnimation(aa);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                view.setAlpha(1);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animationSet);

    }

    public static void fabTextDisappear(final View view, int duration) {

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setDuration(duration);

        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.4f,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animationSet.addAnimation(ta);

        AlphaAnimation aa = new AlphaAnimation(1, 0);
        aa.setDuration(duration / 4);
        animationSet.addAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        view.startAnimation(animationSet);
    }
}
