package com.olamide.findartt.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.olamide.findartt.R;

public class MaxHeightNestedScrollView extends NestedScrollView {
    private int maxHeight;
    private int defaultMaxHeight = 250;

    public MaxHeightNestedScrollView(Context context) {
        super(context);
    }

    public MaxHeightNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }

    }

    public MaxHeightNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

//    public MaxHeightNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        if (!isInEditMode()) {
//            init(context, attrs);
//        }
//    }


    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightNestedScrollView);
            maxHeight = styledAttrs.getDimensionPixelSize(R.styleable.MaxHeightNestedScrollView_maxHeight, defaultMaxHeight);
            styledAttrs.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
