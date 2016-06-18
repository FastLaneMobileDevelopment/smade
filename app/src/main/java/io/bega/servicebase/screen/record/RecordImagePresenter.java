package io.bega.servicebase.screen.record;

import android.os.Bundle;

import javax.inject.Inject;

import io.bega.servicebase.actionbar.ActionBarConfig;
import io.bega.servicebase.actionbar.ActionBarOwner;
import io.bega.servicebase.util.mortar.BaseViewPresenter;

class RecordImagePresenter extends BaseViewPresenter<RecordView> {

	private ActionBarOwner actionBarOwner;

	@Inject
	RecordImagePresenter(ActionBarOwner actionBarOwner) {
		this.actionBarOwner = actionBarOwner;
	}

	@Override
	protected void onLoad(Bundle savedInstanceState) {
		super.onLoad(savedInstanceState);

		RecordView view = getView();
		if (view != null) {
			configureActionBar();
		}
	}

	private void configureActionBar() {
		ActionBarConfig config = new ActionBarConfig.Builder()
				.title("Login")
				.build();
		actionBarOwner.setConfig(config);
	}
}
