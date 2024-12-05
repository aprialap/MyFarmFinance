package com.example.myfarmfinance.models;

public class DataPendapatan {
    private String id;
    private String tanggal;
    private String jenisPendapatanId;
    private String jenisPendapatanNama;
    private String sumberPendapatan;
    private double total;

    public DataPendapatan() {
    }

    // Constructor
    public DataPendapatan(String id, String tanggal, String jenisPendapatanId,String jenisPendapatanNama, String sumberPendapatan, double total) {
        this.id = id;
        this.tanggal = tanggal;
        this.jenisPendapatanId = jenisPendapatanId;
        this.jenisPendapatanNama = jenisPendapatanNama;
        this.sumberPendapatan = sumberPendapatan;
        this.total = total;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJenisPendapatanId() {
        return jenisPendapatanId;
    }

    public void setJenisPendapatanId(String jenisPendapatanId) {
        this.jenisPendapatanId = jenisPendapatanId;
    }

    public String getJenisPendapatanNama() {
        return jenisPendapatanNama;
    }

    public void setJenisPendapatanNama(String jenisPendapatanNama) {
        this.jenisPendapatanNama = jenisPendapatanNama;
    }


    public String getSumberPendapatan() {
        return sumberPendapatan;
    }

    public void setSumberPendapatan(String sumberPendapatan) {
        this.sumberPendapatan = sumberPendapatan;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
