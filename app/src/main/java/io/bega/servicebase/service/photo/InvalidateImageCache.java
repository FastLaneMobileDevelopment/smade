package io.bega.servicebase.service.photo;

import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.ExceptionCatchingInputStream;

import io.bega.servicebase.Application;

import static io.bega.servicebase.activity.ActivityHelper.getActivity;

/**
 * Created by user on 5/27/16.
 */
public class InvalidateImageCache extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Glide.get(getActivity()).clearDiskCache();
            // Glide.get(getActivity()).clearMemory();
        } catch (Exception ex) {
            Log.e("InvalidateCache", "Error invalidating Glide", ex);
            return false;
        }

        return true;
    }

}
