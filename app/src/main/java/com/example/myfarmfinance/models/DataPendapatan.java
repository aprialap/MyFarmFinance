package com.example.myfarmfinance.models;

public class DataPendapatan {
    private int id;
    private String tanggal;
    private String jenisPendapatan;
    private String keterangan;
    private double total;

    // Constructor
    public DataPendapatan(int id, String tanggal, String jenisPendapatan, String keterangan, double total) {
        this.id = id;
        this.tanggal = tanggal;
        this.jenisPendapatan = jenisPendapatan;
        this.keterangan = keterangan;
        this.total = total;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJenisPendapatan() {
        return jenisPendapatan;
    }

    public void setJenisPendapatan(String jenisPendapatan) {
        this.jenisPendapatan = jenisPendapatan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
