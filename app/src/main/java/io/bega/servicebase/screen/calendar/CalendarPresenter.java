package io.bega.servicebase.screen.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import flow.Flow;
import io.bega.servicebase.R;
import io.bega.servicebase.SharedPreferencesKeys;
import io.bega.servicebase.actionbar.ActionBarConfig;
import io.bega.servicebase.actionbar.ActionBarOwner;
import io.bega.servicebase.activity.ActivityHelper;
import io.bega.servicebase.model.UserToken;
import io.bega.servicebase.model.appointment.AppointmentData;
import io.bega.servicebase.model.calendar.AppointmentEvent;
import io.bega.servicebase.model.event.MenuMessageBusEvent;
import io.bega.servicebase.repository.JsonSharedPreferencesRepository;
import io.bega.servicebase.screen.appointment.AppointmentScreen;
import io.bega.servicebase.service.ApiService;
import io.bega.servicebase.util.TextHelper;
import io.bega.servicebase.util.logging.Logger;
import io.bega.servicebase.util.mortar.BaseViewPresenter;
import mortar.MortarScope;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class CalendarPresenter extends BaseViewPresenter<CalendarView> implements
        WeekView.EventClickListener,
        MonthLoader.MonthChangeListener,
        WeekView.EventLongPressListener,
        WeekView.EmptyViewClickListener,
        WeekView.EmptyViewLongPressListener,
        WeekViewLoader {

    private static final Logger LOG = Logger.getLogger(CalendarPresenter.class);

    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

    private ActionBarOwner actionBarOwner;

    Context applicationContext;

    AppCompatActivity appCompatActivity;

    JsonSharedPreferencesRepository repository;

    private ApiService apiService;

    LayoutInflater inflater;

    Flow flow;

    Bus bus;

    @Inject
    CalendarPresenter(Context applicationContext, ApiService service, Bus bus,
                      JsonSharedPreferencesRepository repository,
                      LayoutInflater inflater, Flow flow, ActionBarOwner actionBarOwner, AppCompatActivity appCompatActivity) {
        this.applicationContext = applicationContext;
        this.apiService = service;
        this.bus = bus;
        this.repository = repository;
        this.actionBarOwner = actionBarOwner;
        this.inflater = inflater;
        this.appCompatActivity = appCompatActivity;
        this.flow = flow;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);

        CalendarView view = getView();
        if (view != null) {
            configureActionBar();
        }

        searchData();

    }

    @Override
    protected void onEnterScope(MortarScope scope) {
        super.onEnterScope(scope);
        bus.register(this);
    }

    @Override
    protected void onExitScope() {
        super.onExitScope();
        bus.unregister(this);
    }

    @Subscribe
    public void message(MenuMessageBusEvent event)
    {
        if (event.getMessage() == 0xFE)
        {
            this.searchData();
        } else if (event.getMessage() == 0xFD)
        {
            this.goToday();
        }
    }

    private void goToday()
    {
        final WeekView weekView = getView().mWeekView;
        DateTime dateTime = DateTime.now();
        weekView.goToToday();
        weekView.goToHour(dateTime.getHourOfDay());
    }

    private void searchData()
    {
        final WeekView weekView = getView().mWeekView;
        UserToken userToken = repository.getObject(SharedPreferencesKeys.USER_TOKEN, UserToken.class);
        Call<List<AppointmentData>> appointments = apiService.recieveAppointments(userToken.Token, userToken.UserId);
        appointments.enqueue(new Callback<List<AppointmentData>>() {
            @Override
            public void onResponse(Call<List<AppointmentData>> call, Response<List<AppointmentData>> response) {
                LOG.info("response");
                List<AppointmentData> listOfAppointments = response.body();
                if (listOfAppointments == null)
                {
                    try
                    {
                        Activity activity = ActivityHelper.getActivity();
                        new MaterialDialog.Builder(activity)
                                .title(R.string.appointment_list_error_title)
                                .content(R.string.appointment_list_error_description)
                                .positiveText(R.string.ok)
                                .show();
                    }
                    catch (Exception ex)
                    {
                        LOG.error("Error searching activity", ex);
                    }
                }
                else {
                    int i = 1;
                    for (AppointmentData data : listOfAppointments) {
                        LOG.debug("appointmentData " + data.AppointmentID);
                        try {
                            DateTime startDatetime = new DateTime(data.StartDate);
                            DateTime endDatetime = null;
                            Calendar startTime = startDatetime.toCalendar(Locale.getDefault());
                            Calendar endTime = null;

                            if (!TextHelper.isEmptyString(data.EndDate))
                            {
                                endDatetime = new DateTime(data.EndDate);
                            }
                            else
                            {
                                endDatetime = startDatetime.plusHours(1);
                            }

                            Calendar startCalendar = startDatetime.toCalendar(Locale.getDefault());
                            Calendar endCalendar = endDatetime.toCalendar(Locale.getDefault());
                            AppointmentEvent event = new AppointmentEvent(data, startCalendar, endCalendar);
                            event.setColor(applicationContext.getResources().getColor(R.color.event_color_01));
                            events.add(event);
                            i++;

                        }
                        catch (Exception ex)
                        {
                            LOG.error("Error converting appointment datetime", ex);
                        }



                    }


                    weekView.notifyDatasetChanged();
                    weekView.goToToday();
                    weekView.goToHour(8);
                }
            }

            @Override
            public void onFailure(Call<List<AppointmentData>> call, Throwable t) {
                LOG.error("failure");
                try
                {
                    Activity activity = ActivityHelper.getActivity();
                    new MaterialDialog.Builder(activity)
                            .title(R.string.appointment_list_error_title)
                            .content(R.string.appointment_list_error_description)
                            .positiveText(R.string.ok)
                            .show();
                }
                catch (Exception ex)
                {
                    LOG.error("Error searching activity", ex);
                }
            }
        });

    }

    private void configureActionBar() {
        ActionBarConfig config = new ActionBarConfig.Builder()
                .title("Calendar")
                .setMenu(R.menu.menu_calendar)
                .enableDrawerLayout(true)
                .enableHomeAsUp(true)
                .build();
        actionBarOwner.setConfig(config);
    }

    private String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }

    private List<? extends WeekViewEvent> getData(int newYear, int newMonth) {
        LOG.debug("Create weekevents: Year: " + String.valueOf(newYear) + " Month: " + String.valueOf(newMonth));


        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }
        return matchedEvents;



        /*Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event = new WeekViewEvent(1, "Sabadell (Valles Oriental)\n" + getEventTitle(startTime), startTime, endTime);
        event.setColor(applicationContext.getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 4);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(10, "Mataro (Maresme)\n" + getEventTitle(startTime), startTime, endTime);
        event.setColor(applicationContext.getResources().getColor(R.color.event_color_02));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 4);
        startTime.set(Calendar.MINUTE, 20);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 5);
        endTime.set(Calendar.MINUTE, 0);
        event = new WeekViewEvent(10, "Barcelona (Barcelona)\n" + getEventTitle(startTime), startTime, endTime);
        event.setColor(applicationContext.getResources().getColor(R.color.event_color_03));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 2);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(2, "Rubi (Valles Occidental)\n" + getEventTitle(startTime), startTime, endTime);
        event.setColor(applicationContext.getResources().getColor(R.color.event_color_02));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        startTime.add(Calendar.DATE, 1);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(3, "Sant Cugat del Valles (Valles Occidental)\n" + getEventTitle(startTime), startTime, endTime);
        event.setColor(applicationContext.getResources().getColor(R.color.event_color_03));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 15);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(4, "Barcelona (Barcelones)\n" + getEventTitle(startTime), startTime, endTime);
        event.setColor(applicationContext.getResources().getColor(R.color.event_color_04));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 1);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, "Palau-Solita y plegamans (Valles Occidental)\n" + getEventTitle(startTime), startTime, endTime);
        event.setColor(applicationContext.getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, startTime.getActualMaximum(Calendar.DAY_OF_MONTH));
        startTime.set(Calendar.HOUR_OF_DAY, 15);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 3);
        event = new WeekViewEvent(5, "Montcada y Reixac (Valles Occidental)\n" + getEventTitle(startTime), startTime, endTime);
        event.setColor(applicationContext.getResources().getColor(R.color.event_color_02));
        events.add(event);

        //AllDay event
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 23);
        event = new WeekViewEvent(7, "Barcelona (Barcelones)\n" + getEventTitle(startTime), startTime, endTime);
        event.setColor(applicationContext.getResources().getColor(R.color.event_color_04));
        events.add(event);


        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 8);
        startTime.set(Calendar.HOUR_OF_DAY, 2);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.DAY_OF_MONTH, 10);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        event = new WeekViewEvent(8, "Montornes (Valles Occidental)\n" + getEventTitle(startTime), startTime, endTime);
        event.setColor(applicationContext.getResources().getColor(R.color.event_color_03));
        events.add(event);

        // All day event until 00:00 next day
        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 10);
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.DAY_OF_MONTH, 11);
        event = new WeekViewEvent(8, "Piera (Osona)\n" + getEventTitle(startTime), startTime, endTime);
        event.setColor(applicationContext.getResources().getColor(R.color.event_color_01));
        events.add(event); */

    }

    @Override
    public void onEmptyViewClicked(Calendar time) {
        //Toast.makeText(applicationContext, "Empty Click: " + time.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        //Toast.makeText(applicationContext, "Empty Long Click: " + time.toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        LOG.info("Year: " + String.valueOf(newYear) + " Month: " + String.valueOf(newMonth));
        return this.getData(newYear, newMonth);
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {


        View view = inflater.inflate(R.layout.view_appointment_dialog, null);
        int colorBlack = appCompatActivity.getResources().getColor(R.color.md_black_1000);

        //TextView txtTitle = (TextView)view.findViewById(R.id.view_appointment_dialog_custom_text_view_title);
        TextView txtExp = (TextView)view.findViewById(R.id.view_appointment_dialog_custom_text_view_exp);
        TextView txtAssurance = (TextView)view.findViewById(R.id.view_appointment_dialog_custom_text_view_exp_assurance);
        TextView txtTelephone = (TextView)view.findViewById(R.id.view_appointment_dialog_custom_text_view_telephone);
        TextView txtRequest = (TextView)view.findViewById(R.id.view_appointment_dialog_custom_text_view_exp_request);
        TextView txtTown = (TextView)view.findViewById(R.id.view_appointment_dialog_custom_text_view_town);

      //  txtTitle.setText(appCompatActivity.getString(R.string.calendar_appointment_visit));
        AppointmentData data = null;
        if (event instanceof  AppointmentEvent) {
             data = ((AppointmentEvent)event).getData();
        }

        final AppointmentData currentData = data;
        final String telephone = data.Telephone;
        final String recordId = data.RecordID;
        txtExp.setText("Expediente: " + data.RecordID);
        txtAssurance.setText("Asegurado: " + data.Insured);
        txtTelephone.setText("Teléfonos: " +  data.Telephone);
        txtRequest.setText("Solicitud: " +  data.Description);
        txtTown.setText("Población: " + data.Town);
        txtTelephone.setTextColor(
                appCompatActivity.getResources().getColor(R.color.colorPrimary)
        );
        txtTelephone.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {
                try {
                    Uri call = Uri.parse("tel:" + telephone);
                    Intent surf = new Intent(Intent.ACTION_DIAL, call);
                    appCompatActivity.startActivity(surf);



                } catch (Exception except) {
                    LOG.error("error calling number: ", except);
                }
            }
        });

        txtTown.setTextColor(colorBlack);
        txtRequest.setTextColor(colorBlack);
        txtAssurance.setTextColor(colorBlack);
        txtExp.setTextColor(colorBlack);



        MaterialStyledDialog dialog = new MaterialStyledDialog(appCompatActivity)
                .setTitle(appCompatActivity.getString(R.string.calendar_appointment_visit))
                //.setHeaderDrawable(R.drawable.header)
                .setCustomView(view)
                .setPositive(appCompatActivity.getResources().getString(R.string.calendar_appointment_enter), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Log.d("MaterialStyledDialogs", "Do something!");
                        flow.goTo(new AppointmentScreen(currentData));
                    }
                })
                .setCancelable(true)
                .setNegative(appCompatActivity.getResources().getString(R.string.calendar_appointment_return), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Log.d("MaterialStyledDialogs", "Do something!");
                    }
                })
                .build();

        dialog.show();
       // Toast.makeText(applicationContext, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        //  Toast.makeText(applicationContext, "Long Clicked " + event.getName(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public double toWeekViewPeriodIndex(Calendar instance) {
        return 0;
    }

    @Override
    public List<? extends WeekViewEvent> onLoad(int periodIndex) {
        return null;
    }
}
