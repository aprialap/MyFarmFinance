package com.example.myfarmfinance.models;

public class DataPengeluaran {
    private String id;
    private String tanggal;
    private String jenisPengeluaranId;
    private String jenisPengeluaranNama;
    private String keterangan;
    private double jumlahPengeluaran;

    public DataPengeluaran() {
    }

    // Constructor
    public DataPengeluaran(String id, String tanggal, String jenisPengeluaranId,String jenisPengeluaranNama, String keterangan, double jumlahPengeluaran) {
        this.id = id;
        this.tanggal = tanggal;
        this.jenisPengeluaranId = jenisPengeluaranId;
        this.jenisPengeluaranNama = jenisPengeluaranNama;
        this.keterangan = keterangan;
        this.jumlahPengeluaran = jumlahPengeluaran;
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

    public String getJenisPengeluaranId() {
        return jenisPengeluaranId;
    }

    public void setJenisPengeluaranId(String jenisPengeluaranId) {
        this.jenisPengeluaranId = jenisPengeluaranId;
    }

    public String getJenisPengeluaranNama() {
        return jenisPengeluaranNama;
    }

    public void setJenisPengeluaranNama(String jenisPengeluaranNama) {
        this.jenisPengeluaranNama = jenisPengeluaranNama;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public double getJumlahPengeluaran() {
        return jumlahPengeluaran;
    }

    public void setJumlahPengeluaran(double jumlahPengeluaran) {
        this.jumlahPengeluaran = jumlahPengeluaran;
    }
}
