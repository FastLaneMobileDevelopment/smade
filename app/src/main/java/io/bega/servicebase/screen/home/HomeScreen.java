package io.bega.servicebase.screen.home;

import flow.HasParent;
import flow.Layout;
import io.bega.servicebase.R;
import io.bega.servicebase.screen.main.MainScreen;
import io.bega.servicebase.screen.splash.SplashScreen;
import mortar.Blueprint;

/**
 * This screen might be where you send user's once they're logged in/registered
 * as a starting point for your app.
 *
 * The implementation is incomplete and left to your imagination.
 */
@Layout(R.layout.view_home)
public class HomeScreen implements Blueprint, HasParent<SplashScreen> {
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
			injects = HomeView.class,
			addsTo = MainScreen.Module.class
	)
 	class Module {}
}
