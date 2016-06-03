package dk.vixo.missing_people.control;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.view.View;

/**
 * Created by kasper on 6/2/16.
 */
public class Spinner {


    View originView;
    View spinnerView;

    //Takes 3 params. Boolean, View, View
    //true if the spinner needs to show and false if it does not.
    //First view is the view it should display when the task is done.
    //Second view is the spinner view.
    public void showProgress(final boolean show, View origin, View spin) {
        originView = origin;
        spinnerView = spin;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = 200;

            originView.setVisibility(show ? View.GONE : View.VISIBLE);
            originView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    originView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            spinnerView.setVisibility(show ? View.VISIBLE : View.GONE);
            spinnerView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    spinnerView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            spinnerView.setVisibility(show ? View.VISIBLE : View.GONE);
            originView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
