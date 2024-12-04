package com.example.myfarmfinance.models;

public class User {
    private String id;
    private String nama;
    private String username;
    private String password;
    private String foto;

    private  String role;

    public User() {
    }

    // Constructor
    public User(String id, String nama, String username, String password, String foto, String role) {
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.foto = foto;
        this.role = role;
    }

    // Getter dan Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
