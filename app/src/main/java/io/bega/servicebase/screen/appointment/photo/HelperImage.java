package io.bega.servicebase.screen.appointment.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.util.Log;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.bega.servicebase.model.photo.PhotoType;

/**
 * Created by usuario on 19/06/14.
 */
public class HelperImage {

    private static final String JPEG_FILE_PREFIX = "IMG_";

    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private  static AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    /* Photo album for this application */
    private static String getAlbumName() {
        return "intervagestion";
    }



     public static File getAlbumDir() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
             mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
         } else {
             mAlbumStorageDirFactory = new BaseAlbumDirFactory();
         }

         File storageDir = null;


        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v("intervagestion", "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    public static File createImageFile(PhotoType type, String id, String number, String total) throws IOException {

            String nameType = "Inicial";
            if (type == PhotoType.Form) {
                nameType = "Documento";

            } else if (type == PhotoType.PostWork) {
                nameType = "Final";
            }

            // Create an image file name
            //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = JPEG_FILE_PREFIX + id + "_" + nameType + "_" + number + "_" + total;
            File albumF = HelperImage.getAlbumDir();
            File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
            return imageF;

    }

    public static String fileNameToSend(PhotoType type, String id, String number, String total)
    {
        String nameType = "Inicial";
        if (type == PhotoType.Form) {
            nameType = "Documento";

        } else if (type == PhotoType.PostWork) {
            nameType = "Final";
        }

        return  JPEG_FILE_PREFIX + id + "_" + nameType + "_" + number + "_TOTAL_" + total + JPEG_FILE_SUFFIX;

    }

    private static Matrix getRotationMatrixDegreesFromImage(String path) throws IOException
    {
        ExifInterface exif = new ExifInterface(path);
        int rotationInDegrees = 0;
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { rotationInDegrees = 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  rotationInDegrees = 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  rotationInDegrees = 270; }
        Matrix matrix = new Matrix();
        if (exifOrientation != 0f) {matrix.preRotate(rotationInDegrees);}
        return matrix;

    }
    public static Bitmap decodeFile(String path,int req_Height,int req_Width){
        try {

            // first the rotation of the original image;

            Matrix matrix = getRotationMatrixDegreesFromImage(path);

            //decode image size
            BitmapFactory.Options o1 = new BitmapFactory.Options();
            o1.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o1);


            //Find the correct scale value. It should be the power of 2.
            int width_tmp = o1.outWidth;
            int height_tmp = o1.outHeight;
            int scale = 1;

            if(width_tmp > req_Width || height_tmp > req_Height)
            {
                int heightRatio = Math.round((float) height_tmp / (float) req_Height);
                int widthRatio = Math.round((float) width_tmp / (float) req_Width);


                scale = heightRatio < widthRatio ? heightRatio : widthRatio;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inScaled = false;
            Bitmap scaledBitmap = BitmapFactory.decodeFile(path, o2);
            return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
