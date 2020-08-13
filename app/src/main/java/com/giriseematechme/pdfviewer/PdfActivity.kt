package com.giriseematechme.pdfviewer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_pdf.*
import java.io.File

class PdfActivity : AppCompatActivity() {
    lateinit var uri: Uri
    val REQUEST_CODE: Int =1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)
        openPdf.setOnClickListener {
            if(checkLocalPermission())
                openPdfFile()
            else
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_CODE)
                }
        }
    }

    private fun checkLocalPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return true
        }
        return ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED

    }

    private fun openPdfFile() {
        val file=File(Environment.getExternalStorageDirectory().absolutePath
                + "/Download/OCJP.pdf")
        if(file.exists()){
            val intent= Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
           if(Build.VERSION.SDK_INT>24){
               uri=FileProvider.getUriForFile(this, applicationContext.packageName
                       + ".provider",file)
           }else
               uri=Uri.fromFile(file)
            intent.setDataAndType(uri,"application/pdf")
            try{
            startActivity(intent)
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED&&
                requestCode==REQUEST_CODE)
            openPdfFile()
        else
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),2)
    }
}