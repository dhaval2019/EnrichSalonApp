package com.enrich.salonapp.util.supertoast.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;

import com.enrich.salonapp.util.supertoast.Style;
import com.enrich.salonapp.util.supertoast.SuperActivityToast;
import com.enrich.salonapp.util.supertoast.SuperToast;
import com.enrich.salonapp.util.supertoast.Toaster;


public class AnimationUtils {

    public static final long SHOW_DURATION = 250;
    public static final long HIDE_DURATION = 250;

    private static final String ALPHA = "alpha";
    private static final String TRANSLATION_X = "translationX";
    private static final String TRANSLATION_Y = "translationY";
    private static final String SCALE_X = "scaleX";
    private static final String SCALE_Y = "scaleY";

    /**
     * Returns the corresponding system animation reference for a
     * particular {@link Style.Animations} reference.
     * This is used by the {@link SuperToast}
     * {@link android.view.WindowManager}.
     *
     * @param animations The desired {@link Style.Animations} constant
     * @return The corresponding system animation reference
     */
    public static int getSystemAnimationsResource(@Style.Animations int animations) {
        switch (animations) {
            case Style.ANIMATIONS_FADE: return android.R.style.Animation_Toast;
            case Style.ANIMATIONS_FLY: return android.R.style.Animation_Translucent;
            case Style.ANIMATIONS_SCALE: return android.R.style.Animation_Dialog;
            case Style.ANIMATIONS_POP: return android.R.style.Animation_InputMethod;
            default: return android.R.style.Animation_Toast;
        }
    }

    /**
     * Returns the corresponding {@link Animator} for a particular
     * {@link Style.Animations} reference.
     * This is used by the {@link Toaster}
     * when showing a {@link SuperActivityToast} and has no purpose being called directly.
     *
     * @param superActivityToast The SuperActivityToast being animated
     * @return The corresponding Animator
     */
    public static Animator getShowAnimation(SuperActivityToast superActivityToast) {

        final PropertyValuesHolder propertyValuesHolderAlpha = PropertyValuesHolder
                .ofFloat(ALPHA, 0f, 1f);
        switch (superActivityToast.getAnimations()) {
            case Style.ANIMATIONS_FADE:
                return ObjectAnimator.ofPropertyValuesHolder(superActivityToast.getView(),
                        propertyValuesHolderAlpha)
                        .setDuration(SHOW_DURATION);

            case Style.ANIMATIONS_FLY:
                final PropertyValuesHolder propertyValuesHolderX = PropertyValuesHolder.
                        ofFloat(TRANSLATION_X, -500f, 0f);
                return ObjectAnimator.ofPropertyValuesHolder(superActivityToast.getView(),
                        propertyValuesHolderX,
                        propertyValuesHolderAlpha).setDuration(SHOW_DURATION);

            case Style.ANIMATIONS_SCALE:
                final PropertyValuesHolder propertyValuesHolderScaleX = PropertyValuesHolder
                        .ofFloat(SCALE_X, 0f, 1f);
                final PropertyValuesHolder propertyValuesHolderScaleY = PropertyValuesHolder
                        .ofFloat(SCALE_Y, 0f, 1f);
                return ObjectAnimator.ofPropertyValuesHolder(superActivityToast.getView(),
                        propertyValuesHolderScaleX,
                        propertyValuesHolderScaleY, propertyValuesHolderAlpha)
                        .setDuration(SHOW_DURATION);

            case Style.ANIMATIONS_POP:
                final PropertyValuesHolder propertyValuesHolderY = PropertyValuesHolder
                        .ofFloat(TRANSLATION_Y, 250f, 0f);
                return ObjectAnimator.ofPropertyValuesHolder(superActivityToast.getView(),
                        propertyValuesHolderY,
                        propertyValuesHolderAlpha).setDuration(SHOW_DURATION);

            default:
                return ObjectAnimator.ofPropertyValuesHolder(superActivityToast.getView(),
                        propertyValuesHolderAlpha)
                        .setDuration(SHOW_DURATION);
        }
    }

    /**
     * Returns the corresponding {@link Animator} for a particular
     * {@link Style.Animations} reference.
     * This is used when hiding a {@link SuperActivityToast}.
     *
     * @param superActivityToast {@link SuperActivityToast}
     * @return The corresponding Animator
     */
    public static Animator getHideAnimation(SuperActivityToast superActivityToast) {

        final PropertyValuesHolder propertyValuesHolderAlpha = PropertyValuesHolder.ofFloat(ALPHA, 1f, 0f);
        switch (superActivityToast.getAnimations()) {
            case Style.ANIMATIONS_FADE:
                return ObjectAnimator.ofPropertyValuesHolder(superActivityToast.getView(), propertyValuesHolderAlpha)
                        .setDuration(SHOW_DURATION);

            case Style.ANIMATIONS_FLY:
                final PropertyValuesHolder propertyValuesHolderX = PropertyValuesHolder.ofFloat(TRANSLATION_X, 0f, 500f);
                return ObjectAnimator.ofPropertyValuesHolder(superActivityToast.getView(), propertyValuesHolderX,
                        propertyValuesHolderAlpha).setDuration(SHOW_DURATION);

            case Style.ANIMATIONS_SCALE:
                final PropertyValuesHolder propertyValuesHolderScaleX = PropertyValuesHolder.ofFloat(SCALE_X, 1f, 0f);
                final PropertyValuesHolder propertyValuesHolderScaleY = PropertyValuesHolder.ofFloat(SCALE_Y, 1f, 0f);
                return ObjectAnimator.ofPropertyValuesHolder(superActivityToast.getView(), propertyValuesHolderScaleX,
                        propertyValuesHolderScaleY, propertyValuesHolderAlpha).setDuration(SHOW_DURATION);

            case Style.ANIMATIONS_POP:
                final PropertyValuesHolder propertyValuesHolderY = PropertyValuesHolder.ofFloat(TRANSLATION_Y, 0f, 250f);
                return ObjectAnimator.ofPropertyValuesHolder(superActivityToast.getView(), propertyValuesHolderY,
                        propertyValuesHolderAlpha).setDuration(SHOW_DURATION);

            default:
                return ObjectAnimator.ofPropertyValuesHolder(superActivityToast.getView(), propertyValuesHolderAlpha)
                        .setDuration(SHOW_DURATION);
        }
    }
}
