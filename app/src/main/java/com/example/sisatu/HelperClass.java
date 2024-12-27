package com.example.sisatu;

public class HelperClass {
    // Atribut utama
    String nama, nim, jurusan, prodi, idPegawai, jabatan, email, password, role;

    // Constructor untuk mahasiswa
    public HelperClass(String nama, String nim, String prodi, String jurusan, String email, String password) {
        this.nama = nama;
        this.nim = nim;
        this.prodi = prodi;
        this.jurusan = jurusan;
        this.email = email;
        this.password = password;
        this.idPegawai = null; // Tidak relevan untuk mahasiswa
        this.jabatan = null; // Tidak relevan untuk mahasiswa
        this.role = "Mahasiswa";
    }

    // Constructor untuk satpam atau pegawai
    public HelperClass(String nama, String idPegawai, String jabatan, String email, String password) {
        this.nama = nama;
        this.idPegawai = idPegawai;
        this.jabatan = jabatan;
        this.email = email;
        this.password = password;
        this.nim = null; // Tidak relevan untuk satpam
        this.jurusan = null; // Tidak relevan untuk satpam
        this.prodi = null; // Tidak relevan untuk satpam
        this.role = "Satpam"; // Atur role sebagai pegawai/satpam
    }

    // Constructor default (semua atribut, fleksibel untuk berbagai role)
    public HelperClass(String nama, String nim, String prodi, String jurusan, String idPegawai, String jabatan, String email, String password, String role) {
        this.nama = nama;
        this.nim = nim;
        this.prodi = prodi;
        this.jurusan = jurusan;
        this.idPegawai = idPegawai;
        this.jabatan = jabatan;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getter dan Setter untuk semua atribut
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(String idPegawai) {
        this.idPegawai = idPegawai;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
