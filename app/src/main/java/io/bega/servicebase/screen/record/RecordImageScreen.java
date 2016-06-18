package io.bega.servicebase.screen.record;

import flow.HasParent;
import flow.Layout;
import io.bega.servicebase.R;
import io.bega.servicebase.screen.main.MainScreen;
import io.bega.servicebase.screen.splash.SplashScreen;
import mortar.Blueprint;

@Layout(R.layout.view_login)
public class RecordImageScreen implements Blueprint, HasParent<SplashScreen> {
	@Override
	public String getMortarScopeName() {
		return getClass().getName();
	}

	@Override
	public Object getDaggerModule() {
		return new Module();
	}

	@Override
	public SplashScreen getParent() {
		return new SplashScreen();
	}

	@dagger.Module(
			injects = RecordImageView.class,
			addsTo = MainScreen.Module.class
	)
	class Module {}
}
