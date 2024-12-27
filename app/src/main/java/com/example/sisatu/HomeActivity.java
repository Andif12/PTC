package com.example.sisatu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ambil nama dari Intent jika ada
        TextView nama = findViewById(R.id.nama);
        Intent intent = getIntent();
        String str_nama = intent.getStringExtra("nama");

        // Cek apakah nama tersedia, jika tidak tampilkan pesan
        if (str_nama == null || str_nama.isEmpty()) {
            Toast.makeText(HomeActivity.this, "Tidak Ada Data Nama", Toast.LENGTH_SHORT).show();
        } else {
            nama.setText("Selamat Datang, " + str_nama);
        }

        // Ambil pesan terbaru dari Firebase dan tampilkan secara real-time
        TextView massage = findViewById(R.id.massage);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notifications");

        // Menggunakan addValueEventListener untuk mendengarkan perubahan data secara real-time
        databaseReference.orderByChild("timestamp").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String message = snapshot.child("message").getValue(String.class);
                        if (message != null) {
                            massage.setText(message); // Tampilkan pesan terbaru di TextView dengan ID 'massage'
                        }
                    }
                } else {
                    massage.setText("Tidak ada pesan baru.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Gagal mengambil pesan.", Toast.LENGTH_SHORT).show();
            }
        });

        // Menambahkan aksi klik untuk beberapa ImageView
        ImageView maps = findViewById(R.id.maps_preview);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke SafeRuteActivity
                Intent intent = new Intent(HomeActivity.this, SafeRuteActivity.class);
                intent.putExtra("nama", str_nama); // Mengirim nama
                startActivity(intent);
            }
        });

        ImageView report = findViewById(R.id.incident);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke IncidentReportActivity
                Intent intent = new Intent(HomeActivity.this, IncidentReportActivity.class);
                intent.putExtra("nama", str_nama); // Mengirim nama
                startActivity(intent);
            }
        });

        ImageView profile = findViewById(R.id.fotoprofil);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke SafeRuteActivity
                Intent intent = new Intent(HomeActivity.this, ProfileMahasiswaActivity.class);
                intent.putExtra("nama", str_nama); // Mengirim nama
                startActivity(intent);
            }
        });

        ImageView settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke SafeRuteActivity
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                intent.putExtra("nama", str_nama); // Mengirim nama
                startActivity(intent);
            }
        });

        // Menambahkan aksi klik untuk tombol Emergency
        Button emergencyButton = findViewById(R.id.emergency);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Referensi ke Firebase
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("incidentReports");

                // Membuat pesan darurat
                String emergencyMessage = "EMERGENCY: Mahasiswa membutuhkan bantuan segera. Segera cek laporan di bagian Satpam.";

                // Mendapatkan ID unik untuk pesan
                String key = databaseReference.push().getKey();

                // Mendapatkan waktu saat ini
                long currentTimeMillis = System.currentTimeMillis();

                // Format tanggal dan waktu secara terpisah
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
                String formattedDate = dateFormatter.format(currentTimeMillis);  // Contoh hasil: "10-12-2024"
                String formattedTime = timeFormatter.format(currentTimeMillis); // Contoh hasil: "14:45:30"

                if (key != null) {
                    // Menyimpan pesan darurat ke Firebase
                    databaseReference.child(key).child("message").setValue(emergencyMessage);
                    databaseReference.child(key).child("tanggal").setValue(formattedDate); // Tanggal dalam format terbaca
                    databaseReference.child(key).child("waktu").setValue(formattedTime);  // Waktu dalam format terbaca
                }

                // Tampilkan Toast sebagai konfirmasi
                Toast.makeText(HomeActivity.this, "Pesan darurat terkirim!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ambil nama dari Intent jika ada (untuk memastikan data tetap ada setelah kembali)
        TextView nama = findViewById(R.id.nama);
        Intent intent = getIntent();
        String str_nama = intent.getStringExtra("nama");

        // Cek apakah nama tersedia, jika tidak tampilkan pesan
        if (str_nama == null || str_nama.isEmpty()) {
            Toast.makeText(HomeActivity.this, "Tidak Ada Data Nama", Toast.LENGTH_SHORT).show();
        } else {
            nama.setText("Selamat Datang, " + str_nama);
        }
    }
}
