package expert.rightperception.deviceownerinteraction

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri

internal object DeviceOwnerInteraction {

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
}
