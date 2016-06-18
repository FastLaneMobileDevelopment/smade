package io.bega.servicebase.screen.splash;

import javax.inject.Inject;

import io.bega.servicebase.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import butterknife.Bind;
import butterknife.OnClick;
import mortar.Presenter;
import io.bega.servicebase.analytics.EventTracker;
import io.bega.servicebase.util.mortar.BaseView;

public class SplashView extends BaseView {

	@Inject SplashPresenter presenter;
	@Inject EventTracker eventTracker;

	@Bind(R.id.view_splash_login_btn) Button loginButton;
	@Bind(R.id.view_splash_register_btn) Button registerButton;

	public SplashView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@OnClick(R.id.view_splash_login_btn)
	public void onLoginButtonClicked() {
		if (!isInEditMode()) {
			eventTracker.track("Login button clicked");
			presenter.login();
		}
	}

	@OnClick(R.id.view_splash_register_btn)
	public void onRegisterButtonClicked() {
		if (!isInEditMode()) {
			eventTracker.track("Register button clicked");
			presenter.register();
		}
	}

	@Override
	protected Presenter getPresenter() {
		return presenter;
	}
}
