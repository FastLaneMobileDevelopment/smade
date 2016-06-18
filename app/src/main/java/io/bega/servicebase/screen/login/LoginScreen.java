package io.bega.servicebase.screen.login;

import io.bega.servicebase.R;

import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import io.bega.servicebase.screen.main.MainScreen;
import io.bega.servicebase.screen.splash.SplashScreen;

@Layout(R.layout.view_login)
public class LoginScreen implements Blueprint, HasParent<SplashScreen> {
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
			injects = LoginView.class,
			addsTo = MainScreen.Module.class
	)
	class Module {}
}
