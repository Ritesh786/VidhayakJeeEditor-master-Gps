package fujitsu.vidhayak.vidhayakjeeeditor.FCM;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Fujitsu on 14/06/2017.
 */

public class tokensave {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    private static tokensave mInstance;
    private static Context mCtx;

    private tokensave(Context context) {
        mCtx = context;
    }

    public static synchronized tokensave getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new tokensave(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, "");
    }

}
