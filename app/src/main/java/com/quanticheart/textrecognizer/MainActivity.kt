package com.quanticheart.textrecognizer

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.quanticheart.recognizer.RecognizerConstants.recognizerCode
import com.quanticheart.recognizer.startRecognizer
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.*

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnShowPermission.setOnClickListener { showRecognizerWithPermissionCheck() }
        openRecognizer.setOnClickListener { showRecognizerWithPermissionCheck() }
        showRecognizerWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun showRecognizer() {
        startRecognizer()
        showRecognizerLayout()
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForRecognizer(request: PermissionRequest) {
        showDialog()
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun onRecognizerDenied() {
        showDialog()
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun onRecognizerNeverAskAgain() {
        showDialog()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun showDialog() {
        showPermissionLayout()
        AlertDialog.Builder(this)
            .setTitle("Title")
            .setMessage("Message")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == recognizerCode) {
            if (resultCode == Activity.RESULT_OK) {
                val returnedResult: String = data?.data.toString()
                response.text = returnedResult
            }
        }
    }

    private fun showPermissionLayout() {
        flipper.displayedChild = 0
    }

    private fun showRecognizerLayout() {
        flipper.displayedChild = 1
    }
}
