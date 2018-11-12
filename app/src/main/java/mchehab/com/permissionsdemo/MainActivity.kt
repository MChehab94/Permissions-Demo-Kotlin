package mchehab.com.permissionsdemo

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener { e ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    writeToExternalStorage()
                } else {
                    requestWriteExternalStoragePermission()
                }
            }
        }
    }

    private fun requestWriteExternalStoragePermission() {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 101)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                writeToExternalStorage()
            } else {
                if (shouldShowRequestPermissionRationale(permissions[0])) {
                    AlertDialog.Builder(this)
                            .setMessage("Your error message here")
                            .setPositiveButton("Allow") { dialog, which -> requestWriteExternalStoragePermission() }
                            .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                            .create()
                            .show()
                }else{
                    AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage("Please grant permission in the apps settings")
                            .setPositiveButton("Go to settings") { dialog, which ->
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }
                            .setNegativeButton("Cancel") { dialog, _-> dialog.dismiss() }
                            .setCancelable(false)
                            .create()
                            .show()
                }
            }
        }
    }

    private fun writeToExternalStorage() {
        val root = Environment.getExternalStorageDirectory()
        val directory = File(root.absolutePath + "/pathThatYouWant")
        if (!directory.exists())
            directory.mkdirs()
        val file = File(directory, "fileName.txt")
        try {
            val writer = BufferedWriter(FileWriter(file))
            writer.write("this is my text here")
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
    }
}