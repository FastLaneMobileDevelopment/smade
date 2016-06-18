package io.bega.servicebase.screen.main;

import javax.inject.Inject;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import flow.Flow;
import io.bega.servicebase.actionbar.ActionBarOwner;
import io.bega.servicebase.util.flow.FlowOwner;
import mortar.Blueprint;
import mortar.Mortar;
import io.bega.servicebase.util.flow.CanShowScreen;
import io.bega.servicebase.util.flow.ScreenConductor;

public class MainView extends FrameLayout implements CanShowScreen<Blueprint> {

	@Inject MainPresenter presenter;

	@Inject ActionBarOwner actionBarOwner;

	private ScreenConductor<Blueprint> screenConductor;

	private Context context;

	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (isInEditMode()) { return; }

		this.context = context;
		Mortar.inject(context, this);

		screenConductor = new ScreenConductor<>(context, this);
	}

	@Override
	public void showScreen(Blueprint screen, Flow.Direction direction) {
		screenConductor.showScreen(screen, direction);
	}

	public Flow getFlow() {
		return presenter.getFlow();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		if (isInEditMode()) { return; }

		if (context instanceof ActionBarOwner.View)
		{
			ActionBarOwner.View view =  (ActionBarOwner.View)context;
			actionBarOwner.takeView(view);
		}

		// hack to add the toolbar
		presenter.takeView(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();


		if (presenter instanceof FlowOwner) {

		} else {

			presenter.dropView(this);
		}
	}


}
