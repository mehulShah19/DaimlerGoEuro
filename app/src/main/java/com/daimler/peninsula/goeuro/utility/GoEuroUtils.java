package com.daimler.peninsula.goeuro.utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.daimler.peninsula.goeuro.R;
import com.daimler.peninsula.goeuro.bean.LocationInformation;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Mehul on 13/09/16.
 */
public class GoEuroUtils {
    private static int singleDayInMilleseconds = 24 * 60 * 60 * 1000;

    /**
     * Get Locale Language of the user
     * By default it's kept to de
     * @param context
     * @return
     */
    public static String getLocale(Context context){
        String result = context.getString(R.string.defaultLanguage);
        Locale locale = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = context.getResources().getConfiguration().getLocales();
            if (localeList == null || localeList.size() == 0) {
                return result;
            }
            Log.e("locale", localeList.get(0)+"");
            locale = localeList.get(0);
         }else{
            locale = context.getResources().getConfiguration().locale;
        }
        if(locale != null && !TextUtils.isEmpty(locale.getLanguage())){
            return(locale.getLanguage());
        }
        return result;
    }

    /**
     * Converts the calendar to the String in the format day. month. year
     * @param calendar
     * @return
     */
    public static String convertCalendarToString(Calendar calendar){
        if(calendar == null){
            return "";
        }
        Calendar currentDate =  calendar;
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        return (day + "." + (month + 1) + "." + year);
    }

    /*
        This method is used to check whether the date selected is not the old date
     */
    public static boolean isDateValid(Calendar calendar){
            if(calendar == null){
                return false;
            }
            Calendar currentDate =  Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);

            currentDate.set(year,month,day - 1,0,0, 0);

            Calendar checkingCalendar = calendar ;
            year = checkingCalendar.get(Calendar.YEAR);
            month = checkingCalendar.get(Calendar.MONTH);
            day = checkingCalendar.get(Calendar.DAY_OF_MONTH);
            checkingCalendar.set(year, month, day, 0, 0,0);

            if(checkingCalendar.getTimeInMillis() - currentDate.getTimeInMillis() >=  singleDayInMilleseconds){
                return true;
            }else{
                return false;
            }
    }

    /**
     * This method is used to display name in the format LocationName (Country Name)
     * @param locationInformation
     * @return
     */
    public static String getDisplayName(LocationInformation locationInformation){
        String result = "";
        if(locationInformation == null) {return result;}

        if(!TextUtils.isEmpty(locationInformation.getName()))
        result = locationInformation.getName();
        if(!TextUtils.isEmpty(locationInformation.getCountry()))
        result += "  (" + locationInformation.getCountry() + ")";
        return result;
    }

    /**
     * Checks Internet Connectivity
     * @param context
     * @return
     */
    public static boolean isInternetAvailable(Context context) {
        boolean flag;
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        flag = netInfo != null && netInfo.isConnectedOrConnecting();
        return flag;
    }

    /**
     * Dismisses the Virtual Keyboard if present in the screen
     * @param activity
     */
    public static void dismissVirtualKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
