package com.shivam.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;

public class PDFViewerActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    PDFView pdfView;
    String PDFName;
    String PDFPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        PDFName = getIntent().getStringExtra("name");
        PDFPath = getIntent().getStringExtra("path");


        pdfView = findViewById(R.id.pdfView);
        displayFromAsset();

    }

    private void displayFromAsset() {

        pdfView.fromUri(Uri.fromFile(new File(PDFPath)))
                .defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }

    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText(this, "Load Complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
//        Toast.makeText(this, "Page Changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Toast.makeText(this, "Page Error", Toast.LENGTH_SHORT).show();
    }
}