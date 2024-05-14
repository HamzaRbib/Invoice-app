package com.example.invoiceapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Invoice> invoiceList;
    public MyAdapter(Context context, ArrayList<Invoice> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;
    }
    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.invoice_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditInvoice.class);
            intent.putExtra("invoice", invoiceList.get(position));
            context.startActivity(intent);
            ((Activity)context).finish();
        });
        Invoice invoice = invoiceList.get(position);
        holder.hashtagTextView.setText(invoice.getHashtag().substring(0, 4).toUpperCase());
        holder.dueDateTextView.setText(invoice.getDueDate());
        holder.amountTextView.setText(String.format("$ %d",invoice.getAmount()));
        holder.nameTextView.setText(invoice.getName());
        holder.paidTextView.setText(invoice.getStatus());
        if (invoice.getStatus().equals("Paid")) {
            holder.paidTextView.setTextColor(context.getResources().getColor(R.color.green));
        }else{
            holder.paidTextView.setTextColor(context.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hashtagTextView, dueDateTextView, amountTextView, nameTextView, paidTextView;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            hashtagTextView = itemView.findViewById(R.id.hashtagTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            paidTextView = itemView.findViewById(R.id.statusTextView);
        }
    }
}
