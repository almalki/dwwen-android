package com.dwwen.common;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Typeface;

import com.dwwen.BuildConfig;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import dagger.ObjectGraph;

public class ShipFasterApplication extends Application {

    public static ShipFasterApplication from(Activity activity) {
        return (ShipFasterApplication) activity.getApplication();
    }

    public static Typeface DroidKufi;
    public static Typeface Mitra;
    public static Typeface HelveticaNeueLT;

    private ObjectGraph objectGraph;
    private Activity resumedActivity;

    @Override public void onCreate() {
        super.onCreate();

        DroidKufi = Typeface.createFromAsset(this.getAssets(),"fonts/DroidKufi-Regular.ttf");
        Mitra = Typeface.createFromAsset(this.getAssets(),"fonts/mitra-lt-light.ttf");
        HelveticaNeueLT = Typeface.createFromAsset(this.getAssets(),"fonts/HelveticaNeueLT-W20-45-Light.ttf");

        Picasso.with(this).setDebugging(BuildConfig.DEBUG);

        if (BuildConfig.DEBUG) {
            Object prodModule = new ProdAppModule(this);
            Object devModule = new DevAppModule();
            objectGraph = ObjectGraph.create(prodModule, devModule);
        } else {
            Object prodModule = new ProdAppModule(this);
            objectGraph = ObjectGraph.create(prodModule);
        }

//        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/DroidKufi-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/DroidKufi-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/DroidKufi-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "DEFAULT_BOLD", "fonts/DroidKufi-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "SERIF", "fonts/DroidKufi-Regular.ttf");

        String languageToLoad  = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,
                this.getResources().getDisplayMetrics());

    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public void setResumedActivity(Activity activity) {
        this.resumedActivity = activity;
    }

    /**
     * May return null
     */
    public Activity getResumedActivity() {
        return resumedActivity;
    }
}
