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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class EditInvoice extends AppCompatActivity {
    MaterialToolbar appBar;
    TextInputEditText name, dueDate, amount;
    RadioGroup radioGroup;
    RadioButton paid, unpaid;
    Button btnSaveChanges, btnDelete;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_invoice);
        name = findViewById(R.id.nameEdit);
        dueDate = findViewById(R.id.dueDateEdit);
        amount = findViewById(R.id.amountEdit);
        radioGroup = findViewById(R.id.radioGroupEdit);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnDelete = findViewById(R.id.btnDelete);
        appBar = findViewById(R.id.topAppBarEdit);
        paid = findViewById(R.id.paidEdit);
        unpaid = findViewById(R.id.unpaidEdit);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Invoices");
        appBar.setOnClickListener(v -> {
            Intent i = new Intent(EditInvoice.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        Invoice invoice = (Invoice) getIntent().getSerializableExtra("invoice");
        assert invoice != null;
        name.setText(invoice.getName());
        dueDate.setText(invoice.getDueDate());
        amount.setText(String.valueOf(invoice.getAmount()));
        if (invoice.getStatus().equals("Paid")) {
            radioGroup.check(R.id.paidEdit);
        } else {
            radioGroup.check(R.id.unpaidEdit);
        }

        btnSaveChanges.setOnClickListener(v -> {
            String nameText = String.valueOf(name.getText());
            String dueDateText = String.valueOf(dueDate.getText());
            String amountText = String.valueOf(amount.getText());
            String status = paid.isChecked() ? "Paid" : "Unpaid";
            databaseReference.child(invoice.getHashtag())
                    .setValue(new Invoice(invoice.getHashtag(),
                            dueDateText, Long.parseLong(amountText),
                            nameText, status,
                            Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
            Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(EditInvoice.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                .setMessage("Are you sure you want to delete this invoice?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    databaseReference.child(invoice.getHashtag()).removeValue();
                    Toast.makeText(this, "Invoice deleted", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(EditInvoice.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }).show();
        });
    }
}