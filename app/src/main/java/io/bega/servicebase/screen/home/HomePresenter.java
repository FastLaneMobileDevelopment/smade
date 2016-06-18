package io.bega.servicebase.screen.home;

import android.os.Bundle;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import flow.Flow;
import flow.Parcer;
import io.bega.servicebase.actionbar.ActionBarConfig;
import io.bega.servicebase.actionbar.ActionBarOwner;
import io.bega.servicebase.repository.JsonSharedPreferencesRepository;
import io.bega.servicebase.screen.calendar.CalendarScreen;
import io.bega.servicebase.screen.login.LoginView;
import io.bega.servicebase.screen.main.MainView;
import io.bega.servicebase.util.flow.FlowOwner;
import io.bega.servicebase.util.mortar.BaseViewPresenter;
import mortar.Blueprint;

class HomePresenter extends BaseViewPresenter<HomeView> {

	private ActionBarOwner actionBarOwner;


	private JsonSharedPreferencesRepository sharedPreferencesRepository;

	private Bus bus;

	private Flow flow;


	@Inject
	HomePresenter(Flow flow, JsonSharedPreferencesRepository sharedPreferences, ActionBarOwner actionBarOwner, Bus bus)
	{
		this.flow = flow;
		this.sharedPreferencesRepository = sharedPreferences;
		this.bus = bus;
		this.actionBarOwner = actionBarOwner;
	}

	@Override
	public void onLoad(Bundle savedInstanceState) {
		super.onLoad(savedInstanceState);

		HomeView view = getView();
		if (view != null) {
			configureActionBar();
		}
	}


	private void configureActionBar() {
		ActionBarConfig config = new ActionBarConfig.Builder()
				.title("Home View")
				.enableHomeAsUp(true)
				.enableDrawerLayout(true)
				.visible(true)
				.build();
		actionBarOwner.setConfig(config);
	}

	public void gotoCalendar()
	{
		flow.goTo(new CalendarScreen());
	}
}
