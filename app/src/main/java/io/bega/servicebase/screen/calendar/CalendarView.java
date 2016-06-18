package io.bega.servicebase.screen.calendar;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Button;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import butterknife.Bind;
import flow.Flow;
import io.bega.servicebase.R;
import io.bega.servicebase.util.flow.CanShowScreen;
import io.bega.servicebase.util.flow.ScreenConductor;
import io.bega.servicebase.util.mortar.BaseView;
import mortar.Blueprint;
import mortar.Presenter;

public class CalendarView extends BaseView {

	@Inject
	CalendarPresenter presenter;

	@Bind(R.id.view_calendar_weekView) WeekView mWeekView;


	public CalendarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public void onFinishInflate()
	{
		super.onFinishInflate();
		if (!this.isInEditMode()) {
			mWeekView.setOnEventClickListener(presenter);
			mWeekView.setMonthChangeListener(presenter);
			mWeekView.setEventLongPressListener(presenter);
			mWeekView.setEmptyViewClickListener(presenter);
			mWeekView.setEmptyViewLongPressListener(presenter);
			// mWeekView.setWeekViewLoader(presenter);
			mWeekView.setShowNowLine(true);
			mWeekView.refreshDrawableState();
		}

	}

	@Override
	protected Presenter getPresenter() {
		return presenter;
	}



}
