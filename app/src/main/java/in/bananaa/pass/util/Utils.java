package in.bananaa.pass.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import java.util.List;

import in.bananaa.pass.R;
import in.bananaa.pass.dto.GenericResponse;

/**
 * Created by akashnigam on 14/06/18.
 */

public class Utils {

    public static void setMenuItemsFont(Menu menu, Typeface font, Context mContext) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem, font);
                }
            }
            applyFontToMenuItem(mi, font);
        }
    }

    private static void applyFontToMenuItem(MenuItem mi, Typeface font) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public static Typeface getRegularFont(Context mContext) {
        return Typeface.createFromAsset(mContext.getAssets(), "bna-regular.ttf");
    }

    public static Typeface getBold(Context mContext) {
        return Typeface.createFromAsset(mContext.getAssets(), "bna-bold.ttf");
    }

    public static Typeface getSimpsonFont(Context mContext) {
        return Typeface.createFromAsset(mContext.getAssets(), "bna-simpson.ttf");
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean checkIfInternetConnectedAndToast(Activity activity) {
        if (!isInternetConnected(activity)) {
            Toast.makeText(activity, activity.getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public static void genericErrorToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void exceptionOccurred(Activity activity, Exception e) {
        Toast.makeText(activity, activity.getString(R.string.genericError), Toast.LENGTH_SHORT).show();
    }

    public static void responseError(Activity activity, GenericResponse response) {
        Toast.makeText(activity, activity.getString(R.string.genericError), Toast.LENGTH_SHORT).show();
    }

    public static void responseFailure(Activity activity) {
        Toast.makeText(activity, activity.getString(R.string.genericError), Toast.LENGTH_SHORT).show();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.toString().trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String parseListToCommaSeparatedString(List<String> list) {
        String finalStr = "";
        int i;
        for (i=0; i<list.size()-1; i++) {
            finalStr += list.get(i) + ", ";
        }

        finalStr += list.get(i);
        return finalStr;
    }

    public static void openContactUsApplication(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:contact@bananaa.in?subject=" + "Contact Bananaa" + "&body=");
        intent.setData(data);
        context.startActivity(intent);
    }

    public static void openPlayStoreForRating(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
}
