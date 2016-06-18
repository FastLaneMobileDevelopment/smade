package io.bega.servicebase.screen.record;

import flow.HasParent;
import flow.Layout;
import io.bega.servicebase.R;
import io.bega.servicebase.screen.home.HomeScreen;
import io.bega.servicebase.screen.main.MainScreen;
import io.bega.servicebase.screen.splash.SplashScreen;
import mortar.Blueprint;

@Layout(R.layout.view_record)
public class RecordScreen implements Blueprint, HasParent<HomeScreen> {
	@Override
	public String getMortarScopeName() {
		return getClass().getName();
	}

	@Override
	public Object getDaggerModule() {
		return new Module();
	}

	@Override
	public HomeScreen getParent() {
		return new HomeScreen();
	}

	@dagger.Module(
			injects = RecordView.class,
			addsTo = MainScreen.Module.class
	)
	class Module {}
}
