package com.example.sisatu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileSatpamActivity extends AppCompatActivity {

    private TextView namaTextView, idPegawaiTextView, emailTextView, jabatanTextView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_satpam);

        // Menghubungkan TextView dengan id yang sesuai di XML
        namaTextView = findViewById(R.id.nama);
        idPegawaiTextView = findViewById(R.id.idPegawai);
        emailTextView = findViewById(R.id.email);
        jabatanTextView = findViewById(R.id.jabatan);

        // Tombol kembali
        TextView tombolKembali = findViewById(R.id.tombolkembali);
        tombolKembali.setOnClickListener(view -> {
            // Ambil nama dari Intent dan mengirimkannya kembali ke HomeActivity
            String str_nama = getIntent().getStringExtra("nama"); // Ambil data nama dari Intent
            Intent intent = new Intent(ProfileSatpamActivity.this, HomeSatpamActivity.class);
            if (str_nama != null && !str_nama.isEmpty()) {
                intent.putExtra("nama", str_nama); // Kirimkan kembali nama ke HomeActivity
            }
            startActivity(intent);
            finish(); // Pastikan Activity ini ditutup
        });

        // Ambil idPegawai dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String idPegawai = sharedPreferences.getString("idPegawai", null);

        if (idPegawai != null) {
            // Referensi ke Firebase Database
            databaseReference = FirebaseDatabase.getInstance().getReference("users/satpam").child(idPegawai);

            // Ambil data dari Firebase
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Ambil data dari Firebase
                        String nama = snapshot.child("nama").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String jabatan = snapshot.child("jabatan").getValue(String.class);

                        // Set data ke masing-masing TextView
                        namaTextView.setText("Nama Lengkap: " + nama);
                        idPegawaiTextView.setText("ID Pegawai: " + idPegawai);
                        emailTextView.setText("Email: " + email);
                        jabatanTextView.setText("Jabatan: " + jabatan);
                    } else {
                        Toast.makeText(ProfileSatpamActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileSatpamActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ProfileSatpamActivity.this, "ID Pegawai tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }
}
