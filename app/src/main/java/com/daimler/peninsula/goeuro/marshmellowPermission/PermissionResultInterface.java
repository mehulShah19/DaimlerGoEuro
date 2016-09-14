package com.daimler.peninsula.goeuro.marshmellowPermission;

import android.Manifest;

/**
 * Created by Mehul on 14/09/16.
 */
//PermissionResultIntrerface to interact after knowing the permission status
public interface PermissionResultInterface {
    public void OnPermissionGranted();
    public void OnPermissionDenied(String[] permission);
    public void OnPermissionRationale(String[] permission);
}

interface PermissionConstant {
    //List of permission request which can be asked in the Permission Location
    String[] Permission_Location = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
}


