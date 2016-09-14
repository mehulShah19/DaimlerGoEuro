package com.daimler.peninsula.goeuro.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimler.peninsula.goeuro.R;
import com.daimler.peninsula.goeuro.adapter.AutoCompleteAdapter;
import com.daimler.peninsula.goeuro.bean.LocationInformation;
import com.daimler.peninsula.goeuro.customView.DelayAutoCompleteTextView;
import com.daimler.peninsula.goeuro.fragment.AlertDialogFragment;
import com.daimler.peninsula.goeuro.fragment.DatePickerFragment;
import com.daimler.peninsula.goeuro.marshmellowPermission.PermissionResultInterface;
import com.daimler.peninsula.goeuro.marshmellowPermission.PermissionUtil;
import com.daimler.peninsula.goeuro.utility.GoEuroUtils;
import com.daimler.peninsula.goeuro.utility.SharePreferenceUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

public class GoEuroSearchActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoEuroSearchActivity";
    private static final String FRAGMENT_DATE_PICKER = "FRAGMENT_DATE_PICKER";
    private static final String FRAGMENT_ALERT_FRAGMENT = "FRAGMENT_ALERT_FRAGMENT";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    //Views
    private DelayAutoCompleteTextView startLocationAutoCompleteTV;
    private ProgressBar startLocationPB;
    private DelayAutoCompleteTextView endLocationAutoCompleteTV;
    private ProgressBar endLocationPB;
    private TextView dateOfJourneyTV;
    private TextView searchTV;
    private AutoCompleteAdapter startLocationAdapter, endLocationAdapter;
    private Calendar dateOfJourneyCalendar;
    private Context context;

    protected GoogleApiClient mGoogleApiClient;


    //Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_euro_search);
        context = getApplicationContext();
        buildGoogleApiClient();
        initializeViews();
        dateOfJourneyCalendar = Calendar.getInstance();
        setDateToView(dateOfJourneyCalendar);
    }

    // Attached date to the View in dd.mm.yyyy format
    public void setDateToView(Calendar calendar) {
        if (calendar == null) {
            return;
        }
        dateOfJourneyCalendar = calendar;
        dateOfJourneyTV.setText(GoEuroUtils.convertCalendarToString(calendar));
    }

    /**
     * Initializes the Views, adapters and intiate the clickListeners
     */
    private void initializeViews() {
        startLocationAutoCompleteTV = (DelayAutoCompleteTextView) findViewById(R.id.startLocationAutoCompleteTV);
        startLocationPB = (ProgressBar) findViewById(R.id.startLocationPB);
        startLocationAutoCompleteTV.setLoadingIndicator(startLocationPB);
        startLocationAutoCompleteTV.setThreshold(0);
        startLocationAdapter = new AutoCompleteAdapter(this, startLocationAutoCompleteTV);
        startLocationAutoCompleteTV.setAdapter(startLocationAdapter);
        startLocationAutoCompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startLocationAutoCompleteTV.setText(getDisplayName(adapterView, i), false);
                startLocationAutoCompleteTV.setSelection(startLocationAutoCompleteTV.getText().toString().length());
            }
        });


        endLocationAutoCompleteTV = (DelayAutoCompleteTextView) findViewById(R.id.endLocationAutoCompleteTV);
        endLocationPB = (ProgressBar) findViewById(R.id.endLocationPB);
        endLocationAutoCompleteTV.setLoadingIndicator(endLocationPB);
        endLocationAutoCompleteTV.setThreshold(0);
        endLocationAdapter = new AutoCompleteAdapter(this, endLocationAutoCompleteTV);
        endLocationAutoCompleteTV.setAdapter(endLocationAdapter);
        endLocationAutoCompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                endLocationAutoCompleteTV.setText(getDisplayName(adapterView, i), false);
                endLocationAutoCompleteTV.setSelection(endLocationAutoCompleteTV.getText().toString().length());
            }
        });

        dateOfJourneyTV = (TextView) findViewById(R.id.dateOfJourneyTV);
        searchTV = (TextView) findViewById(R.id.searchTV);
        dateOfJourneyTV.setOnClickListener(this);
        searchTV.setOnClickListener(this);
    }

    /**
     * This method converts the LocationInformation Name to LocationInformation Name + (Country Name)
     * @param adapterView
     * @param i
     * @return
     */
    private String getDisplayName(AdapterView<?> adapterView, int i) {
        String displayName = "";
        Object locationInformationObject = adapterView.getItemAtPosition(i);
        if (locationInformationObject != null) {
            LocationInformation locationInformation = (LocationInformation) locationInformationObject;
            displayName = GoEuroUtils.getDisplayName(locationInformation);
        }
        return displayName;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dateOfJourneyTV:
                GoEuroUtils.dismissVirtualKeyboard(this);
                DialogFragment newFragment = DatePickerFragment.getInstance(dateOfJourneyCalendar);
                newFragment.show(getSupportFragmentManager(), FRAGMENT_DATE_PICKER);
                break;
            case R.id.searchTV:
                if (TextUtils.isEmpty(startLocationAutoCompleteTV.getText().toString().trim()) || TextUtils.isEmpty(endLocationAutoCompleteTV.getText().toString().trim())) {
                    Toast.makeText(GoEuroSearchActivity.this, getString(R.string.allFieldsAreMandatory), Toast.LENGTH_LONG).show();
                    return;
                }
                AlertDialogFragment alertDialogFragment = AlertDialogFragment.getInstance(getString(R.string.searchIsNotYetImplemented));
                alertDialogFragment.show(getSupportFragmentManager(), FRAGMENT_ALERT_FRAGMENT);
                break;
        }
    }
    //Google Location API

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    // PermissionResultInterface methods are called depending on the status of the Location permission for Marshmellow and higher devices
    PermissionResultInterface phonePermissionResultInterface = new PermissionResultInterface() {
        @Override
        public void OnPermissionGranted() {
            onLocationAccessGranted();
        }

        @Override
        public void OnPermissionDenied(String[] permission) {
             ActivityCompat.requestPermissions(GoEuroSearchActivity.this, permission, LOCATION_PERMISSION_REQUEST_CODE);

        }

        @Override
        public void OnPermissionRationale(String[] permission) {
            ActivityCompat.requestPermissions(GoEuroSearchActivity.this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onLocationAccessGranted();
            }
        }
    }

    /**
     * When the access to the location permssion is granted this method is called
     * Internally it rechecks whether permission is given or not
     * THis permission is introduced from Marshmellow API 23
     */
    private void onLocationAccessGranted() {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                return;
            }
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null) {
                saveGeoLocationToSharedPreference(lastLocation.getLatitude(), lastLocation.getLongitude());
            }
        }catch (Exception e){
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //Connecting with te Google API Client
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            //Disconnecting with the Google Client
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null) {
                saveGeoLocationToSharedPreference(lastLocation.getLatitude(), lastLocation.getLongitude());
            }
        }else {
            PermissionUtil permissionUtil = new PermissionUtil();
            permissionUtil.checkPermission(this, phonePermissionResultInterface);
        }
    }

    /**
     * Calles when the Google API Client is unable to connect
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        mGoogleApiClient.connect();
    }
    private  void saveGeoLocationToSharedPreference(double latitude, double longitude) {
        SharePreferenceUtil.saveDoubleToSharePreference(context, getString(R.string.pref_latitude) , latitude);
        SharePreferenceUtil.saveDoubleToSharePreference(context, getString(R.string.pref_longitude), longitude);
    }
}