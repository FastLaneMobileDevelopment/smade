package io.bega.servicebase.service;


import java.util.List;

import io.bega.servicebase.model.UserToken;
import io.bega.servicebase.model.UserWithPassword;
import io.bega.servicebase.model.appointment.AppointmentData;
import io.bega.servicebase.model.service.Operator;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * A sample interface to an API that might exist on your server.
 */
public interface ApiService {

  /**
   * Registers a new user account.
   */
  @POST("operators")
  Call<Operator> register(@Body Operator operator);

  /**
   * Perform a operators login.
   */
  @POST("operators/login")
  Call<UserToken> login(@Body UserWithPassword authentication);

  /**
   * Perform a user logout.
   */
  @POST("operators/logout")
  Observable<Void> logout(@Header("Authorization") UserToken token);

  @GET("operators/{operatorId}/appointments")
  Call<List<AppointmentData>> recieveAppointments(@Header("Authorization") String token, @Path("operatorId") String operatorID);



}
