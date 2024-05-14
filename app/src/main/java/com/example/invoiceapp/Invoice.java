package com.example.invoiceapp;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Invoice implements Serializable {
    public Invoice() {

    }
    public Invoice(String hashtag, String dueDate, Long amount, String name, String status, String uid) {
        this.hashtag = hashtag;
        this.dueDate = dueDate;
        this.amount = amount;
        this.name = name;
        this.status = status;
        this.uid = uid;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return "Invoice{" +
                "hashtag='" + hashtag + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", amount=" + amount +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(hashtag, invoice.hashtag);
    }

    private String hashtag;
    private String dueDate;
    private Long amount;
    private String name;
    private String status;
    private String uid;
}
