package io.bega.servicebase.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import dagger.Module;
import dagger.Provides;
import io.bega.servicebase.activity.ActivityHelper;
import io.realm.Realm;


/**
 * Module providing Android framework dependencies
 */
@Module(
        library = true,
        complete = false
)
public class AndroidModule {

    @Provides
    SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
    }

    @Provides
    LayoutInflater provideLayoutInflater(Application application) {
        return (LayoutInflater) application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Provides
    InputMethodManager provideInputMethodManager(Application application) {
        return (InputMethodManager) application.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Provides
    ActivityManager provideActivityManager(Application application) {
        return (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Provides
    Context provideActivityContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    /*@Provides
    RealmService provideRealmService(final Realm realm) {
        return new RealmService(realm);
    } */


    @Provides
    DrawerLayout providesDrawerLayout(Application application) {
        return null;
    }

    @Provides
    AppCompatActivity providesActivityCompat() {
        AppCompatActivity activityCompat = null;
        try {
            Activity activity = ActivityHelper.getActivity();
            if (activity instanceof AppCompatActivity) {
                activityCompat = (AppCompatActivity) activity;
            }
        } catch (Exception ex) {
            return null;
        }

        return activityCompat;
    }
}
