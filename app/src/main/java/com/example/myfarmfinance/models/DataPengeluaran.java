package com.example.myfarmfinance.models;

public class DataPengeluaran {
    private int id;
    private String tanggal;
    private String jenisPengeluaran;
    private String sumberPengeluaran;
    private double total;

    // Constructor
    public DataPengeluaran(int id, String tanggal, String jenisPengeluaran, String sumberPengeluaran, double total) {
        this.id = id;
        this.tanggal = tanggal;
        this.jenisPengeluaran = jenisPengeluaran;
        this.sumberPengeluaran = sumberPengeluaran;
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

    public String getJenisPengeluaran() {
        return jenisPengeluaran;
    }

    public void setJenisPengeluaran(String jenisPengeluaran) {
        this.jenisPengeluaran = jenisPengeluaran;
    }

    public String getSumberPengeluaran() {
        return sumberPengeluaran;
    }

    public void setSumberPengeluaran(String sumberPengeluaran) {
        this.sumberPengeluaran = sumberPengeluaran;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
