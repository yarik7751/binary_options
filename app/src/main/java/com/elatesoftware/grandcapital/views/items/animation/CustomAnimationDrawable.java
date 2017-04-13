package com.elatesoftware.grandcapital.views.items.animation;

import android.graphics.drawable.AnimationDrawable;


class CustomAnimationDrawable extends AnimationDrawable {

    interface AnimationEndListner {
        void animationEnd();
    }

    private AnimationEndListner animationEndListner;
    private boolean finished = false;

    void setAnimationEndListner(AnimationEndListner animationEndListner) {
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
