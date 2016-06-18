package io.bega.servicebase.screen.forgotlogin.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.facebook.login.widget.LoginButton;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import butterknife.Bind;
import butterknife.OnClick;
import io.bega.servicebase.R;
import io.bega.servicebase.analytics.EventTracker;
import io.bega.servicebase.model.UserWithPassword;
import io.bega.servicebase.util.mortar.BaseView;
import mortar.Presenter;

public class ForgotLoginView extends BaseView {


	@Inject
	EventTracker eventTracker;

	@Inject
	ForgotLoginPresenter presenter;

	public ForgotLoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected Presenter getPresenter() {
		return presenter;
	}

	@Override
	protected void onFinishInjectViews() {
		super.onFinishInjectViews();
		eventTracker.track("Login View");

	}


}
