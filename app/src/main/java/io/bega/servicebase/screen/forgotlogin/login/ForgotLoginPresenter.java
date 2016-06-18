package io.bega.servicebase.screen.forgotlogin.login;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/*import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult; */

import org.joda.time.DateTime;

import javax.inject.Inject;

import flow.Flow;
import io.bega.servicebase.SharedPreferencesKeys;
import io.bega.servicebase.actionbar.ActionBarConfig;
import io.bega.servicebase.actionbar.ActionBarOwner;
import io.bega.servicebase.model.UserToken;
import io.bega.servicebase.model.UserWithPassword;
import io.bega.servicebase.repository.JsonSharedPreferencesRepository;
import io.bega.servicebase.screen.home.HomeScreen;
import io.bega.servicebase.screen.register.RegisterScreen;
import io.bega.servicebase.service.ApiService;
import io.bega.servicebase.util.logging.Logger;
import io.bega.servicebase.util.mortar.BaseViewPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ForgotLoginPresenter extends BaseViewPresenter<ForgotLoginView> {

	private static final Logger LOG = Logger.getLogger(ForgotLoginPresenter.class);


	private ActionBarOwner actionBarOwner;

	private Flow flow;

	private Context ctx;

	private ApiService apiService;

	@Inject
	ForgotLoginPresenter(Context context, Flow flow, ApiService apiService, ActionBarOwner actionBarOwner) {
		this.ctx = context;
		this.flow = flow;
		this.apiService = apiService;
		this.actionBarOwner = actionBarOwner;
	}

	@Override
	protected void onLoad(Bundle savedInstanceState) {
		super.onLoad(savedInstanceState);

		ForgotLoginView view = getView();
		if (view != null) {
			configureActionBar();
		}
	}

	@Override
	protected void onExitScope() {
		super.onExitScope();
	}

	public void recover()
	{

	}


	private void configureActionBar() {
		ActionBarConfig config = new ActionBarConfig.Builder()
				.title("Recover password")
                .enableHomeAsUp(true)
                .enableDrawerLayout(false)
				.build();
		actionBarOwner.setConfig(config);
	}




}
