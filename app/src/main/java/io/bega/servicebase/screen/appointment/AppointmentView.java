package io.bega.servicebase.screen.appointment;


import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;

import javax.inject.Inject;

import butterknife.Bind;
import io.bega.servicebase.R;
import io.bega.servicebase.activity.ActivityHelper;
import io.bega.servicebase.model.appointment.AppointmentData;
import io.bega.servicebase.model.appointment.OrderTask;
import io.bega.servicebase.model.service.AppointmentManager;
import io.bega.servicebase.screen.appointment.adapters.MainPagerAdapter;
import io.bega.servicebase.screen.appointment.adapters.SectionsPagerAdapter;
import io.bega.servicebase.util.mortar.BaseView;
import mortar.Presenter;

public class AppointmentView extends BaseView {

	Context ctx;

	FragmentManager manager;

	MainPagerAdapter  mainPagerAdapter;

	@Inject
	AppointmentPresenter presenter;

    @Inject
    AppCompatActivity activity;

	AppointmentData data;

	@Bind(R.id.view_appointment_tablayout_main)
	TabLayout mainTabLayout;

	@Bind(R.id.view_appointment_viewpager)
	ViewPager mainPager;

	FragmentManager  fragmentManager;

	public AppointmentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ctx = context;
		this.data = AppointmentManager.getInstance().getData();
	}



	@Override
	public void onFinishInflate()
	{
		super.onFinishInflate();

        if (isInEditMode()) { return; }


        OrderTask orderTask = presenter.getAppointmentData(this.data);



		Log.d("TAG","onFinishInflate AppointmentView");
        try {
            AppCompatActivity activityCompat = (AppCompatActivity)ActivityHelper.getActivity();
            fragmentManager = activityCompat.getSupportFragmentManager();
        }
        catch(Exception ex)
        {
            Log.e("TAG","error loading activity compat", ex);
        }

		mainPagerAdapter =  new MainPagerAdapter(orderTask, activity, fragmentManager);
		mainPager.setAdapter(mainPagerAdapter);

		// Give the TabLayout the ViewPager
		mainTabLayout.setupWithViewPager(mainPager);
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
       // mainTabLayout.destroyDrawingCache();
	}

	@Override
	protected Presenter getPresenter() {
		return presenter;
	}


}
