package io.bega.servicebase.activity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.bega.servicebase.actionbar.ActionBarOwner;


@Module(complete = false, library = true)
public class WindowModule {
    @Provides
    @Singleton
    WindowOwner provideActionBarOwner() {
        return new WindowOwner();
    }
}