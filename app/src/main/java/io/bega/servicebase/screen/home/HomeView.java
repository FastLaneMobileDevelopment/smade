package io.bega.servicebase.screen.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import flow.Flow;
import io.bega.servicebase.R;
import io.bega.servicebase.SharedPreferencesKeys;
import io.bega.servicebase.model.User;
import io.bega.servicebase.util.flow.CanShowScreen;
import io.bega.servicebase.util.flow.ScreenConductor;
import io.bega.servicebase.util.mortar.BaseView;
import mortar.Blueprint;
import mortar.Mortar;
import mortar.Presenter;

public class HomeView extends BaseView {

	@Inject
	HomePresenter presenter;

	public HomeView(Context context, AttributeSet attrs) {
		super(context, attrs);


		if (isInEditMode()) { return; }

		// Mortar.inject(context, this);


	}

	@Override
	protected Presenter getPresenter() {
		return presenter;
	}

}
