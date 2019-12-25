package com.example.testzxing

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity: AppCompatActivity(), ZXingScannerView.ResultHandler{
    private val REQUEST_CAMERA_PERMISSIONS = 1
    private lateinit var mScannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        mScannerView = ZXingScannerView(this)
    }

    override fun onResume(){
        super.onResume()
        requestPermissions()
    }

    override fun onPause() {
        super.onPause()
        stopScanner()
    }

    private fun startScanner(){
        setContentView(mScannerView)
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    private fun stopScanner(){
        mScannerView.stopCamera()
    }

    override fun handleResult(mResult: Result){
        verticalLayout{
            textView("FORMAT:\t" + mResult.barcodeFormat)
            textView("VALUE:\t" + mResult.text)
            textView("RAW BYTES:\t" + mResult.rawBytes)
            textView("METADATA:\t" + mResult.resultMetadata)
            textView("POINTS:\t" + mResult.resultPoints)
            textView("TIMESTAMP:\t" + mResult.timestamp)
            val name = editText("https://www.ean-search.org/?q=" + mResult.text)
            button("Browse"){
                onClick{
                    browse("${name.text}")
                }
            }
            button("Scan"){
                onClick{
                    startScanner()
                }
            }
        }
    }

    private fun requestPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS)
            }
        }else{
            startScanner()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray){
        when(requestCode){
            REQUEST_CAMERA_PERMISSIONS -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    longToast("Permission granted")
                }else{
                    longToast("This application needs camera permissions to work correctly!")
                }
                return
            }
            else -> {
            }
        }
    }
}