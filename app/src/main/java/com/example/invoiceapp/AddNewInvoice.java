package com.example.invoiceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddNewInvoice extends AppCompatActivity {
    MaterialToolbar appBar;
    TextInputEditText name, dueDate, amount;
    RadioButton paid, unpaid;
    Button btnAdd;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_invoice);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Invoices");

        name = findViewById(R.id.name);
        dueDate = findViewById(R.id.dueDate);
        amount = findViewById(R.id.amount);
        paid = findViewById(R.id.paid);
        unpaid = findViewById(R.id.unpaid);
        btnAdd = findViewById(R.id.btnAdd);

        appBar = findViewById(R.id.topAppBar);
        appBar.setOnClickListener(v -> {
            Intent i = new Intent(AddNewInvoice.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        btnAdd.setOnClickListener(v -> {
            String nameText = String.valueOf(name.getText());
            String dueDateText = String.valueOf(dueDate.getText());
            String amountText = String.valueOf(amount.getText());
            String status = paid.isChecked() ? "Paid" : "Unpaid";

            if (nameText.isBlank() || dueDateText.isBlank() || amountText.isBlank()) {
                new MaterialAlertDialogBuilder(this)
                        .setMessage("All fields are required")
                        .setPositiveButton("Ok", null)
                        .show();
                return;
            }
            Invoice invoice = new Invoice("#", dueDateText, Long.parseLong(amountText), nameText, status,
                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            databaseReference.push().setValue(invoice);
            Toast.makeText(this, "Invoice added successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AddNewInvoice.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}