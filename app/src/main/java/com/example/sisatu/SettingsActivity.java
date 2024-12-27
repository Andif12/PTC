package com.example.sisatu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Ambil nama dari Intent jika ada
        Intent intent = getIntent();
        String str_nama = intent.getStringExtra("nama"); // Ambil data nama dari Intent
        String userType = intent.getStringExtra("userType"); // Ambil jenis pengguna dari Intent

        // Tombol kembali
        TextView tombolKembali = findViewById(R.id.tombolkembali);
        tombolKembali.setOnClickListener(view -> {
            Intent backIntent;
            if ("satpam".equals(userType)) {
                backIntent = new Intent(SettingsActivity.this, HomeSatpamActivity.class);
            } else if ("mahasiswa".equals(userType)) {
                backIntent = new Intent(SettingsActivity.this, HomeActivity.class);
            } else {
                // Jika userType tidak diketahui, arahkan ke HomeActivity sebagai default
                backIntent = new Intent(SettingsActivity.this, HomeActivity.class);
            }

            // Kirimkan kembali nama jika ada
            if (str_nama != null && !str_nama.isEmpty()) {
                backIntent.putExtra("nama", str_nama);
            }

            startActivity(backIntent);
            finish(); // Tutup SettingsActivity
        });


        TextView tombolkeluar = findViewById(R.id.tombolkeluar);
        tombolkeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });

        // Tombol ganti password
        TextView change = findViewById(R.id.change);
        change.setOnClickListener(view -> {
            Intent editIntent;
            if ("satpam".equals(userType)) {
                editIntent = new Intent(SettingsActivity.this, EditPassActivity.class);
            } else {
                editIntent = new Intent(SettingsActivity.this, EditPasswordActivity.class);
            }
            editIntent.putExtra("nama", str_nama);
            startActivity(editIntent);
        });

        TextView privacy = findViewById(R.id.privacy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke SafeRuteActivity
                Intent intent = new Intent(SettingsActivity.this, PrivacyPoliceActivity.class);
                intent.putExtra("nama", str_nama); // Mengirim nama
                startActivity(intent);
            }
        });

        TextView about = findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengirimkan nama ke SafeRuteActivity
                Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
                intent.putExtra("nama", str_nama); // Mengirim nama
                startActivity(intent);
            }
        });
    }
}