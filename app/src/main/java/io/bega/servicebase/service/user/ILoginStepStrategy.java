package io.bega.servicebase.service.user;

import android.content.Context;

import com.squareup.otto.Bus;

import io.bega.servicebase.service.api.ServiceBaseAPIService;


/**
 * Created by user on 3/31/16.
 */
public interface ILoginStepStrategy {

    void init(Context ctx, Bus bus, ServiceBaseAPIService apiService);
   // void execute(UserLogin data);
    void result(LoginResult data);
    void release();
}
