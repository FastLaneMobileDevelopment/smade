package io.bega.servicebase.model.calendar;

import com.alamkanak.weekview.WeekViewEvent;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Locale;

import io.bega.servicebase.model.appointment.AppointmentData;


/**
 * Created by user on 4/12/16.
 */
public class AppointmentEvent extends WeekViewEvent {

    AppointmentData data;

    public AppointmentEvent(AppointmentData appointmentData,  Calendar startTime, Calendar endTime)
    {
        super(appointmentData.hashCode(), appointmentData.Title, appointmentData.toString(), startTime, endTime);
        this.data = appointmentData;
    }

    public AppointmentData getData()
    {
        return this.data;
    }

}
