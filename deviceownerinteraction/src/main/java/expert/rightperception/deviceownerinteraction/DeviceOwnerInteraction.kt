package expert.rightperception.deviceownerinteraction

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import expert.rightperception.deviceownerinteraction.DeviceOwnerInteraction.DO_PACKAGE_NAME

internal object DeviceOwnerInteraction {

    const val DO_PACKAGE_NAME = "expert.rightperception.deviceowner"
    const val SCHEME = "owner"
}

object Kiosk {

    private const val HOST = "kiosk"

    private const val KEY_LOCK_TASK_PACKAGE_NAME = "lockTaskPackageName"
    private const val KEY_LOCK_TASK_ENABLED = "lockTaskEnabled"

    @SuppressLint("QueryPermissionsNeeded")
    fun Activity.startKiosk() {
        val uri = Uri.Builder()
            .scheme(DeviceOwnerInteraction.SCHEME)
            .authority(HOST)
            .appendQueryParameter(KEY_LOCK_TASK_PACKAGE_NAME, packageName)
            .appendQueryParameter(KEY_LOCK_TASK_ENABLED, true.toString())
            .build()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val activities = packageManager.queryIntentActivities(intent, 0)
        if (activities.size > 0) {
            startActivity(intent)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun Activity.stopKiosk() {
        stopLockTask()
        val uri = Uri.Builder()
            .scheme(DeviceOwnerInteraction.SCHEME)
            .authority(HOST)
            .appendQueryParameter(KEY_LOCK_TASK_PACKAGE_NAME, packageName)
            .appendQueryParameter(KEY_LOCK_TASK_ENABLED, false.toString())
            .build()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val activities = packageManager.queryIntentActivities(intent, 0)
        if (activities.size > 0) {
            startActivity(intent)
        }
    }

    fun Context.isKioskActive(): Boolean {
        val activityManager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For SDK version 23 and above.
            return (activityManager.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE)
        }
        return activityManager.isInLockTaskMode
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun Context.isPackageExisted(): Boolean {
        val packages: List<ApplicationInfo>
        val pm: PackageManager = packageManager
        packages = pm.getInstalledApplications(0)
        for (packageInfo in packages) {
            if (packageInfo.packageName.equals(DO_PACKAGE_NAME)) return true
        }
        return false
    }
}
