package io.bega.servicebase.screen.appointment.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcel;
import org.parceler.Parcels;

import io.bega.servicebase.R;
import io.bega.servicebase.model.appointment.OrderTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppointmentWorkforceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppointmentWorkforceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentWorkforceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TASK = "param1";

    // TODO: Rename and change types of parameters
    private OrderTask orderTask;

    public AppointmentWorkforceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentWorkforceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentWorkforceFragment newInstance(OrderTask orderTask) {
        AppointmentWorkforceFragment fragment = new AppointmentWorkforceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, Parcels.wrap(orderTask));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderTask = Parcels.unwrap(getArguments().getParcelable(ARG_TASK));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_appointment_worforce, container, false);
        return view;
    }





}
