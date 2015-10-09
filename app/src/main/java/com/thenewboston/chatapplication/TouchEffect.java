package com.thenewboston.chatapplication;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rahul on 08/10/15.
 */
public class TouchEffect implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if(view.getId()==R.id.signupbutton||view.getId()==R.id.loginbutton)
        {
            view.performClick();
        }
        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
        {
            Drawable d = view.getBackground();
            d.mutate();
            d.setAlpha(150);
            view.setBackgroundDrawable(d);
        }
        else if(motionEvent.getAction()==MotionEvent.ACTION_UP || motionEvent.getAction()==MotionEvent.ACTION_CANCEL)
        {
            Drawable d = view.getBackground();
            d.setAlpha(255);
            view.setBackgroundDrawable(d);
        }
        return false;
    }
}
