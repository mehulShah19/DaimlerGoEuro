package com.daimler.peninsula.goeuro.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mehul on 14/09/16.
 * This class is used to get to do operations related to SharePreference Class
 *
 */
public class SharePreferenceUtil {

        private static final String MyPREFERENCES = "NEWS_Preferences";

    /**
     * Saves the double value to the respective Parameter
     * @param context
     * @param key
     * @param addingParameter
     */
        public static void saveDoubleToSharePreference(Context context, String key, Double addingParameter){
            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, String.valueOf(addingParameter));
            editor.commit();
        }

    /**
     * Fetches the double value in accordance to key
     * @param context
     * @param key
     * @return
     */
        public static Double getDoubleFromSharePreference(Context context,String key){
            SharedPreferences  sharedPreferences = context.getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            return Double.parseDouble(sharedPreferences.getString(key, "0"));
        }
}
