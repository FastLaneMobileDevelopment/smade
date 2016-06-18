package io.bega.servicebase.service.user;

import android.content.Context;

import com.squareup.otto.Bus;


import io.bega.servicebase.model.UserToken;
import io.bega.servicebase.service.api.ServiceBaseAPIService;
//import retrofit.Callback;
//import retrofit.RetrofitError;
//import retrofit.client.Response;

/**
 * Created by user on 3/31/16.
 */
public class LoginStepImpl  {

    LoginState state;

    ServiceBaseAPIService apiService;

    ILoginStepStrategy strategy;

    Context ctx;

    UserToken currentUserToken = null;

    Bus bus;


    public void init(Context ctx, Bus bus, ServiceBaseAPIService apiService) {
        this.bus = bus;
        this.ctx = ctx;
        this.apiService = apiService;
        this.state = LoginState.Login;

        //strategy = this;

    }

   /* @Override
    public void execute(UserLogin data) {
        apiService.login(data, new Callback<UserToken>() {
            @Override
            public void success(UserToken userToken, Response response) {
                currentUserToken = userToken;
                apiService.setApiToken(userToken);
                strategy.result(LoginResult.LoginOK);
                callback.result(LoginResult.LoginOK, response.getReason(), null, userToken);



            }

            @Override
            public void failure(RetrofitError error) {
               strategy.result(LoginResult.PasswordError);


            }

        });
    }


    @Override
    public void result(LoginResult data) {

        // bus.post(new LoginEvent(data, currentUserToken));
    }

    @Override
    public void release() {
        this.bus = null;
        this.strategy = null;
        this.apiService = null;
        this.ctx = null;
        this.currentUserToken = null;
    } */
}
