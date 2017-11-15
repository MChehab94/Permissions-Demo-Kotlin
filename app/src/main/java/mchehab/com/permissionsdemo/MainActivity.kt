package mchehab.com.permissionsdemo

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener { e ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (arePermissionsEnabled()) {
                    //                    permissions granted, continue flow normally
                } else {
                    requestMultiplePermissions()
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun arePermissionsEnabled(): Boolean {
        return permissions.none { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED};
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestMultiplePermissions() {
        val remainingPermissions = permissions.filter { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
        requestPermissions(remainingPermissions.toTypedArray(), 101)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        AlertDialog.Builder(this)
                                .setMessage("Your error message here")
                                .setPositiveButton("Allow") { dialog, which -> requestMultiplePermissions() }
                                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                                .create()
                                .show()
                    }
                    return
                }
            }
            //all is good, continue flow
        }
    }
}