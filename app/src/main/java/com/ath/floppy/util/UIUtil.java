package com.ath.floppy.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Build;
import android.view.View;

public class UIUtil {

    public static void showProgress(Activity activity, final View progressView,
                                    final View contentView, final boolean show) {
        if (contentView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime =
                    activity.getResources().getInteger(android.R.integer.config_shortAnimTime);

            contentView.setVisibility(show ? View.GONE : View.VISIBLE);
            contentView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            contentView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
        } else {
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            contentView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
