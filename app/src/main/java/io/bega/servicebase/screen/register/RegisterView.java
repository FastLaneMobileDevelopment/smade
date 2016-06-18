package io.bega.servicebase.screen.register;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import butterknife.Bind;
import io.bega.servicebase.R;
import io.bega.servicebase.model.UserWithPassword;
import io.bega.servicebase.util.mortar.BaseView;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import butterknife.OnClick;
import mortar.Presenter;

public class RegisterView extends BaseView {

  @Inject RegisterPresenter presenter;

  @Bind(R.id.register_view_name_field) EditText nameEditText;
  @Bind(R.id.register_view_email_field) EditText emailEditText;
  @Bind(R.id.register_view_password_field) EditText passwordEditText;
  @Bind(R.id.register_view_btn_register) Button registerButton;

  AlertDialog alertDialog;

  public RegisterView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @OnClick(R.id.register_view_btn_register)
  public void onRegisterButtonClicked() {
    registerButton.setEnabled(false);

    hideKeyboard();

    String name = nameEditText.getText().toString();
    String email = emailEditText.getText().toString();
    String password = passwordEditText.getText().toString();


    presenter.register(name, email, password);
  }

  public void onValidationError(Set<ConstraintViolation<UserWithPassword>> errors) {
    registerButton.setEnabled(true);

    for (ConstraintViolation<UserWithPassword> error : errors) {
      String property = error.getPropertyPath().toString();
      if (property.equals("name")) {
        nameEditText.setError(error.getMessage());
      } else if (property.equals("email")) {
        emailEditText.setError(error.getMessage());
      } else if (property.equals("password")) {
        passwordEditText.setError(error.getMessage());
      }
    }
  }



  public void onRegistrationError(Throwable thrown) {
    registerButton.setEnabled(true);

    alertDialog = showBasicAlertDialog(getString(R.string.dialog_error_title), thrown.getMessage());
  }

  @Override
  protected Presenter getPresenter() {
    return presenter;
  }

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();

    if (alertDialog != null && alertDialog.isShowing()) {
      alertDialog.dismiss();
    }
  }
}
