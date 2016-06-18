package io.bega.servicebase.screen.appointment.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.bega.servicebase.R;
import io.bega.servicebase.model.appointment.OrderTask;
import io.bega.servicebase.screen.appointment.fragments.AppointmentDataFragment;
import io.bega.servicebase.screen.appointment.fragments.AppointmentPhotoFragment;
import io.bega.servicebase.screen.appointment.fragments.AppointmentWorkforceFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    Context context;

    OrderTask orderTask;

    AppointmentDataFragment appointmentDataFragment;

    AppointmentWorkforceFragment appointmentWorkforceFragment;

    AppointmentPhotoFragment appointmentPhotoFragment;


    @Inject
    public MainPagerAdapter(OrderTask orderTask, Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.orderTask = orderTask;
    }



    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                    return AppointmentDataFragment.newInstance(orderTask);
            case 1:
                    return AppointmentWorkforceFragment.newInstance(orderTask);
            case 2:
                    return AppointmentPhotoFragment.newInstance(orderTask);
        }

        return null;
    }


    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return context.getString(R.string.appointment_tab_titledata);  //this.mActivity.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return context.getString(R.string.appointment_tab_titleextra);  //this.mActivity.getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return context.getString(R.string.appointment_tab_titlephotos);  //this.mActivity.getString(R.string.title_section3).toUpperCase(l);
        }

        return null;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_photo_main, container, false);
            return rootView;
        }
    }
}