package io.bega.servicebase.screen.appointment;

import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import io.bega.servicebase.R;
import io.bega.servicebase.model.appointment.AppointmentData;
import io.bega.servicebase.model.service.AppointmentManager;
import io.bega.servicebase.screen.calendar.CalendarScreen;
import io.bega.servicebase.screen.main.MainScreen;
import io.realm.annotations.Ignore;
import mortar.Blueprint;

@Layout(R.layout.view_appointment)
public class AppointmentScreen implements Blueprint, HasParent<CalendarScreen> {

    private AppointmentData data;

    public AppointmentScreen(AppointmentData data)
    {
        this.data = data;
		AppointmentManager.getInstance().setData(data);
	}

	@Override
	public String getMortarScopeName() {
		return getClass().getName();
	}



	@Override
	public Object getDaggerModule() {
		return new Module();
	}

	@Override
	public CalendarScreen getParent() {
		return new CalendarScreen();
	}

	@dagger.Module(
			injects = AppointmentView.class,
			addsTo = MainScreen.Module.class

	)
	class Module {
    }
}
