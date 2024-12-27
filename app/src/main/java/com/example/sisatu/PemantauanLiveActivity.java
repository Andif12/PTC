package com.example.sisatu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class PemantauanLiveActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemantauan_live);

        // Tombol kembali
        TextView tombolKembali = findViewById(R.id.tombolkembali);
        tombolKembali.setOnClickListener(view -> {
            // Ambil nama dari Intent dan mengirimkannya kembali ke HomeActivity
            String str_nama = getIntent().getStringExtra("nama"); // Ambil data nama dari Intent
            Intent intent = new Intent(PemantauanLiveActivity.this, HomeSatpamActivity.class);
            if (str_nama != null && !str_nama.isEmpty()) {
                intent.putExtra("nama", str_nama); // Kirimkan kembali nama ke HomeActivity
            }
            startActivity(intent);
            finish(); // Pastikan Activity ini ditutup
        });

        // Inisialisasi VideoView
        videoView = findViewById(R.id.VideoView);

        // Video pertama (dari folder raw)
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video); // video.mp4 yang ada di folder raw
        videoView.setVideoURI(videoUri);
        videoView.start(); // Mulai pemutaran video
    }
}
