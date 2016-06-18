package io.bega.servicebase.model.service;

import io.bega.servicebase.model.appointment.AppointmentData;

/**
 * Created by user on 6/2/16.
 */


public class AppointmentManager {
    private static AppointmentManager mInstance = null;

    private AppointmentData data;

    private AppointmentManager(){
    }

    public static AppointmentManager getInstance(){
        if(mInstance == null)
        {
            mInstance = new AppointmentManager();
        }
        return mInstance;
    }

    public AppointmentData getData(){
        return this.data;
    }

    public void setData(AppointmentData value){
        this.data = value;
    }
}
