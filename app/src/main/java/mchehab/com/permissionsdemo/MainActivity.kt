package mchehab.com.permissionsdemo

import android.Manifest
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.widget.Button
import com.tbruyelle.rxpermissions2.RxPermissions

class MainActivity : AppCompatActivity() {

    val permissions =  arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest
            .permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS)

    val rxPermissions by lazy {
        RxPermissions(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener { e ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermissions(){
        rxPermissions.request(*permissions)
                .subscribe { granted ->
                    if(granted){
//                        all permissions granted
                    }else{
                        if(permissions.any { shouldShowRequestPermissionRationale(it) }){
                            AlertDialog.Builder(this)
                                    .setTitle("Eable Permissions")
                                    .setMessage("Please enable permissions")
                                    .setPositiveButton("Allow", {dialog, which -> requestPermissions()})
                                    .setNegativeButton("Cancel", {dialog, which -> dialog.dismiss()})
                                    .create()
                                    .show()
                        }
                    }
                }
    }
}