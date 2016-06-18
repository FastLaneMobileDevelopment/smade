package io.bega.servicebase.screen.appointment.fragments;



import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.joda.time.DateTime;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.bega.servicebase.Application;
import io.bega.servicebase.R;
import io.bega.servicebase.model.appointment.OrderTask;
import io.bega.servicebase.screen.login.LoginView;
import io.bega.servicebase.screen.main.MainScreen;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TASK = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private  OrderTask orderTask;


    public AppointmentDataFragment() {
        // Required empty public constructor
    }

    Realm realm;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AppointmentDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentDataFragment newInstance(OrderTask orderTask) {
        AppointmentDataFragment fragment = new AppointmentDataFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, Parcels.wrap(orderTask));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderTask =  Parcels.unwrap( getArguments().getParcelable(ARG_TASK));
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_appointment_data, container, false);
        FloatingActionButton actionButton = ButterKnife.findById(view, R.id.fragment_appointment_data_fab_enter);


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewButton) {
                MaterialDialog materialDialog = new
                                    MaterialDialog.Builder(getContext())
                                            .title(R.string.dialog_enter_appointment)
                                            .content(R.string.dialog_enter_appointment_confirm)
                                            .positiveText(R.string.ok_dialog)
                                            .negativeText(R.string.cancel_dialog)
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    if (realm == null)
                                                    {
                                                        realm = Realm.getDefaultInstance();
                                                    }

                                                    realm.beginTransaction();
                                                    orderTask.setDateEnter(DateTime.now());
                                                    realm.commitTransaction();
                                                    TextView dateEnter = ButterKnife.findById(view, R.id.fragment_appointment_data_report_date_enter);
                                                    View viewTop = ButterKnife.findById(view, R.id.fragment_appointment_data_anchor_visit_top);
                                                    dateEnter.setText(getResources().getString(R.string.appointment_info_dateenter, DateTime.now()));
                                                    dateEnter.setVisibility(View.VISIBLE);
                                                    viewTop.setVisibility(View.VISIBLE);
                                                }
                                            })
                                            .show();

            }
        });



        TextView reportTxt = ButterKnife.findById(view, R.id.fragment_appointment_data_report);
        reportTxt.setText(getResources().getString(R.string.appointment_info_report, orderTask.getOrderID()));
        TextView reportCompanyTxt = (TextView)view.findViewById(R.id.fragment_appointment_data_report_company);
        reportCompanyTxt.setText(view.getResources().getText(R.string.appointment_info_company, orderTask.getOrderID()));




        TextView ciaTxt = (TextView)view.findViewById(R.id.fragment_appointment_data_cia);
        ciaTxt.setText(view.getResources().getText(R.string.appointment_info_report, orderTask.getStatus().toString()));

        TextView companyTxt = (TextView)view.findViewById(R.id.fragment_appointment_data_company);
        companyTxt.setText("Cliente: AIDE");

        TextView contractTxt = (TextView)view.findViewById(R.id.fragment_appointment_data_contract);
        contractTxt.setText("Contrato: PROTECCION HOGAR BASIC");



        TextView telephoneTxt = (TextView)view.findViewById(R.id.fragment_appointment_data_telephone);
        telephoneTxt.setText("Teléfonos: 610579257");

        TextView assuranceTxt = (TextView)view.findViewById(R.id.fragment_appointment_data_assurance);
        assuranceTxt.setText("Asegurado: ROSARIO OLIVER PEREZ - 38418747F");




        TextView locationTxt = (TextView)view.findViewById(R.id.fragment_appointment_data_location);
        locationTxt.setText("Calle JACINT VERDAGUER 11 BAJO 3ª - 08170 \n Montornes del Vallès (Vallès Oriental)");

        TextView requestText = (TextView)view.findViewById(R.id.fragment_appointment_data_request);
        requestText.setText("ROGAMOS ENVIO DE REPARADOR POR POSIBLE ROTURA DE TUBERIA CAUSA HUMEDAD EN PARED DE LA COCINA, GRACIAS, calle JACINT VERDAGUER, 08170 - MONTORNES DEL VALLES - Barcelona - ESPAÑA Nº11 BAJO 3ª");


        return view;
    }

}
