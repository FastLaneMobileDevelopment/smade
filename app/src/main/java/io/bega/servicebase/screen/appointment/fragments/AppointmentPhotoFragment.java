package io.bega.servicebase.screen.appointment.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import io.bega.servicebase.R;
import io.bega.servicebase.activity.ActivityHelper;
import io.bega.servicebase.model.appointment.OrderTask;
import io.bega.servicebase.screen.appointment.adapters.SectionsPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentPhotoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TASK = "param1";

    private OrderTask orderTask;


    public AppointmentPhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AppointmentPhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentPhotoFragment newInstance(OrderTask orderTask) {
        AppointmentPhotoFragment fragment = new AppointmentPhotoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, Parcels.wrap(orderTask));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderTask =  Parcels.unwrap(getArguments().getParcelable(ARG_TASK));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_appointment_photo, container, false);
        TabLayout tabLayout =  ButterKnife.findById(view, R.id.fragment_appointment_photo_tablayout_main);
        ViewPager viewPager = ButterKnife.findById(view, R.id.fragment_appointment_photo_viewpager);
        viewPager.setAdapter(new SectionsPagerAdapter(orderTask, getContext(), getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
