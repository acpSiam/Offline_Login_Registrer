package bmarpc.acpsiam.offlineloginregister;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class MyAnim {

    public void popOpenBothSide(View view, long duration) {
        if (view.getVisibility() == View.GONE){
            view.setVisibility(View.VISIBLE);
            view.setAlpha(0f);
            view.setScaleX(.5f);
            view.animate()
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .alpha(1f)
                    .scaleX(1f)
                    .setDuration(duration)
                    .start();
        }
    }

    public void popCloseBothSide(View view, long duration) {
        if (view.getVisibility() == View.VISIBLE){
            view.animate()
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .alpha(0f)
                    .scaleX(0f)
                    .setDuration(duration)
                    .start();
            view.setVisibility(View.GONE);
        }
    }

}
