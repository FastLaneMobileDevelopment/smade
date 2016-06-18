package io.bega.servicebase.screen.appointment.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.helpers.ClickListenerHelper;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import io.bega.servicebase.R;
import io.bega.servicebase.model.appointment.IOrderTaskPhotoCreator;
import io.bega.servicebase.model.appointment.OrderTask;
import io.bega.servicebase.model.appointment.OrderTaskPhotoAdapter;
import io.bega.servicebase.model.appointment.TaskPhoto;
import io.bega.servicebase.model.event.PhotoMessageBusEvent;
import io.bega.servicebase.model.items.ImageDummyData;
import io.bega.servicebase.model.items.ImageItem;
import io.bega.servicebase.model.photo.PhotoType;
import io.bega.servicebase.model.service.Constants;
import io.bega.servicebase.screen.appointment.photo.AlbumStorageDirFactory;
import io.bega.servicebase.screen.appointment.photo.BaseAlbumDirFactory;
import io.bega.servicebase.screen.appointment.photo.FroyoAlbumDirFactory;
import io.bega.servicebase.screen.appointment.photo.HelperImage;
import io.bega.servicebase.screen.appointment.photo.PhotoIntentActivity;
import io.bega.servicebase.screen.main.MainActivity;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter implements View.OnClickListener {

    final Context context;

    final OrderTask orderTask;

    public SectionsPagerAdapter(OrderTask orderTask, Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.orderTask = orderTask;
    }



    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position, orderTask);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return this.context.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return this.context.getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return this.context.getString(R.string.title_section3).toUpperCase(l);
            case 3:
                return "";

        }
        return null;
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener, IOrderTaskPhotoCreator {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private int sectionNumber = 0;

        private int MakePhoto = 0x01;

        private int GalleryPhoto = 0x02;

        RecyclerView recyclerViewImageListItem;

        OrderTaskPhotoAdapter orderTaskPhotoAdapter;

        ClickListenerHelper<ImageItem> mClickListenerHelper;

        FastItemAdapter fastItemAdapter;

        Bus bus;

        Realm realm;

        OrderTask currentTask;

        private String mCurrentPhotoPath;

        PhotoType photoType;

        private static final String ARG_SECTION_NUMBER = "section_number";

        private static final String ARG_ORDER = "order_task";

        private static final int ACTION_TAKE_PHOTO = 1;

        private static final String JPEG_FILE_PREFIX = "IMG_";
        private static final String JPEG_FILE_SUFFIX = ".jpg";

        private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

        private File setUpPhotoFile() throws IOException {

            File f = createImageFile();
            mCurrentPhotoPath = f.getAbsolutePath();

            return f;
        }

        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
            File albumF = HelperImage.getAlbumDir();
            File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
            return imageF;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, OrderTask orderTask) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putParcelable(ARG_ORDER, Parcels.wrap(orderTask));
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }


        public void refreshData()
        {

            List<ImageItem> list = currentTask.getTaskPhotos(photoType);
            fastItemAdapter.clear();
            //fill with some sample data
            fastItemAdapter.add(list);
           // fastItemAdapter.notifyDataSetChanged();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments() != null) {
                currentTask = Parcels.unwrap(getArguments().getParcelable(ARG_ORDER));
                sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            }




            photoType = PhotoType.PreWork;
            if (sectionNumber == 1)
            {
                photoType = PhotoType.Form;
            }
            else if (sectionNumber == 2)
            {
                photoType = PhotoType.PostWork;
            }

            View rootView = inflater.inflate(R.layout.fragment_photo_main, container, false);
            FloatingActionButton button = ButterKnife.findById(rootView, R.id.fragment_photo_main_fab_new_photo);
            button.setOnClickListener(this);
            recyclerViewImageListItem = ButterKnife.findById(rootView, R.id.fragment_photo_main_list);
            fastItemAdapter = new FastItemAdapter<>();

            //init the ClickListenerHelper which simplifies custom click listeners on views of the Adapter
            mClickListenerHelper = new ClickListenerHelper<>(fastItemAdapter);

            //fastItemAdapter our fastAdapter
            fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<ImageItem>() {
                @Override
                public boolean onClick(View v, IAdapter<ImageItem> adapter, ImageItem item, int position) {
                    if (item instanceof ImageItem) {
                        final TaskPhoto taskPhoto  = ((ImageItem) item).mTaskPhoto;
                        MaterialDialog materialDialog = new
                                MaterialDialog.Builder(getContext())
                                .title(R.string.appointment_image_deletetitle)
                                .content(R.string.appointment_image_deletetitle)
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
                                        currentTask.deletePhoto(taskPhoto);
                                        realm.commitTransaction();
                                        refreshData();
                                    }
                                })
                                .show();

                    }


                    // Toast.makeText(v.getContext(), item.mName, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            fastItemAdapter.withOnLongClickListener(new FastAdapter.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v, IAdapter adapter, IItem item, int position) {
                    final TaskPhoto taskPhoto  = ((ImageItem) item).mTaskPhoto;
                    MaterialDialog materialDialog = new
                            MaterialDialog.Builder(getContext())
                            .title(R.string.appointment_image_deletetitle)
                            .content(R.string.appointment_image_deletetitle)
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
                                    currentTask.deletePhoto(taskPhoto);
                                    realm.commitTransaction();
                                    refreshData();


                                }
                            })
                            .show();
                    return false;
                }
            });

            //find out how many columns we display
            int columns = getResources().getInteger(R.integer.wall_splash_columns);
            if (columns == 1) {
                //linearLayoutManager for one column
                recyclerViewImageListItem.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                //gridLayoutManager for more than one column ;)
                recyclerViewImageListItem.setLayoutManager(new GridLayoutManager(getContext(), columns));
            }
            recyclerViewImageListItem.setItemAnimator(new DefaultItemAnimator());
            recyclerViewImageListItem.setAdapter(fastItemAdapter);

            List<ImageItem> list = currentTask.getTaskPhotos(photoType);
            //fill with some sample data
            fastItemAdapter.add(list);

            //restore selections (this has to be done after the items were added
            //mFastItemAdapter.withSavedInstanceState(savedInstanceState);

            //a custom OnCreateViewHolder listener class which is used to create the viewHolders
            //we define the listener for the imageLovedContainer here for better performance
            //you can also define the listener within the items bindView method but performance is better if you do it like this
            fastItemAdapter.withOnCreateViewHolderListener(new FastAdapter.OnCreateViewHolderListener() {
                @Override
                public RecyclerView.ViewHolder onPreCreateViewHolder(ViewGroup parent, int viewType) {
                    return fastItemAdapter.getTypeInstance(viewType).getViewHolder(parent);
                }

                @Override
                public RecyclerView.ViewHolder onPostCreateViewHolder(final RecyclerView.ViewHolder viewHolder) {
                    //we do this for our ImageItem.ViewHolder
                    if (viewHolder instanceof ImageItem.ViewHolder) {


                        //if we click on the imageLovedContainer
                        mClickListenerHelper.listen(viewHolder, ((ImageItem.ViewHolder) viewHolder).itemView, new ClickListenerHelper.OnClickListener<ImageItem>() {
                            @Override
                            public void onClick(View v, int position, ImageItem item) {


                                //item.withStarred(!item.mStarred);
                                //we animate the heart
                                //item.animateHeart(((ViewGroup) v).getChildAt(0), ((ViewGroup) v).getChildAt(1), item.mStarred);

                                //we display the info about the click
                                //Toast.makeText(getContext(), item.mImageUrl + " - " + item.mStarred, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    return viewHolder;
                }
            });


            return rootView;
        }

        @Override
        public void onClick(View view) {
           // Intent photoIntent =
            //        new Intent().setClass(getContext(), PhotoIntentActivity.class);
            photoType = PhotoType.PreWork;
            if (sectionNumber == 1)
            {
                photoType = PhotoType.Form;
            }
            else if (sectionNumber == 2)
            {
                photoType = PhotoType.PostWork;
            }

           // photoIntent.putExtra(Constants.PHOTO_TYPE, photoType);

            String taskId = "ZAQ4941";

            Log.v("PhotoIntentActivity", "TaskId: " + taskId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
            } else {
                mAlbumStorageDirFactory = new BaseAlbumDirFactory();
            }


            dispatchTakePictureIntent(ACTION_TAKE_PHOTO);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case ACTION_TAKE_PHOTO: {
                    if (resultCode == Activity.RESULT_OK) {

                        handleBigCameraPhoto();
                        refreshData();

                    }
                    break;
                }
            } // switch

        }

        private void createNewTaskPhoto(OrderTask task, String path, PhotoType type)
        {
            if (realm == null)
            {
                realm = Realm.getDefaultInstance();
            }

            realm.beginTransaction();
            TaskPhoto newTaskPhoto = new TaskPhoto(task.getOrderID(), path, type);
            newTaskPhoto = realm.copyToRealm(newTaskPhoto);
            task.addPhoto(newTaskPhoto);
            //task.getTaskPhotos().add(newTaskPhoto);
            realm.commitTransaction();
        }

        private void galleryAddPic() {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getContext().sendBroadcast(mediaScanIntent);
        }

        private void handleBigCameraPhoto() {

            if (mCurrentPhotoPath != null) {

                galleryAddPic();
                createNewTaskPhoto(currentTask, mCurrentPhotoPath, photoType);
                mCurrentPhotoPath = null;

            }

        }

        private void dispatchTakePictureIntent(int actionCode) {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra("order", Parcels.wrap(currentTask));


            switch (actionCode) {
                case ACTION_TAKE_PHOTO:
                    File f = null;

                    try {
                        f = setUpPhotoFile();
                        mCurrentPhotoPath = f.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    } catch (IOException e) {
                        e.printStackTrace();
                        f = null;
                        mCurrentPhotoPath = null;
                    }
                    catch (Exception ex)
                    {
                        Log.e(PhotoIntentActivity.class.toString(), ex.getMessage());
                    }
                    break;

                default:
                    break;
            } // switch

            startActivityForResult(takePictureIntent, actionCode);
        }

        @Override
        public void createTaskPhoto(TaskPhoto taskPhoto) {

        }

        @Override
        public void deleteTaskPhoto(TaskPhoto taskPhoto) {

        }

        @Override
        public void refreshTaskPhoto(PhotoType type) {

        }

        @Override
        public void changeTaskPhoto(TaskPhoto taskPhoto) {

        }
    }
}