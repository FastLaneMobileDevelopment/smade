package io.bega.servicebase.screen.appointment.photo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



import android.media.ExifInterface;
import android.graphics.Matrix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.VideoView;
import android.graphics.drawable.BitmapDrawable;

import com.squareup.otto.Bus;

import org.parceler.Parcels;

import javax.inject.Inject;

import io.bega.servicebase.Application;
import io.bega.servicebase.R;
import io.bega.servicebase.activity.ActivityHelper;
import io.bega.servicebase.model.appointment.OrderTask;
import io.bega.servicebase.model.appointment.TaskPhoto;
import io.bega.servicebase.model.event.MenuMessageBusEvent;
import io.bega.servicebase.model.event.PhotoMessageBusEvent;
import io.bega.servicebase.model.items.ExpandableItem;
import io.bega.servicebase.model.photo.PhotoType;
import io.bega.servicebase.model.service.Constants;
import io.bega.servicebase.screen.main.MainActivity;
import io.bega.servicebase.screen.main.MainScreen;
import io.bega.servicebase.screen.register.RegisterView;
import io.realm.Realm;
import io.realm.RealmResults;


public class PhotoIntentActivity extends Activity {

    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_PHOTO_S = 2;
    private static final int ACTION_TAKE_VIDEO = 3;
    private Realm realm;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private static final String VIDEO_STORAGE_KEY = "viewvideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
    private VideoView mVideoView;
    private Uri mVideoUri;
    private Spinner spinner;

    private String mCurrentPhotoPath;


    @Inject
    Bus bus;

    // Added by Jordi

    private OrderTask currentTask;

    private PhotoType photoType;

    private String mSaveCurrentPhotoPath;



    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = HelperImage.getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }
    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch (actionCode) {
            case ACTION_TAKE_PHOTO_B:
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

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
    }

    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);
        mVideoUri = null;
        mImageView.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.INVISIBLE);
    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            		/* Get the size of the ImageView */
            //int targetW = mImageView.getWidth();
            //int targetH = mImageView.getHeight();
            // Bitmap image = HelperImage.decodeFile(mCurrentPhotoPath, targetW, targetH);
            /* Associate the Bitmap to the ImageView */
            // mImageView.setImageBitmap(image);

            mVideoUri = null;
            // mImageView.setVisibility(View.VISIBLE);

            galleryAddPic();
            // image.recycle();
            mSaveCurrentPhotoPath = mCurrentPhotoPath;
            mCurrentPhotoPath = null;

        }

    }

    private void handleCameraVideo(Intent intent) {
        mVideoUri = intent.getData();
        mVideoView.setVideoURI(mVideoUri);
        mImageBitmap = null;
        mVideoView.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.INVISIBLE);
    }

    Button.OnClickListener mTakePicOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
                }
            };

    Button.OnClickListener mButtonOkClickListener =
            new Button.OnClickListener() {

                @Override
                public void onClick(View view) {

                    // TODO refactor

                     Bitmap bitmap = HelperImage.decodeFile(mSaveCurrentPhotoPath, 256, 256);
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);



                                        // mImageView.setImageDrawable(null);
                   // createNewTaskPhoto(currentTask,  mSaveCurrentPhotoPath, photoType);

                    getParent().setResult(Activity.RESULT_OK);

                    mImageView = null;

                    finish();
                }
            };


    Button.OnClickListener mButtonCancelClickListener =
            new Button.OnClickListener() {

                @Override
                public void onClick(View view) {
                   // getParent().setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            };


	Button.OnClickListener mTakePicSOnClickListener = 
		new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
		}
	};

	Button.OnClickListener mTakeVidOnClickListener = 
		new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakeVideoIntent();
		}
	};


    /*private void createNewTaskPhoto(OrderTask task, String path, PhotoType type)
    {
        realm.beginTransaction();
        TaskPhoto newTaskPhoto = new TaskPhoto(task.GenerateNewIdForPhoto(), path, type);
        task.addPhoto(newTaskPhoto);
        realm.commitTransaction();
    } */


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_intent);
        ((Application)getApplication()).Inject(this);
        realm = Realm.getDefaultInstance();
		mImageView = (ImageView) findViewById(R.id.imageView1);
		// mVideoView = (VideoView) findViewById(R.id.videoView1);
		mImageBitmap = null;
		mVideoUri = null;


        // Button for control the adquisition of the photo
		// Button picBtn = (Button) findViewById(R.id.btnIntend);
        Button btnOk = (Button) findViewById(R.id.photo_intent_btnPhotoOk);
        Button btnCancel = (Button)findViewById(R.id.photo_intent_btnPhotoCancel);

        btnOk.setOnClickListener(mButtonOkClickListener);
        btnCancel.setOnClickListener(mButtonCancelClickListener);


        // spinner for select the type of photo
        spinner = (Spinner)findViewById(R.id.photo_intent_spinner_state);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_dropdown_item_1line);


        spinner.setAdapter(adapter);

        currentTask = Parcels.unwrap(getIntent().getParcelableExtra("order"));


        String taskId = getIntent().getStringExtra(Constants.ARG_ITEM_ID);

        photoType = (PhotoType)getIntent().getSerializableExtra(Constants.PHOTO_TYPE);

        Log.v("PhotoIntentActivity", "TaskId: " + taskId);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}


        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTION_TAKE_PHOTO_B: {
			if (resultCode == RESULT_OK) {

                /*RealmResults<OrderTask> results1 =
                        realm.where(OrderTask.class).findAll();
                if (results1.size() == 0)
                {
                    currentTask = new OrderTask("ZAQ4941","Montornes del Vallés");
                    realm.beginTransaction();
                    OrderTask orderTaskRealm = realm.copyToRealm(currentTask);
                    realm.commitTransaction();
                }
                else
                {
                    currentTask = results1.first();
                } */



                handleBigCameraPhoto();
               // Bitmap bitmap = HelperImage.decodeFile(mSaveCurrentPhotoPath, 256, 256);
               // Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                try {
                    mImageView.setImageDrawable(null);
                    //createNewTaskPhoto(currentTask, mSaveCurrentPhotoPath, photoType);
                    mImageView = null;
                    bus.post(new PhotoMessageBusEvent<Integer>(0x01, null));
                }
                catch(Exception ex)
                {
                    Log.e("Error", "error", ex);
                }

                finish();


			}
			break;
		}
		} // switch
	}

	// Some lifecycle callbacks so that the image can survive orientation change
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
		// outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null) );
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		// mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
		mImageView.setImageBitmap(mImageBitmap);
		mImageView.setVisibility(
				savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ? 
						ImageView.VISIBLE : ImageView.INVISIBLE
		);


        // mVideoView.setVideoURI(mVideoUri);
		//mVideoView.setVisibility(
		//		savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ?
		//				ImageView.VISIBLE : ImageView.INVISIBLE
		//);
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
			packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	private void setBtnListenerOrDisable( 
			Button btn, 
			Button.OnClickListener onClickListener,
			String intentName
	) {
		if (isIntentAvailable(this, intentName)) {
			btn.setOnClickListener(onClickListener);        	
		} else {
			btn.setText( 
				getText(R.string.cannot).toString() + " " + btn.getText());
			btn.setClickable(false);
		}
	}

}