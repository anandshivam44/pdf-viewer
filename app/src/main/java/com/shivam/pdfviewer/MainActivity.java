package com.shivam.pdfviewer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "debug-mode";

    private final int REQUEST_READ_STORAGE = 13;
    private ArrayList<PDFDoc> files;//list of all pdf files
    private RecyclerView pdfList;
    MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        pdfList = findViewById(R.id.recycler_list_pdf);

        files = new ArrayList<>();//initialized empty object

        PermissionManager permissionManager = new PermissionManager(this);

        permissionManager.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionManager.PermissionAskListener() {
            @Override
            public void onNeedPermission() {
                Log.d(TAG, "onNeedPermission: ");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
            }

            @Override
            public void onPermissionPreviouslyDenied() {
                /*Last time permission was denied. You can ask again.*/
                Log.d(TAG, "onPermissionPreviouslyDenied: ");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
            }

            @Override
            public void onPermissionPreviouslyDeniedWithNeverAskAgain() {
                /*App can't ask permission until explicitly granted permission from settings*/
                Log.d(TAG, "onPermissionPreviouslyDeniedWithNeverAskAgain: ");
                dialogForSettings("Permission Denied", "Access Denied. Can't read pdf from storage.");
            }

            @Override
            public void onPermissionGranted() {
                /*Permission already Granted before. So Start searching PDF's*/
                Log.d(TAG, "onPermissionGranted: ");
                Log.d(TAG, "Start search");
                startSearchingPdf(Environment.getExternalStorageDirectory());// crawl and search external directory for all PDF's
            }
        });


        /**
         *Initialized Recycler View
         * */
        adapter = new MyListAdapter(files, new MyListAdapter.ItemEvents() {
            @Override
            public void onItemClicked(int position) {
                /*On Item Clicked Open PDF in a New Activity from Main Activity*/
                /*Take meta-data of PDF i.e. PDF Name and Path from MainActivity.java to New Activity*/
                openPDFViewerActivity(position);
            }
        });
        pdfList.setHasFixedSize(true);
        pdfList.setLayoutManager(new LinearLayoutManager(this));
        pdfList.setAdapter(adapter);
        Log.d(TAG, "onCreate: EXIT");


    }

    private void openPDFViewerActivity(int position) {
        Log.d(TAG, "Start openPDFViewerActivity");
        Intent i = new Intent(MainActivity.this, PDFViewerActivity.class);
        i.putExtra("name", files.get(position).getName());
        i.putExtra("path", files.get(position).getPath());
        startActivity(i);

    }

    public void startSearchingPdf(File dir) {
//        Log.d(TAG, "Start search");
        String pdfPattern = ".pdf";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    startSearchingPdf(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)) {
                        /*
                         * if file of type PDF found then add object to Array List
                         * */
//                        Log.d(TAG, "walkdir: " + listFile[i].getName() + " " + listFile[i].getAbsolutePath().toString());
                        files.add(new PDFDoc(listFile[i].getName().toString(), listFile[i].getAbsolutePath().toString()));

                    }
                }
            }
        }
    }

    private void dialogForSettings(String title, String msg) {
        Log.d(TAG, "dialogForSettings: ");
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToSettings();
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Start search");
                    startSearchingPdf(Environment.getExternalStorageDirectory());// crawl and search external directory for all PDF's

                    adapter.notifyDataSetChanged();
                } else {
                    // Permission was denied.......
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    private void goToSettings() {
        Log.d(TAG, "goToSettings: ");
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + getPackageName());
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (data != null) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(photo);
        }
    }
}

