package io.bega.servicebase;

import javax.inject.Singleton;
import javax.validation.Validation;
import javax.validation.Validator;

import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import io.bega.servicebase.analytics.AnalyticsModule;
import io.bega.servicebase.android.AndroidModule;
import io.bega.servicebase.environment.Environment;
import io.bega.servicebase.environment.EnvironmentModule;
import io.bega.servicebase.repository.JsonSharedPreferencesRepository;
import io.bega.servicebase.service.ApiService;
import io.bega.servicebase.util.gson.GsonModule;

import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(
  includes = {
    AndroidModule.class,
    AnalyticsModule.class,
    EnvironmentModule.class,
    GsonModule.class
  },
  library = true
)
public class ApplicationModule {
  private Application application;

  ApplicationModule(Application application) {
    this.application = application;
  }

  @Provides
  @Singleton
  android.app.Application provideApplication() {
    return application;
  }

  @Provides
  @Singleton
  Bus providesBus() {
    return new Bus(); // my name is Otto and I love to get blotto
  }

  @Provides
  @Singleton
  JsonSharedPreferencesRepository provideSharedPreferenceRepository(GsonBuilder gsonBuilder, SharedPreferences sharedPreferences) {
    return new JsonSharedPreferencesRepository(gsonBuilder, sharedPreferences);
  }

  @Provides
  @Singleton
  ApiService provideApiService(Environment environment) {
    if (environment.getName().equals("Local")) {
      return null; // new StubApiService();
    } else {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
         interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
         String url = environment.getApiHost() + environment.getApiBasePath();
         Retrofit restAdapter = new Retrofit.Builder()
        .client(client)
        .baseUrl(url)
              .addConverterFactory(GsonConverterFactory.create())
        .build();
      return restAdapter.create(ApiService.class);
    }
  }

  @Provides
  @Singleton
  Validator provideValidator() {
    return Validation.buildDefaultValidatorFactory().getValidator();
  }
}
