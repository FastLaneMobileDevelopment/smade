package io.bega.servicebase.screen.record;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;
import com.mikepenz.fastadapter_extensions.swipe.SimpleSwipeCallback;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import butterknife.BindString;

import butterknife.ButterKnife;
import flow.Flow;
import io.bega.servicebase.R;
import io.bega.servicebase.actionbar.ActionBarConfig;
import io.bega.servicebase.actionbar.ActionBarOwner;
import io.bega.servicebase.model.appointment.AppointmentData;
import io.bega.servicebase.model.items.SwipeableItem;
import io.bega.servicebase.screen.appointment.AppointmentScreen;
import io.bega.servicebase.screen.calendar.CalendarView;
import io.bega.servicebase.util.mortar.BaseViewPresenter;

class RecordPresenter extends BaseViewPresenter<RecordView> {

	private ActionBarOwner actionBarOwner;

    @Inject
    Flow flow;

    @Inject
    LayoutInflater inflater;

    @Inject
    AppCompatActivity appCompatActivity;

	@Inject
	RecordPresenter(ActionBarOwner actionBarOwner) {
		this.actionBarOwner = actionBarOwner;
	}

	@BindString(R.string.view_record_title) String recordTitle;

	@Override
	protected void onLoad(Bundle savedInstanceState) {
		super.onLoad(savedInstanceState);

		RecordView view = getView();
		if (view != null) {
			ButterKnife.bind(view);
			configureActionBar();
		}

	}

	private void configureActionBar() {
		ActionBarConfig config = new ActionBarConfig.Builder()
				.setMenu(R.menu.menu_record)
				.title(recordTitle)
				.build();
		actionBarOwner.setConfig(config);
	}

    public void moveToRecord(AppointmentData data)
    {
        flow.goTo(new AppointmentScreen(data));
    }

    public void openCalendarDialog()
    {
        View view = inflater.inflate(R.layout.view_dialog_calendar, null);
        CalendarPickerView calendar = ButterKnife.findById(view, R.id.view_dialog_calendar_view);

        int colorBlack = appCompatActivity.getResources().getColor(R.color.md_black_1000);
        Calendar previousTwoMonth = Calendar.getInstance();
        Calendar nextWeek = Calendar.getInstance();
        previousTwoMonth.add(Calendar.MONTH, -2);


        /*calendar.init(today, nextYear.getTime())
                .withSelectedDate(today); */
         calendar.init(previousTwoMonth.getTime(), nextWeek.getTime())
            .inMode(CalendarPickerView.SelectionMode.RANGE);



        MaterialDialog dialog = new MaterialDialog.Builder(appCompatActivity)
                //.setTitle(appCompatActivity.getString(R.string.calendar_appointment_visit))
               // .setHeaderDrawable(R.drawable.header)
                .customView(view, false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .cancelable(true)
                .build();

        dialog.show();
    }

}
