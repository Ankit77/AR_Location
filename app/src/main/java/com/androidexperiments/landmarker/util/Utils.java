package com.androidexperiments.landmarker.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.transition.ChangeImageTransform;
import android.transition.Fade;
import android.widget.EditText;
import android.widget.TextView;

import com.google.creativelabs.androidexperiments.typecompass.R;

import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    /**
     * Method is used for checking network availability.
     *
     * @param context context
     * @return isNetAvailable: boolean true for Internet availability, false otherwise
     */

    public static boolean isNetworkAvailable(Context context) {

        boolean isNetAvailable = false;
        if (context != null) {
            final ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (mConnectivityManager != null) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    final Network[] allNetworks = mConnectivityManager.getAllNetworks();

                    for (Network network : allNetworks) {
                        final NetworkInfo networkInfo = mConnectivityManager.getNetworkInfo(network);
                        if (networkInfo != null && networkInfo.isConnected()) {
                            isNetAvailable = true;
                            break;
                        }
                    }

                } else {
                    boolean wifiNetworkConnected = false;
                    boolean mobileNetworkConnected = false;

                    final NetworkInfo mobileInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    final NetworkInfo wifiInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (mobileInfo != null) {
                        mobileNetworkConnected = mobileInfo.isConnected();
                    }
                    if (wifiInfo != null) {
                        wifiNetworkConnected = wifiInfo.isConnected();
                    }
                    isNetAvailable = (mobileNetworkConnected || wifiNetworkConnected);
                }
            }
        }
        return isNetAvailable;
    }

    /**
     * Display progress dialog with loading
     *
     * @param context context
     * @return dialog
     */
    public static ProgressDialog displayProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading......");
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    /**
     * Dismiss current progress dialog
     *
     * @param dialog dialog
     */
    public static void dismissProgressDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    /**
     * Alert dialog to show common messages.
     *
     * @param title   title
     * @param message message
     * @param context context
     */
    public static void displayDialog(final Context context, final String title, final String message) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        if (title == null)
            alertDialog.setTitle(context.getString(R.string.app_name));
        else
            alertDialog.setTitle(title);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        if (!((Activity) context).isFinishing()) {

            alertDialog.show();
        }
    }


    /**
     * Validate emailid with regular expression
     *
     * @param emailId email
     * @return true valid emailid, false invalid emailid
     */
    public static boolean isValidEmailId(final String emailId) {
        return !TextUtils.isEmpty(emailId) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
    }

    /**
     * Validate edit field for blank value
     *
     * @param editText edit field
     * @return true if contains empty value
     */
    public static boolean isEmptyField(final EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString().trim());
    }


    /**
     * Displays alert dialog with given message and butttons
     *
     * @param context
     * @param title
     * @param msg
     * @param strPositiveText
     * @param strNegativeText
     * @param isNagativeBtn
     * @param isFinish
     */
    public static void displayDialog(final Activity context, final String title, final String msg, final String strPositiveText, final String strNegativeText,
                                     final boolean isNagativeBtn, final boolean isFinish) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setCancelable(false);
        dialog.setMessage(msg);
        dialog.setPositiveButton(strPositiveText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (isFinish) {
                    context.finish();
                }
            }
        });
        if (isNagativeBtn) {
            dialog.setNegativeButton(strNegativeText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }


    private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    /**
     * Common add fragment method
     *
     * @param containerid
     * @param mActivity
     * @param targetedFragment
     * @param shooterFragment
     * @param isDownToUp
     */


    /**
     * Common add fragment method
     *
     * @param containerid
     * @param mActivity
     * @param targetedFragment
     * @param shooterFragment
     */

    public static void addNextFragmentNoAnim(int containerid, Activity mActivity, Fragment targetedFragment, Fragment shooterFragment) {

        final FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();
        transaction.add(containerid, targetedFragment, targetedFragment.getClass().getSimpleName());
        //curFragment = targetedFragment;
        transaction.hide(shooterFragment);
        transaction.addToBackStack(targetedFragment.getClass().getSimpleName());
        transaction.commit();
    }


    /**
     * Common replace fragent method
     *
     * @param containerid
     * @param mActivity
     * @param targetedFragment
     */
    public static void replaceNextFragment(int containerid, Activity mActivity, Fragment targetedFragment) {
        final FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();
        transaction.replace(containerid, targetedFragment, targetedFragment.getClass().getSimpleName());
        //curFragment = targetedFragment;
        transaction.commit();
    }


    /**
     * Add fragment with lolipop trasncation
     *
     * @param containerid
     * @param mActivity
     * @param targetedFragment
     * @param shooterFragment
     */
    public static void addNextFragment(int containerid, Activity mActivity, Fragment targetedFragment, Fragment shooterFragment, String textTransitionName, TextView textView) {
        final FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            targetedFragment.setSharedElementEnterTransition(new ChangeImageTransform());
            targetedFragment.setEnterTransition(new Fade());
            targetedFragment.setExitTransition(new Fade());
            targetedFragment.setSharedElementReturnTransition(new ChangeImageTransform());
            transaction.addSharedElement(textView, textTransitionName);
            transaction.add(containerid, targetedFragment, targetedFragment.getClass().getSimpleName());
            transaction.hide(shooterFragment);
            transaction.addToBackStack(targetedFragment.getClass().getSimpleName());
            transaction.commit();
        }
    }


    /**
     * Get device name + UDID
     *
     * @param context
     * @return
     */
    public static String getDeviceName(Context context) {
        String name = "";
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            name = capitalize(model);
        } else {
            name = capitalize(manufacturer) + " " + model;
        }
        name += android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        return name;
    }

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }

    /**
     * Capitalizes string
     *
     * @param s
     * @return
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    public static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    /**
     * Emits a sample share {@link Intent}.
     */
    public static void share(Context context, String message) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(sharingIntent, "Ttile"));
    }

    public static Intent getFileChooserIntent(String contentType) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        final Intent openGalleryIntent;
        if (isKitKat) {
            openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            openGalleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
            openGalleryIntent.setType(contentType);
            openGalleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        } else {
            openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            openGalleryIntent.setType(contentType);
            openGalleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        }
        return openGalleryIntent;
    }

    public static void deleteFiles(File file) {
        if (file.isDirectory())
            for (File f : file.listFiles())
                deleteFiles(f);
        else
            file.delete();
    }

//    public static Menu newMenuInstance(Context context) {
//        try {
//            Class<?> menuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder");
//            Constructor<?> constructor = menuBuilderClass.getDeclaredConstructor(Context.class);
//            return (Menu) constructor.newInstance(context);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }


    public static boolean isNotNullOrEmpty(String data) {
        return !TextUtils.isEmpty(data) && !data.equalsIgnoreCase("null");
    }

    public static String isCheckNullOrEmpty(JSONObject contentObject, String arg) {
        return contentObject.isNull(arg) ? "" : contentObject.optString(arg);
    }


    @NonNull
    public static String getBasePath(Context context) {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/" + context.getString(R.string.app_name));
        return dir.getPath();
    }



//    public static synchronized void setKeywordListHighlighted(Context context, TextView view, String fulltext, List<String> subtextList) {
//        view.setText(fulltext, TextView.BufferType.SPANNABLE);
//        if (subtextList != null && subtextList.size() > 0) {
//            for (String subtext : subtextList) {
//                addHighlightedText(context, view, fulltext, subtext);
//            }
//        }
//    }


}
