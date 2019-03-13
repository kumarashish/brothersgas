package utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import common.Common;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ashish.kumar on 23-01-2019.
 */

public class Utils {

    public static boolean isUserLoggedIn(String value) {
        try {
            JSONObject jsonObject = new JSONObject(value);
            JSONObject result = jsonObject.getJSONObject("RESULT");
            JSONArray groups = result.getJSONArray("GRP");
            JSONObject statusJSon = groups.getJSONObject(1);
            JSONObject statusValueJSON = statusJSon.getJSONObject("FLD");
            if (statusValueJSON.getInt("content") == 2) {
                return true;
            }

        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        return false;
    }



public static String getBase64(String path)
{

    // File imageFile = new File(imageUrl);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 8;
    Bitmap myBitmap = BitmapFactory.decodeFile(path,options);
    ByteArrayOutputStream baos=new ByteArrayOutputStream();
    myBitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
    byte [] b=baos.toByteArray();
    String temp= Base64.encodeToString(b, Base64.DEFAULT);
    System.out.print(temp);
//        byte[] byteArray = getImageByteArray(imageFile);
//        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
    return temp;

}
    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }

    public static void showAlert(final Activity act, String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder( act);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        act.finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public static void showAlertForReturnIntent(final Activity act, String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder( act);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent data = new Intent();
                        act.setResult(RESULT_OK,data);

                        act.finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public static void showAlertNavigateToPrintEmail(final Activity act, String message,final Class b)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder( act);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      act.startActivity(new Intent(act, b));
                        act.finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public static void showAlertNavigateToInvoices(final Activity act, String message, final Class b, final String data)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder( act);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent in=new Intent(act,b);
                        in.putExtra("Data",data);
                        act.startActivity(in);
                        act.finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public static void showAlertNormal(final Activity act, String message)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder( act);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
public static String getDate(String date)
{
    //String newDate=date.substring(6,8)+"-"+date.substring(4,6)+"-"+date.substring(0,4);
    return date;
}
    public static String getFormatted(String date)
    {date=date.replaceAll("/","");
        String newDate=date.substring(4,8)+""+date.substring(2,4)+""+date.substring(0,2);
        return newDate;
    }
    public static boolean isValidEmailId(String email){

        return Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$").matcher(email).matches();
    }

    public static void makeFolder(String path, String folder) {
        File directory = new File(path, folder);
        if (directory.exists() == false) {
            directory.mkdirs();
        }
    }


    /* * camera module popup
     *************************************/
    public static void selectImageDialog(final Activity act,String heading) {
        final CharSequence[] items = {"Take Photo", "Choose from gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(act);
        builder.setTitle(heading);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    if (isDeviceSupportCamera(act)) {
                        captureImage(act);
                    } else {
                        Toast.makeText(act, "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                    }
                } else if (items[item].equals("Choose from gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    act.startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            Common.SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Checking device has camera hardware or not
     */
    private static boolean isDeviceSupportCamera(Activity act) {
        if (act.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static void captureImage(Activity act) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Common.imageUri = getOutputMediaFileUri(Common.MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Common.imageUri);

        // start the image capture Intent
        try {
            act.startActivityForResult(intent, Common.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

    public static Uri getOutputMediaFileUri(int type) {
        File tempFile = getOutputMediaFile(type);
        Uri uri = Uri.fromFile(tempFile);
        return uri;
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Common.sdCardPath);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == Common.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        File files = mediaFile;
        return mediaFile;
    }

}
