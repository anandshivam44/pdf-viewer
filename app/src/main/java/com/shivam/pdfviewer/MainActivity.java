package com.shivam.pdfviewer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "debug-mode";
    private ArrayList<PDFDoc> files;//list of all pdf files
    private RecyclerView pdfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdfList = findViewById(R.id.recycler_list_pdf);

        files = new ArrayList<>();//initialized empty object

        search(Environment.getExternalStorageDirectory());// crawl and search external directory for all PDF's

        /**
         *Initialized Recycler View
         * */
        MyListAdapter adapter = new MyListAdapter(files, new MyListAdapter.ItemEvents() {
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


    }

    private void openPDFViewerActivity(int position) {
        Intent i = new Intent(MainActivity.this, PDFViewerActivity.class);
        i.putExtra("name",files.get(position).getName());
        i.putExtra("path",files.get(position).getPath());
        startActivity(i);

    }

    public void search(File dir) {
        String pdfPattern = ".pdf";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    search(listFile[i]);
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

}