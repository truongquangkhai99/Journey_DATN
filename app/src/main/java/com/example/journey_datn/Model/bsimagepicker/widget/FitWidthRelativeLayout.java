package com.example.journey_datn.Model.bsimagepicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.journey_datn.R;

public class FitWidthRelativeLayout extends RelativeLayout {
    protected float aspectRatio;

    public FitWidthRelativeLayout(Context context) {
        super(context);
    }

    public FitWidthRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AspectRatioView, 0, 0);
        try {
            aspectRatio = a.getFloat(R.styleable.AspectRatioView_view_aspectRatio, 0);
        } finally {
            a.recycle();
        }
    }

    public FitWidthRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (aspectRatio > 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = Math.round(MeasureSpec.getSize(widthMeasureSpec) / aspectRatio);
            int finalWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            int finalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            super.onMeasure(finalWidthMeasureSpec, finalHeightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
