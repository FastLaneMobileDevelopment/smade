package io.bega.servicebase.android;

/**
 * Created by user on 4/24/16.
 */


import android.util.Log;

import javax.inject.Singleton;

        import dagger.Module;
        import dagger.Provides;
        import io.bega.servicebase.BuildConfig;
        import io.bega.servicebase.screen.appointment.fragments.AppointmentActionsFragment;
        import io.bega.servicebase.screen.appointment.fragments.AppointmentDataFragment;
        import io.bega.servicebase.screen.appointment.fragments.AppointmentPhotoFragment;
        import io.bega.servicebase.screen.main.MainActivity;
        import timber.log.Timber;

@Module(injects = {
        // Activities
        // Fragments
        AppointmentActionsFragment.class,
        AppointmentPhotoFragment.class,
        AppointmentDataFragment.class,
}, library = true)
public class LoggingModule {

    @Provides @Singleton Timber provideTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

        } else {
            Timber.plant(new CrashReportingTree());
        }

       return null;

    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }


            // TODO integrate with reporting library
            //FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    // FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    //  FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }
}