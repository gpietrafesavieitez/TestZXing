package com.example.testzxing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity: AppCompatActivity(), ZXingScannerView.ResultHandler{
    private lateinit var mScannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        mScannerView = ZXingScannerView(this)
    }

    override fun onResume(){
        super.onResume()
        startScanner()
    }

    override fun onPause(){
        super.onPause()
        stopScanner()
    }

    override fun onDestroy(){
        super.onDestroy()
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
}
