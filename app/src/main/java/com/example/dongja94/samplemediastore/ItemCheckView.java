package com.example.dongja94.samplemediastore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by dongja94 on 2016-02-22.
 */
public class ItemCheckView extends FrameLayout implements Checkable {
    public ItemCheckView(Context context) {
        super(context);
        init();
    }

    public ItemCheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView checkView;
    private void init() {
        inflate(getContext(), R.layout.view_item, this);
        checkView = (ImageView)findViewById(R.id.image_check);
    }

    private void drawCheck() {
        if (isChecked) {
            checkView.setVisibility(View.VISIBLE);
        } else {
            checkView.setVisibility(View.GONE);
        }
    }

    boolean isChecked;
    @Override
    public void setChecked(boolean checked) {
        if (isChecked != checked) {
            isChecked = checked;
            drawCheck();
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
