package com.example.sisatu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SecurityAllertActivity extends AppCompatActivity {

    private EditText incidentMessageInput;
    private Button sendIncidentButton;
    private TextView messageDisplay;

    private DatabaseReference databaseReference;
    private int reportCount = 0; // Variabel untuk menghitung jumlah laporan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_allert);

        // Inisialisasi tampilan
        incidentMessageInput = findViewById(R.id.inputMessage);
        sendIncidentButton = findViewById(R.id.sendButton);
        messageDisplay = findViewById(R.id.messageDisplay);

        // Tombol kembali
        TextView tombolKembali = findViewById(R.id.tombolkembali);
        tombolKembali.setOnClickListener(view -> {
            // Ambil nama dari Intent dan mengirimkannya kembali ke HomeActivity
            String str_nama = getIntent().getStringExtra("nama"); // Ambil data nama dari Intent
            Intent intent = new Intent(SecurityAllertActivity.this, HomeSatpamActivity.class);
            if (str_nama != null && !str_nama.isEmpty()) {
                intent.putExtra("nama", str_nama); // Kirimkan kembali nama ke HomeActivity
            }
            startActivity(intent);
            finish(); // Pastikan Activity ini ditutup
        });

        // Referensi Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("notifications");

        // Hitung jumlah laporan saat ini
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reportCount = (int) dataSnapshot.getChildrenCount(); // Hitung jumlah laporan
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SecurityAllertActivity.this, "Gagal menghitung laporan.", Toast.LENGTH_SHORT).show();
            }
        });

        sendIncidentButton.setOnClickListener(v -> sendIncidentReport());
    }

    private void sendIncidentReport() {
        String message = incidentMessageInput.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Pesan tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simpan data ke Firebase
        String notificationId = databaseReference.push().getKey();
        reportCount++; // Tingkatkan jumlah laporan

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("report_id", "Laporan " + reportCount); // Tambahkan label laporan
        notificationData.put("sender_id", "satpam");
        notificationData.put("message", message);
        notificationData.put("timestamp", System.currentTimeMillis() / 1000);

        if (notificationId != null) {
            databaseReference.child(notificationId).setValue(notificationData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(SecurityAllertActivity.this, "Pesan berhasil dikirim ke mahasiswa!", Toast.LENGTH_SHORT).show();
                        incidentMessageInput.setText(""); // Reset kolom input
                        messageDisplay.setVisibility(View.VISIBLE);
                        messageDisplay.setText(message); // Tampilkan label dan pesan
                    })
                    .addOnFailureListener(e -> Toast.makeText(SecurityAllertActivity.this, "Gagal mengirim pesan.", Toast.LENGTH_SHORT).show());
        }
    }
}
