package com.c.downloadpdf_withprogress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_STORAGE_CODE = 1000;
    Button download;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        download = findViewById(R.id.download);

        download.setOnClickListener(view -> {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //Permission Denied , request permission
                String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_STORAGE_CODE);

            } else {
                //Permission already granted ,perform download
                startDownloading();
            }
        });
    }

    private void startDownloading() {
        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
        String URL="https://www.clickdimensions.com/links/TestPDFfile.pdf";
        String URL_ZIP="https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-zip-file.zip";
        //Create Download Request
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(URL));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading File...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());
        DownloadManager manager=(DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    //Handle Permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                    startDownloading();
                }else{
                    Toast.makeText(this, "Permission Denied!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}