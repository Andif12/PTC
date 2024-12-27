package com.example.sisatu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeSatpamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_satpam);

        // Ambil nama dari Intent
        TextView nama = findViewById(R.id.nama);
        Intent intent = getIntent();
        String str_nama = intent.getStringExtra("nama");

        // Validasi nama
        if (str_nama == null || str_nama.isEmpty()) {
            Toast.makeText(HomeSatpamActivity.this, "Tidak Ada Data Nama", Toast.LENGTH_SHORT).show();
        } else {
            nama.setText("Selamat Datang, " + str_nama);
        }

        // Menyambungkan tombol untuk berbagai aktivitas
        ImageView feed = findViewById(R.id.livestream);
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke PemantauanActivity
                Intent intent = new Intent(HomeSatpamActivity.this, PemantauanLiveActivity.class);
                intent.putExtra("nama", str_nama); // Mengirim nama
                startActivity(intent);
            }
        });

        ImageView cek = findViewById(R.id.ceklaporan);
        cek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke cekLaporanActivity
                Intent intent = new Intent(HomeSatpamActivity.this, ReportCheckActivity.class);
                intent.putExtra("nama", str_nama); // Mengirim nama
                startActivity(intent);
            }
        });

        ImageView log = findViewById(R.id.logkejadian);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke logKejadianActivity
                Intent intent = new Intent(HomeSatpamActivity.this, IncidentLogActivity.class);
                intent.putExtra("nama", str_nama); // Mengirim nama
                startActivity(intent);
            }
        });

        ImageView laporan = findViewById(R.id.laporan);
        laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke laporanActivity
                Intent intent = new Intent(HomeSatpamActivity.this, SecurityAllertActivity.class);
                intent.putExtra("nama", str_nama); // Mengirim nama
                startActivity(intent);
            }
        });

        ImageView profile = findViewById(R.id.fotoprofil);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke laporanActivity
                Intent intent = new Intent(HomeSatpamActivity.this, ProfileSatpamActivity.class);
                intent.putExtra("nama", str_nama); // Mengirim nama
                startActivity(intent);
            }
        });

        ImageView settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke laporanActivity
                Intent intent = new Intent(HomeSatpamActivity.this, SettingsActivity.class);
                intent.putExtra("userType", "satpam"); // Atau "mahasiswa" sesuai jenis pengguna
                intent.putExtra("nama", str_nama); // Jika ada data nama
                startActivity(intent);
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
            Toast.makeText(HomeSatpamActivity.this, "Tidak Ada Data Nama", Toast.LENGTH_SHORT).show();
        } else {
            nama.setText("Selamat Datang, " + str_nama);
        }
    }
}
