package com.daimler.peninsula.goeuro.adapter;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;

import com.daimler.peninsula.goeuro.R;
import com.daimler.peninsula.goeuro.bean.LocationInformation;
import com.daimler.peninsula.goeuro.connection.ConnectionRequestResponse;
import com.daimler.peninsula.goeuro.utility.GoEuroUtils;
import com.daimler.peninsula.goeuro.utility.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Mehul on 12/09/16.
 * This Adapter is used by AutoCompleteTextView to show the list in the dropdown manner
 *
 */
public class AutoCompleteAdapter extends ArrayAdapter<LocationInformation> {

    Context context;
    ArrayList<LocationInformation> locationInformationArrayList;
    ArrayFilter arrayFilter = new ArrayFilter();
    ConnectionRequestResponse connectionRequestResponse;
    AutoCompleteTextView autoCompleteTextView;

   public AutoCompleteAdapter(Context context, AutoCompleteTextView autoCompleteTextView) {
       super(context,R.layout.adapter_auto_complete_single_row);
       this.context = context;
       this.connectionRequestResponse = new ConnectionRequestResponse();
       this.autoCompleteTextView  = autoCompleteTextView;
   }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.adapter_auto_complete_single_row, null);
        }
        TextView customerNameLabel = (TextView) v.findViewById(R.id.singleRowTV);
        customerNameLabel.setText(GoEuroUtils.getDisplayName(locationInformationArrayList.get(position)));

        return v;
    }

    @Override
    public Filter getFilter() {
        return arrayFilter;
    }

    @Override
    public int getCount() {
        if(locationInformationArrayList != null){
            return  locationInformationArrayList.size();
        }
        return 0;
    }

    @Override
    public LocationInformation getItem(int position) {
        if(locationInformationArrayList !=null){
            return locationInformationArrayList.get(position);
        }
      return  null;
    }

    /**
     * This class is used to perform Filtering and sorting to the data
     */
    public class ArrayFilter extends Filter{

        /**
         * This method performs the filtering process
         * It is called when user types any character
         * @param charSequence
         * @return
         */
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            if(charSequence == null){
                return null;
            }
            ArrayList<LocationInformation> receivedLocationInformationListFromAPI = connectionRequestResponse.callLocationAPIService(charSequence.toString().toLowerCase().trim(), context);
            if(receivedLocationInformationListFromAPI == null){
                receivedLocationInformationListFromAPI = new ArrayList<LocationInformation>();
            }

            ArrayList<LocationInformation> filteredLocationInformationArrayList = new ArrayList<LocationInformation>();
            for(LocationInformation locationInformation : receivedLocationInformationListFromAPI){
                if(!TextUtils.isEmpty(autoCompleteTextView.getText().toString()) && locationInformation.getName().trim().toLowerCase().startsWith(autoCompleteTextView.getText().toString().toLowerCase())){
                    filteredLocationInformationArrayList.add(locationInformation);
                }
            }
            filterResults.values = filteredLocationInformationArrayList;
            filterResults.count = filteredLocationInformationArrayList.size();
            return filterResults;
        }


        /**
         * THis method will redisplay the list
         * We are perfomring sorting operation on the basis of Distance from the current location
         * @param charSequence
         * @param filterResults
         */
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            final Double savedLatitide = SharePreferenceUtil.getDoubleFromSharePreference(context, context.getString(R.string.pref_latitude));
            final Double savedLongitude = SharePreferenceUtil.getDoubleFromSharePreference(context,context.getString(R.string.pref_longitude));
            final Location savedLocation = new Location("savedLocation");
            savedLocation.setLatitude(savedLatitide);
            savedLocation.setLongitude(savedLongitude);
            if (filterResults != null && filterResults.values != null) { //&& filterResults.count > 0 &&
                locationInformationArrayList = (ArrayList<LocationInformation>) filterResults.values;
                Collections.sort(locationInformationArrayList, new Comparator<LocationInformation>() {
                    @Override
                    public int compare(LocationInformation locationInformation, LocationInformation t1) {
                        if(locationInformation.getGeoPosition() == null || t1.getGeoPosition() == null){return 0;}

                        Location firstLocation = new Location("firstLocation");
                        firstLocation.setLatitude(locationInformation.getGeoPosition().getLatitude());
                        firstLocation.setLongitude(locationInformation.getGeoPosition().getLongitude());

                        Location secondLocation = new Location("secondLocation");
                        secondLocation.setLatitude(locationInformation.getGeoPosition().getLatitude());
                        secondLocation.setLongitude(locationInformation.getGeoPosition().getLongitude());
                        if(savedLocation.distanceTo(firstLocation)> savedLocation.distanceTo(secondLocation)) {
                            return 0;
                        }else{
                            return 1;
                        }
                    }
                });
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}


