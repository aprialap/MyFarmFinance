package com.example.myfarmfinance.models;

public class JenisPengeluaran {
    private String id;
    private String nama;

    public JenisPengeluaran() {
    }
    public JenisPengeluaran(String id, String nama) {
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
