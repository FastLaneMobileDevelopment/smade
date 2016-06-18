package io.bega.servicebase.screen.login;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

/*import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult; */
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.Api;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import flow.Flow;
import io.bega.servicebase.R;
import io.bega.servicebase.SharedPreferencesKeys;
import io.bega.servicebase.actionbar.ActionBarConfig;
import io.bega.servicebase.actionbar.ActionBarOwner;
import io.bega.servicebase.activity.ActivityHelper;
import io.bega.servicebase.activity.WindowOwner;
import io.bega.servicebase.model.UserToken;
import io.bega.servicebase.model.UserWithPassword;
import io.bega.servicebase.repository.JsonSharedPreferencesRepository;
import io.bega.servicebase.screen.forgotlogin.login.ForgotLoginScreen;
import io.bega.servicebase.screen.forgotlogin.login.ForgotLoginView;
import io.bega.servicebase.screen.home.HomeScreen;
import io.bega.servicebase.screen.register.RegisterScreen;
import io.bega.servicebase.service.ApiService;
import io.bega.servicebase.util.logging.Logger;
import io.bega.servicebase.util.mortar.BaseViewPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class LoginPresenter extends BaseViewPresenter<LoginView> { //implements FacebookCallback<LoginResult> {

	private static final Logger LOG = Logger.getLogger(LoginPresenter.class);


	private ActionBarOwner actionBarOwner;

	private final Flow flow;

	private Context ctx;

	private ApiService apiService;

	private JsonSharedPreferencesRepository jsonSharedPreferencesRepository;

	@Inject
	LoginPresenter(Context context, Flow flow,  JsonSharedPreferencesRepository repository, ApiService apiService, ActionBarOwner actionBarOwner) {
		this.ctx = context;
		this.flow = flow;
		this.apiService = apiService;
		this.actionBarOwner = actionBarOwner;
		this.jsonSharedPreferencesRepository = repository;
	}

	@Override
	protected void onLoad(Bundle savedInstanceState) {
		super.onLoad(savedInstanceState);

		LoginView view = getView();
		if (view != null) {
			configureActionBar();
		}



	}

	@Override
	protected void onExitScope() {
		super.onExitScope();
	}

	public void forgot()
	{
		flow.goTo(new ForgotLoginScreen());
	}

	public void login(UserWithPassword userWithPassword) {
		try {

			Call<UserToken> userTokenCall = apiService.login(userWithPassword);


				userTokenCall.enqueue(new Callback<UserToken>() {
					@Override
					public void onResponse(Call<UserToken> call, Response<UserToken> response) {
						LOG.info("Navigating to home screen");
						UserToken token = response.body();
                        if (token == null)
                        {
                            try
                            {
                                Activity activity = ActivityHelper.getActivity();
                                jsonSharedPreferencesRepository.putObject(SharedPreferencesKeys.USER_TOKEN, null);
                                new MaterialDialog.Builder(activity)
                                        .title(R.string.login_error_identification_title)
                                        .content(R.string.login_error_identification_description)
                                        .positiveText(R.string.ok)
                                        .show();
                            }
                            catch (Exception ex)
                            {
                                LOG.error("Error searching activity", ex);
                            }



                            return;
                        }
						//DateTimeFormatter formatter =
						//		DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						String time = DateTime.now().toString();
						token.MobileCreated = time;
						// save token
						jsonSharedPreferencesRepository.putObject(SharedPreferencesKeys.USER_TOKEN, token);
						flow.goTo(new HomeScreen());
					}

					@Override
					public void onFailure(Call<UserToken> call, Throwable t) {
						Log.e("Error", "error", t);
                        if (t instanceof java.net.ConnectException)
                        {
                            try
                            {
                                Activity activity = ActivityHelper.getActivity();
                                jsonSharedPreferencesRepository.putObject(SharedPreferencesKeys.USER_TOKEN, null);
                                new MaterialDialog.Builder(activity)
                                        .title(R.string.login_error_connect_title)
                                        .content(R.string.login_error_connect_description)
                                        .positiveText(R.string.ok)
                                        .show();
                            }
                            catch (Exception ex)
                            {
                                LOG.error("Error searching activity", ex);
                            }

                        }


					}
				});

		}catch(Exception ex)
		{
			String p = ex.getMessage();
		}
	}

	public void register() {
		LOG.info("Navigating to register screen");
		flow.goTo(new RegisterScreen());
	}

	private void configureActionBar() {
		ActionBarConfig config = new ActionBarConfig.Builder()
				.title("Login")
                .enableHomeAsUp(true)
                .enableDrawerLayout(false)
				.build();
		actionBarOwner.setConfig(config);
	}

	/*@Override
	public void onSuccess(LoginResult loginResult) {

		flow.goTo(new HomeScreen());
	}

	@Override
	public void onCancel() {
		// display error
	}

	@Override
	public void onError(FacebookException error) {

	}*/


}
