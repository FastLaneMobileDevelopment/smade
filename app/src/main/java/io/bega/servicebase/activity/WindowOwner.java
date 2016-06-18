package io.bega.servicebase.activity;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import io.bega.servicebase.actionbar.ActionBarConfig;
import mortar.Mortar;
import mortar.MortarScope;
import mortar.Presenter;

/**
 * Created by user on 4/5/16.
 */
public class WindowOwner extends Presenter<WindowOwner.Activity> {

    public AppCompatActivity getActivityCompat() {
        return activityCompat;
    }

    private AppCompatActivity activityCompat;

    private WindowConfig config;

    @Inject
    public WindowOwner() {
        this.config = new WindowConfig.Builder().build();
    }

    @Override
    protected MortarScope extractScope(WindowOwner.Activity view) {
        if (view instanceof AppCompatActivity)
        {
            activityCompat = (AppCompatActivity)view;
        }

        return Mortar.getScope(view.getMortarContext());
    }

    public interface Activity {
        void addFlagsToWindow(int flags);

        Context getMortarContext();
    }

    public static class Config {
        private final int flags;

        Config(int flags) {
            this.flags = flags;
        }

        public int getFlags() {
            return this.flags;
        }
    }



    private void update() {
        WindowOwner.Activity activity = getView();
        if (activity != null) {
          //  activity.addFlagsToWindow(config.get);
        }
    }

    @dagger.Module(injects = {}, library = true)
    public static class WindowOwnerModule {
        @Provides
        @Singleton
        WindowOwner provideWindowOwnerPersenter() {
            return new WindowOwner();
        }
    }
}
