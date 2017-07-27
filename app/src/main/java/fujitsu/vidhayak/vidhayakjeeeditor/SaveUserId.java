package fujitsu.vidhayak.vidhayakjeeeditor;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Fujitsu on 14/06/2017.
 */

public class SaveUserId {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    private static SaveUserId mInstance;
    private static Context mCtx;

    private SaveUserId(Context context) {
        mCtx = context;
    }

    public static synchronized SaveUserId getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SaveUserId(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveuserId(int id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TAG_TOKEN, id);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public int getUserId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getInt(TAG_TOKEN, 0);
    }

}
