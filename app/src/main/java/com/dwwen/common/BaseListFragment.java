package com.dwwen.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.dwwen.oauth2.TokenManager;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Created by abdulaziz on 12/10/2014.
 */
public class BaseListFragment extends ListFragment{

    private View mainView;
    private View progressView;

    @Inject
    BusRegistrar busRegistrar;

    @Inject
    Bus bus;

    @Inject
    TokenManager tokenManager;

    ShipFasterApplication application;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = ShipFasterApplication.from(this.getActivity());
        application.inject(this);
        bus.register(this);
        tokenManager.checkToken();
    }

    @Override public void onResume() {
        super.onResume();
        application.setResumedActivity(this.getActivity());
        busRegistrar.registerSubscribers();
    }

    @Override public void onPause() {
        busRegistrar.unregisterSubscribers();
        application.setResumedActivity(null);
        super.onPause();
    }

    public <T extends View> T findById(int resId) {
        return (T) this.getActivity().findViewById(resId);
    }

    /**
     * Shows the progress UI and hides the main view.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
            mainView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public View getMainView() {
        return mainView;
    }

    public void setMainView(View mainView) {
        this.mainView = mainView;
    }

    public View getProgressView() {
        return progressView;
    }

    public void setProgressView(View progressView) {
        this.progressView = progressView;
    }


}
