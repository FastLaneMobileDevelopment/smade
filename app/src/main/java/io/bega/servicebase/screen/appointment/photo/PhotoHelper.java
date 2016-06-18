package io.bega.servicebase.screen.appointment.photo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import io.bega.servicebase.R;
import io.bega.servicebase.model.photo.IPhotoChooser;

/**
 * Created by usuario on 06/02/15.
 */
public class PhotoHelper {
    public static AlertDialog getPhotoDialog(final IPhotoChooser chooser) {

        Activity activity = (Activity)chooser;
        AlertDialog.Builder builder = new AlertDialog.Builder(
                activity);
        builder.setTitle(R.string.photo_source);
        builder.setPositiveButton(R.string.action_camera, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                chooser.fromCamera();


            }

        });
        builder.setNegativeButton(R.string.action_gallery, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                chooser.fromGallery();
            }

        });

        return builder.create();
    }
}
