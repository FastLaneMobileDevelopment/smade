package io.bega.servicebase.service.appointment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.gotev.uploadservice.BuildConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bega.servicebase.R;
import io.bega.servicebase.activity.ActivityHelper;
import io.bega.servicebase.activity.WindowOwner;

/**
 * Created by user on 5/19/16.
 */
public class UploadBroker {

    private static final String TAG = "UploadServiceDemo";
    private static final String USER_AGENT = "UploadServiceDemo/" + BuildConfig.VERSION_NAME;
    private static final int FILE_CODE = 1;

    private Map<String, UploadProgressViewHolder> uploadProgressHolders = new HashMap<>();

    private final UploadServiceBroadcastReceiver uploadReceiver =
            new UploadServiceBroadcastReceiver() {

                @Override
                public void onProgress(String uploadId, int progress) {
                    Log.i(TAG, "The progress of the upload with ID " + uploadId + " is: " + progress);

                    if (uploadProgressHolders.get(uploadId) == null)
                        return;

                    uploadProgressHolders.get(uploadId).progressBar.setProgress(progress);
                }

                @Override
                public void onError(String uploadId, Exception exception) {
                    Log.e(TAG, "Error in upload with ID: " + uploadId + ". "
                            + exception.getLocalizedMessage(), exception);

                    if (uploadProgressHolders.get(uploadId) == null)
                        return;

                   // container.removeView(uploadProgressHolders.get(uploadId).itemView);
                    uploadProgressHolders.remove(uploadId);
                }

                @Override
                public void onCompleted(String uploadId, int serverResponseCode, byte[] serverResponseBody) {
                    Log.i(TAG, "Upload with ID " + uploadId + " is completed: " + serverResponseCode + ", "
                            + new String(serverResponseBody));

                    if (uploadProgressHolders.get(uploadId) == null)
                        return;

                  //  container.removeView(uploadProgressHolders.get(uploadId).itemView);
                    uploadProgressHolders.remove(uploadId);
                }

                @Override
                public void onCancelled(String uploadId) {
                    Log.i(TAG, "Upload with ID " + uploadId + " is cancelled");

                    if (uploadProgressHolders.get(uploadId) == null)
                        return;

                  //  container.removeView(uploadProgressHolders.get(uploadId).itemView);
                    uploadProgressHolders.remove(uploadId);
                }
            };

            private String getFilename(String filepath) {
                if (filepath == null)
                    return null;

                final String[] filepathParts = filepath.split("/");

                return filepathParts[filepathParts.length - 1];
            }

           /* @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
                    List<Uri> resultUris = new ArrayList<>();

                    if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                        // For JellyBean and above
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ClipData clip = data.getClipData();

                            if (clip != null) {
                                for (int i = 0; i < clip.getItemCount(); i++) {
                                    resultUris.add(clip.getItemAt(i).getUri());
                                }
                            }

                            // For Ice Cream Sandwich
                        } else {
                            ArrayList<String> paths = data.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);

                            if (paths != null) {
                                for (String path: paths) {
                                    resultUris.add(Uri.parse(path));
                                }
                            }
                        }
                    } else {
                        resultUris.add(data.getData());
                    }

                    StringBuilder absolutePathsConcat = new StringBuilder();
                    for (Uri uri : resultUris) {
                        if (absolutePathsConcat.length() == 0) {
                            absolutePathsConcat.append(new File(uri.getPath()).getAbsolutePath());
                        } else {
                            absolutePathsConcat.append(",").append(new File(uri.getPath()).getAbsolutePath());
                        }
                    }

                    //filesToUpload.setText(absolutePathsConcat.toString());
                }
            }
            */

    class UploadProgressViewHolder {
        View itemView;

        @Bind(R.id.uploadTitle)
        TextView uploadTitle;
        @Bind(R.id.uploadProgress)
        ProgressBar progressBar;

        String uploadId;

        UploadProgressViewHolder(View view, String filename) {
            itemView = view;
            ButterKnife.bind(this, itemView);

            progressBar.setMax(100);
            progressBar.setProgress(0);

            try {
                Activity activity = ActivityHelper.getActivity();
                if (activity != null) {
                    String text = activity.getString(R.string.upload_progress, filename);
                    uploadTitle.setText(text);
                }
            }
            catch(Exception ex)
            {
                Log.e(TAG, "Error setting text", ex);
            }

        }

        @OnClick(R.id.cancelUploadButton)
        void onCancelUploadClick() {
            if (uploadId == null)
                return;

            UploadService.stopUpload(uploadId);
        }
    }
}
