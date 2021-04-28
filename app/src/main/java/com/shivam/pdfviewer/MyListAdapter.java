package com.shivam.pdfviewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    ArrayList<PDFDoc> files;
    ItemEvents itemEventsObject;

    public MyListAdapter(ArrayList<PDFDoc> files,ItemEvents itemEventsObject) {
        this.files = files;
        this.itemEventsObject=itemEventsObject;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PDFDoc viewHolderData = files.get(position);
        holder.pdfName.setText(viewHolderData.getName());
        holder.pdfLocation.setText(viewHolderData.getPath());
        holder.viewHolderRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemEventsObject.onItemClicked(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View viewHolder;
        public ImageView imageView;
        public TextView pdfName;
        public TextView pdfLocation;
        public RelativeLayout viewHolderRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.viewHolderRecyclerView =itemView.findViewById(R.id.view_holder);
            this.pdfName = (TextView) itemView.findViewById(R.id.pdf_name);
            this.viewHolder=itemView.findViewById(R.id.main_view_holder);
            this.pdfLocation=itemView.findViewById(R.id.pdf_location);
        }
    }
    public interface ItemEvents{

        void onItemClicked(int position);
    }
}