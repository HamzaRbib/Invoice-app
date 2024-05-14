package com.example.invoiceapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Invoice> invoiceList, data;
    MyAdapter myAdapter;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser user;
    Chip all, paid, unpaid;
    ImageButton logout;
    FloatingActionButton fab;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        invoiceList = new ArrayList<>();
        data = new ArrayList<>();
        myAdapter = new MyAdapter(this, invoiceList);
        databaseReference = FirebaseDatabase.getInstance().getReference("Invoices");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        all = findViewById(R.id.all);
        paid = findViewById(R.id.paid);
        unpaid = findViewById(R.id.unpaid);

        progressBar = findViewById(R.id.progressBar);

        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddNewInvoice.class));
            finish();
        });
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this).setMessage("Are you sure you want to log out?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Log out", (dialog, which) -> {
                        auth.signOut();
                        Intent i = new Intent(MainActivity.this, Login.class);
                        startActivity(i);
                        finish();
                    })
                    .show();
        });

        all.setOnClickListener(v -> {
            paid.setChecked(false);
            unpaid.setChecked(false);
            invoiceList.clear();
            invoiceList.addAll(data);
            myAdapter.notifyDataSetChanged();
        });
        paid.setOnClickListener(v -> {
            all.setChecked(false);
            unpaid.setChecked(false);
            ArrayList<Invoice> newInvoiceList = data.stream()
                    .filter(invoice -> invoice.getStatus().equals("Paid"))
                    .collect(Collectors.toCollection(ArrayList::new));
            invoiceList.clear();
            invoiceList.addAll(newInvoiceList);
            myAdapter.notifyDataSetChanged();
        });
        unpaid.setOnClickListener(v -> {
            all.setChecked(false);
            paid.setChecked(false);
            ArrayList<Invoice> newInvoiceList = data.stream()
                    .filter(invoice -> invoice.getStatus().equals("Unpaid"))
                    .collect(Collectors.toCollection(ArrayList::new));
            invoiceList.clear();
            invoiceList.addAll(newInvoiceList);
            myAdapter.notifyDataSetChanged();
        });

        if (user != null) {
            fetchAllInvoices();
        } else {
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
            finish();
        }
    }

    private void fetchAllInvoices() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Invoice invoice = ds.getValue(Invoice.class);
                    String id = ds.getKey();
                    if (!invoiceList.contains(invoice)) {
                        assert invoice != null;
                        if (invoice.getUid().equals(user.getUid())) {
                            assert id != null;
                            invoice.setHashtag(id);
                            invoiceList.add(invoice);
                        }
                    }
                }
                data.clear();
                data.addAll(invoiceList);
                myAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
