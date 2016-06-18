package io.bega.servicebase.screen.appointment;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.bega.servicebase.R;
import io.bega.servicebase.actionbar.ActionBarConfig;
import io.bega.servicebase.actionbar.ActionBarOwner;
import io.bega.servicebase.model.appointment.AppointmentData;
import io.bega.servicebase.model.appointment.OrderTask;
import io.bega.servicebase.util.logging.Logger;
import io.bega.servicebase.util.mortar.BaseViewPresenter;
import io.realm.Realm;
import io.realm.RealmResults;

class AppointmentPresenter extends BaseViewPresenter<AppointmentView>   {

	private static final Logger LOG = Logger.getLogger(AppointmentPresenter.class);

	private ActionBarOwner actionBarOwner;

	Context applicationContext;

    Realm realm;

	@Inject
	AppointmentPresenter(Context applicationContext, Realm realm, ActionBarOwner actionBarOwner) {
		this.applicationContext = applicationContext;
        this.realm = realm;
		this.actionBarOwner = actionBarOwner;
	}

	@Override
	protected void onLoad(Bundle savedInstanceState) {
		super.onLoad(savedInstanceState);

		AppointmentView view = getView();
		if (view != null) {
			configureActionBar();
		}

		// this.getAppointmentData()
	}

	private void configureActionBar() {
		ActionBarConfig config = new ActionBarConfig.Builder()
				.title("Visita")
				.setMenu(R.menu.menu_appointment)
				.enableDrawerLayout(true)
				.enableHomeAsUp(true)
				.build();
		actionBarOwner.setConfig(config);
	}

    public OrderTask getAppointmentData(AppointmentData data) {

        return new OrderTask(data, "","");

		/*RealmResults<OrderTask> results1 =
				realm.where(OrderTask.class).findAll();
		if (results1.size() == 0) {

			realm.beginTransaction();
			OrderTask orderTaskRealm = realm.copyToRealm(orderTask);
			realm.commitTransaction();
			return orderTaskRealm;
		} else {
			return results1.first();
		} */
	}
}
