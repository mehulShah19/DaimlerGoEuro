package com.daimler.peninsula.goeuro.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import com.daimler.peninsula.goeuro.activity.GoEuroSearchActivity;
import com.daimler.peninsula.goeuro.utility.GoEuroUtils;

import java.util.Calendar;

/**
 * Created by Mehul on 12/09/16.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String ARG_CALENDAR = "ARG_CALENDAR";
    public DatePickerFragment(){
    }

    Calendar initCalendar;

    /**
     * It creates the instance of the Diloag Fragment
     * Parameter calendar will be the initial date which will be shown when the date picker is displayed
     * @param calendar
     * @return
     */

    public static DatePickerFragment getInstance(Calendar calendar){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CALENDAR, calendar);
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            initCalendar = (Calendar) getArguments().getSerializable(ARG_CALENDAR);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = initCalendar;
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    /**
     * When a date is selected by the user this method is called
     * @param view
     * @param year
     * @param month
     * @param day
     */

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        if(getActivity() instanceof GoEuroSearchActivity){
            if(GoEuroUtils.isDateValid(calendar)) {
                ((GoEuroSearchActivity) getActivity()).setDateToView(calendar);
            }else{
                Toast.makeText(getContext(),"Please select future date", Toast.LENGTH_LONG).show();
            }
        }
        dismiss();
    }
}
