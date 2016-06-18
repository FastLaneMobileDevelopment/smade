package io.bega.servicebase.record.register;

import android.os.Bundle;
import android.util.Log;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import flow.Flow;
import io.bega.servicebase.SharedPreferencesKeys;
import io.bega.servicebase.actionbar.ActionBarConfig;
import io.bega.servicebase.actionbar.ActionBarOwner;
import io.bega.servicebase.model.User;
import io.bega.servicebase.model.UserWithPassword;
import io.bega.servicebase.model.service.Operator;
import io.bega.servicebase.repository.JsonSharedPreferencesRepository;
import io.bega.servicebase.screen.login.LoginScreen;
import io.bega.servicebase.service.ApiService;
import io.bega.servicebase.util.logging.Logger;
import io.bega.servicebase.util.mortar.BaseViewPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

class RegisterPresenter extends BaseViewPresenter<RegisterView> {

  private static final Logger LOG = Logger.getLogger(RegisterPresenter.class);

  private final Flow flow;
  private final ActionBarOwner actionBarOwner;
  private final Validator validator;
  private final ApiService apiService;
  private final JsonSharedPreferencesRepository prefsRepository;

  private Subscription registerSubscription;

  @Inject
  RegisterPresenter(Flow flow, ActionBarOwner actionBarOwner, Validator validator, ApiService apiService, JsonSharedPreferencesRepository prefsRepository) {
    this.flow = flow;
    this.actionBarOwner = actionBarOwner;
    this.validator = validator;
    this.apiService = apiService;
    this.prefsRepository = prefsRepository;
  }

  @Override
  protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);

    RegisterView view = getView();
    if (view != null) {
      configureActionBar();
    }
  }

  @Override
  public void dropView(RegisterView view) {
    if (registerSubscription != null && !registerSubscription.isUnsubscribed()) {
      registerSubscription.unsubscribe();
    }

    super.dropView(view);
  }

  private void configureActionBar() {
    ActionBarConfig config = new ActionBarConfig.Builder()
      .title("Register")
      .build();
    actionBarOwner.setConfig(config);
  }

  public void register(String name, String email, String password) {
    RegisterView view = getView();




    UserWithPassword user = new UserWithPassword(trimToEmpty(name), trimToEmpty(email), trimToEmpty(password));
    Set<ConstraintViolation<UserWithPassword>> errors = validator.validate(user);

    if (errors.isEmpty()) {
        Operator operator = new Operator(name, email, password);
        registerWithApi(operator);
    } else {
      view.onValidationError(errors);
    }
  }

  private void registerWithApi(Operator operator) {
    try {



      Call<Operator> operatorCall = apiService.register(operator);
      operatorCall.enqueue(new Callback<Operator>() {
        @Override
        public void onResponse(Call<Operator> call, Response<Operator> response) {

          // save token



          flow.goTo(new LoginScreen());
        }

        @Override
        public void onFailure(Call<Operator> call, Throwable t) {
          Log.e("Error", "error", t);
        }
      });
    }catch(Exception ex)
    {
      String p = ex.getMessage();
    }



    //registerSubscription = apiService.register(user);
      /*.subscribe(new Action1<User>() {
        @Override
        public void call(User user) {
          storeUser(user);
          flow.resetTo(new HomeScreen());
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable thrown) {
          getView().onRegistrationError(thrown);
        }
      }); */
  }

  private void storeUser(User user) {
    prefsRepository.putObject(SharedPreferencesKeys.USER_ACCOUNT, user);
  }
}
