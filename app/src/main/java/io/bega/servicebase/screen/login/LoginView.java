package io.bega.servicebase.screen.login;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import android.content.Context;
import android.os.Debug;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.facebook.login.widget.LoginButton;

import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;
import io.bega.servicebase.R;
import io.bega.servicebase.analytics.EventTracker;
import io.bega.servicebase.model.UserWithPassword;
import io.bega.servicebase.model.service.Operator;
import mortar.Presenter;
import io.bega.servicebase.util.mortar.BaseView;

public class LoginView extends BaseView {


	@Inject
	EventTracker eventTracker;

	@Inject LoginPresenter presenter;

	@Bind(R.id.view_login_btn_login)
	Button btnLogin;

	@Bind(R.id.view_login_email_field)
	EditText edit_email_field;

	@Bind(R.id.view_login_password_field)
	EditText editPassword;

	@Bind(R.id.view_login_btn_signup)
	Button btnRegister;

	public LoginView(Context context, AttributeSet attrs) {
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

        //if (Debug.isDebuggerConnected())
        //{
            edit_email_field.setText("jordi.buges@gmail.com");
            editPassword.setText("1234");
        //}
	}

	@OnClick(R.id.view_login_btn_signup)
	public void onSignupButtonClick()
	{
		eventTracker.track("Signup from login");
		presenter.register();
	}

	@OnClick(R.id.view_login_btn_login)
	public void onLoginButtonClicked() {
		eventTracker.track("Login Enter button clicked");
		if (!validate())
		{
			return;
		}

		String email = edit_email_field.getText().toString();
		String password = editPassword.getText().toString();
		presenter.login(new UserWithPassword("",email, password));
	}

	@OnClick(R.id.view_login_btn_forgot)
	public void onForgotButtonClicked()
	{
		presenter.forgot();
	}



	public void onValidationError(Set<ConstraintViolation<UserWithPassword>> errors) {
		btnLogin.setEnabled(true);

		for (ConstraintViolation<UserWithPassword> error : errors) {
			String property = error.getPropertyPath().toString();
			if (property.equals("email")) {
				//editEmail.setError(error.getMessage());
			} else if (property.equals("password")) {
				editPassword.setError(error.getMessage());
			}
		}
	}

	public boolean validate() {
		boolean valid = true;

		String email = edit_email_field.getText().toString();
		String password = editPassword.getText().toString();

		if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			editPassword.setError("enter a valid email address");
			valid = false;
		} else {
			edit_email_field.setError(null);
		}

		if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
			editPassword.setError("between 4 and 10 alphanumeric characters");
			valid = false;
		} else {
			editPassword.setError(null);
		}

		return valid;
	}
}
