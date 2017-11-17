package mchehab.com.permissionsdemo

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener { e ->
            requestPermissionsWithPermissionCheck()
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS)
    fun requestPermissions() {
        //do whatever you want here
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS)
    fun permissionsDenied() {
        AlertDialog.Builder(this)
                .setTitle("Permission denied")
                .setMessage("Enable permissions")
                .setPositiveButton("Allow") { _, _ -> requestPermissionsWithPermissionCheck() }
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .create()
                .show()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS)
    fun onNeverAskAgain() {
        AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Message")
                .setPositiveButton("Ok") { dialog, which ->
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }.create()
                .show()
    }
}