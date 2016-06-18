package io.bega.servicebase.screen.appointment.photo;




import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import io.bega.servicebase.R;
import io.bega.servicebase.model.appointment.IOrderTaskCreator;
import io.bega.servicebase.model.appointment.TaskPhoto;
import io.bega.servicebase.model.photo.PhotoType;
import io.bega.servicebase.model.service.Constants;


public class ChangeStateDialogFragment extends DialogFragment implements OnClickListener {


    private Spinner spinner;

    private Button mbtnOk;

    private Button mbtnCancel;

    private TaskPhoto currentTaskPhoto;

    IOrderTaskCreator taskCreator;

    public ChangeStateDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public void setData(TaskPhoto photo)
    {
        this.currentTaskPhoto = photo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        getDialog().setTitle(this.getActivity().getString(R.string.change_state));
        View view = inflater.inflate(R.layout.fragment_dialog_change_state, container);

        spinner = (Spinner) view.findViewById(R.id.fragment_dialog_change_state_spinner_state);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.states, android.R.layout.simple_dropdown_item_1line);


        spinner.setAdapter(adapter);
       // mLocText = (EditText) view.findViewById(R.id.fragment_new_task_txt_tracknumber);
       // mDescriptionText = (EditText)view.findViewById(R.id.fragment_new_task_txt_description);
        mbtnOk = (Button)view.findViewById(R.id.fragment_new_task_button_newtask_ok);
        mbtnCancel = (Button)view.findViewById(R.id.fragment_new_task_button_newtask_cancel);
        mbtnOk.setOnClickListener(this);
        mbtnCancel.setOnClickListener(this);
        return view;
    }

    
   

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.fragment_new_task_button_newtask_ok)
		{


            int position = spinner.getSelectedItemPosition();
            PhotoType type = PhotoType.PostWork;
            switch (position) {
                case 0:

                {
                    type = PhotoType.PreWork;
                }
                break;

                case 1:

                {
                    type = PhotoType.Form;

                }

                break;

                case 2:

                {
                    type = PhotoType.PostWork;
                }
                break;
            }





			//String idTask = mIdText.getText().toString();
			//String localizator = mLocText.getText().toString();
			//String description = mDescriptionText.getText().toString();
            //TaskDetailActivity activity = (TaskDetailActivity) getActivity();

            currentTaskPhoto.setType(type);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent().putExtra(Constants.PHOTO_TYPE, type));




            //OrderTask newTask = new OrderTask(idTask, "");
           // newTask.setDescription(description);
            //activity.createNewTask(newTask);

           
		}
		
		this.dismiss();
	} 
}