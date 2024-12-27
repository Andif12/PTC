package com.example.sisatu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SafeRuteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_rute);

        // Tombol kembali
        TextView tombolKembali = findViewById(R.id.tombolkembali);
        tombolKembali.setOnClickListener(view -> {
            // Ambil nama dari Intent dan mengirimkannya kembali ke HomeActivity
            String str_nama = getIntent().getStringExtra("nama"); // Ambil data nama dari Intent
            Intent intent = new Intent(SafeRuteActivity.this, HomeActivity.class);
            if (str_nama != null && !str_nama.isEmpty()) {
                intent.putExtra("nama", str_nama); // Kirimkan kembali nama ke HomeActivity
            }
            startActivity(intent);
            finish();
        });
    }
}
