package com.daimler.peninsula.goeuro.marshmellowPermission;

import android.Manifest;

/**
 * Created by Mehul on 14/09/16.
 */
//PermissionResultIntrerface
public interface PermissionResultInterface {
    public void OnPermissionGranted();
    public void OnPermissionDenied(String[] permission);
    public void OnPermissionRationale(String[] permission);
}

interface PermissionConstant {

    String[] Permission_Location = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
}


