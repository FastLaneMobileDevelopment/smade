package io.bega.servicebase.screen.record;

import android.content.Context;
import android.util.AttributeSet;

import javax.inject.Inject;

import io.bega.servicebase.util.mortar.BaseView;
import mortar.Presenter;

public class RecordImageView extends BaseView {

	@Inject
	RecordPresenter presenter;

	public RecordImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected Presenter getPresenter() {
		return presenter;
	}
}
