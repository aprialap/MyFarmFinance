package com.example.myfarmfinance.models;

public class JenisPendapatan {
    private String id;
    private String nama;

    public JenisPendapatan() {
    }

    public JenisPendapatan(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getNama() {
        return nama;
    }
}
