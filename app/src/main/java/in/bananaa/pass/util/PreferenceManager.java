package in.bananaa.pass.util;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;

import in.bananaa.pass.dto.user.LoginResponse;
import in.bananaa.pass.dto.user.User;

public class PreferenceManager extends Application implements Application.ActivityLifecycleCallbacks {
    private static final String BANANAA = "Bananaa";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String LOCATION = "location";
    private static final String ACCESS_TOKEN = "accessToken";

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor prefEditor;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getApplicationContext().getSharedPreferences(BANANAA, MODE_PRIVATE);
        prefEditor = preferences.edit();
        prefEditor.commit();
    }

    public static void putLoginDetails(LoginResponse loginResponse) {
        if (loginResponse == null) {
            return;
        }
        prefEditor.putBoolean(IS_LOGGED_IN, true);
        prefEditor.putInt(ID, loginResponse.getUser().getId());
        prefEditor.putString(NAME, loginResponse.getUser().getName());
        prefEditor.putString(LOCATION, loginResponse.getUser().getLocation());
        prefEditor.putString(ACCESS_TOKEN, loginResponse.getAccessToken());
        prefEditor.commit();
    }

    public static Boolean isUserLoggedIn() {
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }

    public static String getAccessToken() {
        return preferences.getString(ACCESS_TOKEN, null);
    }

    public static User getUser() {
        User user = new User();
        user.setId(preferences.getInt(ID, 0));
        user.setName(preferences.getString(NAME, null));
        user.setLocation(preferences.getString(LOCATION, null));
        return user;
    }

    public static void resetUser() {
        prefEditor.putBoolean(IS_LOGGED_IN, false);
        prefEditor.putInt(ID, 0);
        prefEditor.putString(NAME, null);
        prefEditor.putString(LOCATION, null);
        prefEditor.putString(ACCESS_TOKEN, null);
        prefEditor.commit();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
