package com.daimler.peninsula.goeuro.marshmellowPermission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Mehul on 14/09/16.
 */
public class PermissionUtil {

    /*
    This method can be used to check the Permission
    PermissionResultInterface is created to delegate to the called class
    If permission is granted then OnGranted will be called
    If permission is denied then OnDenied will be called
    If permission is denied before and again asked then showRationale will be called
     */

    public void checkPermission(Activity context, PermissionResultInterface permissionResultInterface) {
        if (Build.VERSION.SDK_INT < 23) {
            permissionResultInterface.OnPermissionGranted();
        }
        String[] permissions = PermissionConstant.Permission_Location;
        boolean isPermissionRejected = false;
        int loopingInt = 0;
        for (int i = 0; i < permissions.length; i++) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, permissions[i]);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                isPermissionRejected = true;
                loopingInt = i;
                break;
            }
        }
        if (!isPermissionRejected) {
            permissionResultInterface.OnPermissionGranted();
            return;
        }
        if (!(context instanceof Activity)) {
            permissionResultInterface.OnPermissionDenied(permissions);
        }
        Activity activity = (Activity) context;
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[loopingInt])) {
            permissionResultInterface.OnPermissionRationale(permissions);
        } else {
            permissionResultInterface.OnPermissionDenied(permissions);
        }
    }
}