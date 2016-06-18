package io.bega.servicebase.service.api;

import io.bega.servicebase.model.UserToken;
import io.bega.servicebase.model.UserWithPassword;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Interfaz para retrofit para definir los endpoints de la api rest.
 */
public interface ServiceBaseApiServer {

    public static final String API_GET_COUNTRY = "http://ip-api.com";



    @POST("/Operators/login")
    Call<UserToken> loadRepo(@Body UserWithPassword userWithPassword);









}
