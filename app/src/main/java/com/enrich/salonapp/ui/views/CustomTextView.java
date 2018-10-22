package com.enrich.salonapp.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.enrich.salonapp.R;
import com.enrich.salonapp.util.Constants;

/**
 * Created by varunbarve on 16/09/17.
 */

public class CustomTextView extends AppCompatTextView {

    public static Typeface BLACK_TTF;
    public static Typeface BOLD_TTF;
    public static Typeface EXTRA_BOLD_TTF;
    public static Typeface LIGHT_TTF;
    public static Typeface REGULAR_TTF;
    public static Typeface SEMI_BOLD_TTF;
    public static Typeface ULTRA_LIGHT_TTF;
    public static Typeface ITALIC_TTF;

    public CustomTextView(Context context) {
        super(context);
        setCustomTypeFace(context, 6);
    }

    public CustomTextView(Context context, AttributeSet attr) {
        super(context, attr);
        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.CustomTextView);
        if (a.hasValue(R.styleable.CustomTextView_font_type)) {
            int value = a.getInt(R.styleable.CustomTextView_font_type, 0);
            setCustomTypeFace(context, value);
        }
        if (a.hasValue(R.styleable.CustomTextView_strike_text)) {
            if (a.getBoolean(R.styleable.CustomTextView_strike_text, false))
                setPaintFlags(getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public int setCustomTypeFace(Context context, int isBold) {
        try {
            if (!isInEditMode()) {
                if (isBold == 1)
                    setTypeface(getBlackTtf(context), Typeface.NORMAL);
                else if (isBold == 2)
                    setTypeface(getBoldTtf(context), Typeface.NORMAL);
                else if (isBold == 3)
                    setTypeface(getExtraBoldTtf(context), Typeface.NORMAL);
                else if (isBold == 4)
                    setTypeface(getLightTtf(context), Typeface.NORMAL);
                else if (isBold == 5)
                    setTypeface(getRegularTtf(context), Typeface.NORMAL);
                else if (isBold == 6)
                    setTypeface(getSemiBoldTtf(context), Typeface.NORMAL);
                else if (isBold == 7)
                    setTypeface(getUltraLightTtf(context), Typeface.NORMAL);
                else if (isBold == 8)
                    setTypeface(getItalicTtf(context), Typeface.NORMAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isBold;
    }

    public static Typeface getBlackTtf(Context context) {
        if (BLACK_TTF == null)
            BLACK_TTF = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + Constants.BLACK_FONT);
        return BLACK_TTF;
    }

    public static Typeface getBoldTtf(Context context) {
        if (BOLD_TTF == null)
            BOLD_TTF = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + Constants.BOLD_FONT);
        return BOLD_TTF;
    }

    public static Typeface getExtraBoldTtf(Context context) {
        if (EXTRA_BOLD_TTF == null)
            EXTRA_BOLD_TTF = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + Constants.EXTRA_BOLD_FONT);
        return EXTRA_BOLD_TTF;
    }

    public static Typeface getLightTtf(Context context) {
        if (LIGHT_TTF == null)
            LIGHT_TTF = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + Constants.LIGHT_FONT);
        return LIGHT_TTF;
    }

    public static Typeface getRegularTtf(Context context) {
        if (REGULAR_TTF == null)
            REGULAR_TTF = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + Constants.REGULAR_FONT);
        return REGULAR_TTF;
    }

    public static Typeface getSemiBoldTtf(Context context) {
        if (SEMI_BOLD_TTF == null)
            SEMI_BOLD_TTF = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + Constants.SEMI_BOLD_FONT);
        return SEMI_BOLD_TTF;
    }

    public static Typeface getUltraLightTtf(Context context) {
        if (ULTRA_LIGHT_TTF == null)
            ULTRA_LIGHT_TTF = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + Constants.ULTRA_LIGHT_FONT);
        return ULTRA_LIGHT_TTF;
    }

    public static Typeface getItalicTtf(Context context) {
        if (ULTRA_LIGHT_TTF == null)
            ULTRA_LIGHT_TTF = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + Constants.ITALIC_FONT);
        return ULTRA_LIGHT_TTF;
    }
}