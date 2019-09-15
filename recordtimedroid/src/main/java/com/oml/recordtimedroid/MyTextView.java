package com.oml.recordtimedroid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

// Issue:
//         User needs to click twice to select line.
//
// Solution:
//            https://android-er.blogspot.com/2014/09/warning-custom-view-overrides.html

public class MyTextView extends TextView {
    private long lDiferencia;
    boolean touchOn;
    boolean mDownTouch = false;

    public MyTextView(Context context)
    {
        super(context);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public long getlDiferencia() {
        return lDiferencia;
    }

    public void setlDiferencia(long lDiferencia) {
        this.lDiferencia = lDiferencia;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        // Listening for the down and up touch events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                touchOn = !touchOn;
                invalidate();

                mDownTouch = true;
                return true;

            case MotionEvent.ACTION_UP:
                if (mDownTouch) {
                    mDownTouch = false;
                    performClick(); // Call this method to handle the response, and
                    // thereby enable accessibility services to
                    // perform this action for a user who cannot
                    // click the touchscreen.
                    return true;
                }
        }

        return false; // Return false for other touch events
    }

    @Override
    public boolean performClick() {
        // Calls the super implementation, which generates an AccessibilityEvent
        // and calls the onClick() listener on the view, if any
        super.performClick();

        // Handle the action for the custom click here

        return true;
    }

    private void init() {
        touchOn = false;
    }

}
