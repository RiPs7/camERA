package perimara.era.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by periklismaravelias on 09/09/16.
 */
public class Animations {

    public static void FadeOut(View v, long offset){
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f) ;
        v.startAnimation(fadeOut);
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);
        fadeOut.setStartOffset(offset);
    }

    public static void FadeIn(View v, long offset){
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        v.startAnimation(fadeIn);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);
        fadeIn.setStartOffset(offset);
    }

}
