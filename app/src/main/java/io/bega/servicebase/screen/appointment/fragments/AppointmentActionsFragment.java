package io.bega.servicebase.screen.appointment.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import io.bega.servicebase.R;
import io.bega.servicebase.model.appointment.OrderTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentActionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentActionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ORDER = "order";

    // TODO: Rename and change types of parameters
    private OrderTask orderTask;

    public AppointmentActionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AppointmentActionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentActionsFragment newInstance(OrderTask orderTask) {
        AppointmentActionsFragment fragment = new AppointmentActionsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ORDER, Parcels.wrap(orderTask));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderTask = Parcels.unwrap(getArguments().getParcelable(ARG_ORDER));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment_actions, container, false);
    }

}
