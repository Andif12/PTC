package com.example.sisatu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileMahasiswaActivity extends AppCompatActivity {

    private TextView namaTextView, nimTextView, prodiTextView, jurusanTextView, emailTextView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_mahasiswa);

        // Inisialisasi View
        namaTextView = findViewById(R.id.nama);
        nimTextView = findViewById(R.id.nim);
        prodiTextView = findViewById(R.id.prodi);
        jurusanTextView = findViewById(R.id.jurusan);
        emailTextView = findViewById(R.id.email);

        // Tombol kembali
        TextView tombolKembali = findViewById(R.id.tombolkembali);
        tombolKembali.setOnClickListener(view -> {
            // Ambil nama dari Intent dan mengirimkannya kembali ke HomeActivity
            String str_nama = getIntent().getStringExtra("nama"); // Ambil data nama dari Intent
            Intent intent = new Intent(ProfileMahasiswaActivity.this, HomeActivity.class);
            if (str_nama != null && !str_nama.isEmpty()) {
                intent.putExtra("nama", str_nama); // Kirimkan kembali nama ke HomeActivity
            }
            startActivity(intent);
            finish(); // Pastikan Activity ini ditutup
        });

        // Ambil NIM dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String nim = sharedPreferences.getString("nim", null);

        if (nim != null) {
            // Referensi ke Firebase Database
            databaseReference = FirebaseDatabase.getInstance().getReference("users/mahasiswa").child(nim);

            // Ambil data dari Firebase
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nama = snapshot.child("nama").getValue(String.class);
                        String prodi = snapshot.child("prodi").getValue(String.class);
                        String jurusan = snapshot.child("jurusan").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);

                        // Log untuk memverifikasi data yang diambil
                        Log.d("ProfileMahasiswa", "Nama: " + nama + ", Prodi: " + prodi + ", Jurusan: " + jurusan + ", Email: " + email);

                        // Tampilkan data ke TextView
                        namaTextView.setText("Nama Lengkap: " + nama);
                        nimTextView.setText("NIM: " + nim);
                        jurusanTextView.setText("Jurusan: " + jurusan);
                        prodiTextView.setText("Program Studi: " + prodi);
                        emailTextView.setText("Email: " + email);
                    } else {
                        Toast.makeText(ProfileMahasiswaActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileMahasiswaActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "NIM tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
        }
    }
}
