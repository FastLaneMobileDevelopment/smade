package io.bega.servicebase.service.api;


import android.content.Context;
import android.text.format.Time;

import com.google.gson.Gson;

import io.bega.servicebase.environment.Environment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

import io.bega.servicebase.model.UserToken;
import okhttp3.Interceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Controllador retorfit que utilizando la interfaz KdUINOAPIServer
 * implementa dos endpoints para la aplicación android, y uno con authenticación y otro para la parte que no usa
 * oauth. Crear un adaptador retrofit para el servicio de ip para determinar los datos del usuari por ip.
 */
public class ServiceBaseAPIService {

    // private RestAdapter.LogLevel LOG_LEVEL = RestAdapter.LogLevel.FULL;

    private Context ctx;

    private Retrofit retrofit;

    private Retrofit retrofitAuth;

    private Retrofit retrofitCountryIPService;

    private OkHttpClient okHttpClient;

    private UserToken auth_token;

    private long lastDateToken;

    public void refreshAuthToken() {

    }

    @Inject
    Environment environment;

    public void setApiToken(UserToken token) {
        Time time = new Time();
        time.setToNow();
        lastDateToken = time.toMillis(false);
        this.auth_token = token;

        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request request = chain.request();
                                if (auth_token != null) {
                                    //request.
                                    //     ddHeader("Authorization", auth_token.).build();
                                }
                                return chain.proceed(request);
                            }
                        }).build();

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl()
                .addConverterFactory(GsonConverterFactory.create())
                .build(); */

      /*  retrofitAuth = new Retrofit.Builder()
                //.setLogLevel(LOG_LEVEL)
                .setLog(new AndroidLog(BegaApplication.TAG))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {

                        if (auth_token != null) {
                            request.addHeader(
                            "Authorization:", auth_token.Token);

                        }
                    }
                })

                // .setErrorHandler(new KdUINOErrorHandler())
                .setEndpoint(KdUINOApiServer.API_URL)
                .build(); */

    }

    public Boolean isAthenticated() {
        if (retrofitAuth == null) {
            return false;
        }


        return true;
    }

    @Inject
    public ServiceBaseAPIService(Context ctx) {
        this.ctx = ctx;
        okHttpClient = new OkHttpClient();

        /*retrofit = new RestAdapter.Builder()
                .setLogLevel(LOG_LEVEL)
                .setLog(new AndroidLog(BegaApplication.TAG))
                .setClient(new OkClient(okHttpClient))
                .setErrorHandler(new APIErrorHandler())
                .setEndpoint(ServiceBaseApiServer.API_URL)
                .build();

        /*retrofitCountryIPService = new RestAdapter.Builder()
                .setLogLevel(LOG_LEVEL)
                .setLog(new AndroidLog(BegaApplication.TAG))
                .setClient(new OkClient(okHttpClient))
                .setErrorHandler(new KdUINOErrorHandler())
                .setEndpoint(KdUINOApiServer.API_GET_COUNTRY)
                .build(); */
    }
}





//  /*  public void getAllUsers(Callback<String> userCallback)
//    {
//        final KdUINOApiServer kdUINOApiService = retrofit.create(KdUINOApiServer.class);
//
//        // kdUINOApiService.getAllUser(userCallback);
//    }
//
//    public void getLastID(Callback<String> callback)
//    {
//        final KdUINOApiServer kdUINOApiService = retrofit.create(KdUINOApiServer.class);
//
//        // kdUINOApiService.getLastId(callback);
//    }
//
//
//    public String getLastID()
//    {
//        final KdUINOApiServer kdUINOApiService = retrofit.create(KdUINOApiServer.class);
//        return "";
//        //return kdUINOApiService.getLastIdSynchro();
//    }
//
//
//
//
//    public void getAllUser(Callback<String> callback)
//    {
//        final KdUINOApiServer kdUINOApiService = retrofitAuth.create(KdUINOApiServer.class);
//        //kdUINOApiService.getAllUser(callback);
//    }
//
//    public String getAllUser()
//    {
//        final KdUINOApiServer kdUINOApiService = retrofitAuth.create(KdUINOApiServer.class);
//        return "";
//        //return kdUINOApiService.getAllUserSynchro();
//    }
//
//
//
//
//    public void login(UserLogin login, Callback<UserToken> callback)
//    {
//        final ServiceBaseApiServer serviceApiService = retrofit.create(ServiceBaseApiServer.class);
//        serviceApiService.login(login,  callback);
//    }
//
//
//    public void saveUser(UserDefinition user, Callback<String> callback) {
//
//        final KdUINOApiServer kdUINOApiService = retrofit.create(KdUINOApiServer.class);
//        // kdUINOApiService.createUser(user, callback);
//    }
//
//    public String saveUser(UserDefinition user) {
//
//        final KdUINOApiServer kdUINOApiService = retrofit.create(KdUINOApiServer.class);
//        return "";
//        //return kdUINOApiService.createUserSynchro(user);
//    } /*
////}
