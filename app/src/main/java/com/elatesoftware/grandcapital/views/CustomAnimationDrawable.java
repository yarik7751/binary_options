package com.elatesoftware.grandcapital.views;

import android.graphics.drawable.AnimationDrawable;


public class CustomAnimationDrawable extends AnimationDrawable {

    public interface AnimationEndListner {
        void animationEnd();
    }

    private AnimationEndListner animationEndListner;
    private boolean finished = false;

    public AnimationEndListner getAnimationEndListner() {
        return animationEndListner;
    }

    public void setAnimationEndListner(AnimationEndListner animationEndListner) {
        this.animationEndListner = animationEndListner;
    }

    @Override
    public boolean selectDrawable(int index) {
        boolean result =  super.selectDrawable(index);

        if ((index != 0) && (index == getNumberOfFrames() - 1)) {
            if (!finished) {
                finished = true;
                if (animationEndListner != null) animationEndListner.animationEnd();
            }
        }
        return result;
    }
}
