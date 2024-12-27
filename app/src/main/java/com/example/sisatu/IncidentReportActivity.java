package com.example.sisatu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IncidentReportActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_report);

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("incidentReports");

        // Menampilkan popup panduan pelaporan
        showReportingGuidePopup();

        // Tombol kembali
        TextView tombolKembali = findViewById(R.id.tombolkembali);
        tombolKembali.setOnClickListener(view -> {
            // Ambil nama dari Intent dan mengirimkannya kembali ke HomeActivity
            String str_nama = getIntent().getStringExtra("nama"); // Ambil data nama dari Intent
            Intent intent = new Intent(IncidentReportActivity.this, HomeActivity.class);
            if (str_nama != null && !str_nama.isEmpty()) {
                intent.putExtra("nama", str_nama); // Kirimkan kembali nama ke HomeActivity
            }
            startActivity(intent);
            finish(); // Pastikan Activity ini ditutup
        });

        // Elemen UI lainnya
        EditText inputMessage = findViewById(R.id.inputMessage);
        TextView messageDisplay = findViewById(R.id.messageDisplay);
        Button sendButton = findViewById(R.id.sendButton);

        // Tombol Kirim
        sendButton.setOnClickListener(v -> {
            String message = inputMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                String reportId = databaseRef.push().getKey(); // ID unik untuk laporan
                if (reportId == null) {
                    Toast.makeText(this, "Gagal menghasilkan ID laporan", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Mendapatkan waktu, tanggal, dan lokasi
                String tanggal = getCurrentDate();
                String waktu = getCurrentTime();
                String lokasi = parseLocationFromMessage(message);

                // Menyimpan laporan ke Firebase
                saveReportToFirebase(reportId, message, tanggal, waktu, lokasi);

                // Menampilkan pesan dan membersihkan input
                messageDisplay.setText(message);
                messageDisplay.setVisibility(View.VISIBLE);
                inputMessage.setText("");
            } else {
                Toast.makeText(IncidentReportActivity.this, "Deskripsi kejadian tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showReportingGuidePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Panduan Pelaporan Kejadian");
        builder.setMessage(
                "Harap isi laporan kejadian dengan format berikut:\n\n" +
                        "Deskripsi Kejadian: [Ceritakan kejadian secara singkat]\n" +
                        "Jenis Bantuan: [Jenis bantuan yang dibutuhkan (Segera/Tidak Segera)]\n\n" +
                        "Contoh:\n" +
                        "Deskripsi Kejadian: Ada dua orang berkelahi di area parkir.\n" +
                        "Jenis Bantuan: Segera."
        );
        builder.setPositiveButton("Mengerti", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void saveReportToFirebase(String reportId, String message, String tanggal, String waktu, String lokasi) {
        // Membuat map laporan
        Map<String, Object> reportMap = new HashMap<>();
        reportMap.put("message", message);
        reportMap.put("tanggal", tanggal);
        reportMap.put("waktu", waktu);
        reportMap.put("lokasi", lokasi);

        // Simpan ke Firebase
        databaseRef.child(reportId).setValue(reportMap)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Laporan berhasil dikirim", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Gagal menyimpan laporan", Toast.LENGTH_SHORT).show());
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String parseLocationFromMessage(String message) {
        String lokasi = "Tidak diketahui";
        String[] words = message.split("\\s+"); // Memecah pesan menjadi kata-kata
        for (int i = 0; i < words.length - 1; i++) {
            if (words[i].equalsIgnoreCase("lokasi")) {
                lokasi = words[i + 1]; // Ambil kata setelah "lokasi"
                break;
            }
        }
        return lokasi;
    }
}
