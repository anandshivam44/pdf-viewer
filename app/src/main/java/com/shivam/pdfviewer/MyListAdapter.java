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
        holder.textView.setText(viewHolderData.getName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
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
        public TextView textView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.viewHolder=itemView.findViewById(R.id.main_view_holder);
        }
    }
    public interface ItemEvents{

        void onItemClicked(int position);
    }
}