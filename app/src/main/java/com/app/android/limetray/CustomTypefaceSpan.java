package com.app.android.limetray;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * Created by blackadmin on 1/7/14.
 */
public class CustomTypefaceSpan extends TypefaceSpan {

    private final Typeface newType;
    private static float fontSize = 0.0F;

    public CustomTypefaceSpan(String family, Typeface type) {
        this(family, type, 0.0F);
    }

    public CustomTypefaceSpan(String family, Typeface type, float fontSize) {
        super(family);
        newType = type;
        this.fontSize = fontSize;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        if(0.0F != fontSize) {
            paint.setTextSize(fontSize);
        }
        paint.setTypeface(tf);
    }
}