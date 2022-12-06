package com.telederma.gov.co.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by Daniel Hernández on 18/07/2018.
 */

public class ExtendableGridView extends GridView {

    public ExtendableGridView(Context context) {
        super(context);
    }

    public ExtendableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendableGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExtendableGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int heightSpec;

        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT)
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        else
            heightSpec = heightMeasureSpec;

        super.onMeasure(widthMeasureSpec, heightSpec);
    }

}
