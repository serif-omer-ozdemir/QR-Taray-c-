package com.info.tarayici

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.info.tarayici.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var tasarim: ActivityMainBinding
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        tasarim = ActivityMainBinding.inflate(layoutInflater)
        setContentView(tasarim.root)

        val scannerView: CodeScannerView = tasarim.qrScanner


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) // eger onaylı degılse  git onayını yap
            {
                val permissList = arrayOf(android.Manifest.permission.CAMERA)

                requestPermissions(permissList, 1)
            }
        }
        codeScanner = CodeScanner(this, scannerView) // eklenen kutuphane kurucu
        codeScanner.camera = CodeScanner.CAMERA_BACK // arka kapmerayi kullan
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE// fokuslanma olayı aktif
        codeScanner.isAutoFocusEnabled = true// otomatık fokuslanma acık olsun
        codeScanner.isFlashEnabled = false// flash acık olsun mu bunu sonra if elseye ekleyebılırız

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(applicationContext, "Sonuc:${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback {    // hata durumunda ne yapsın
            runOnUiThread {
                Toast.makeText(applicationContext, "Hata :${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener { // tıkalandıgında ne olucak
            codeScanner.startPreview()
        }
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()// goruntulemeyı  baslat
    }

    override fun onStop() {
        codeScanner.releaseResources()
        super.onStop()
    }

}