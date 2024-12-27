package com.example.sisatu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_check);

        TextView lapor = findViewById(R.id.lapor);
        Button btnSegeraDitangani = findViewById(R.id.btnSegeraDitangani);
        Button btnDiatasi = findViewById(R.id.btnDiatasi);
        Button btnSelesai = findViewById(R.id.btnSelesai);

        btnSegeraDitangani.setVisibility(View.GONE);
        btnDiatasi.setVisibility(View.GONE);
        btnSelesai.setVisibility(View.GONE);

        DatabaseReference incidentReportsRef = FirebaseDatabase.getInstance().getReference("incidentReports");

        incidentReportsRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot snapshot = dataSnapshot.getChildren().iterator().next();
                    String message = snapshot.child("message").getValue(String.class);
                    if (message != null) {
                        lapor.setText(message);
                        btnSegeraDitangani.setVisibility(View.VISIBLE);
                        btnDiatasi.setVisibility(View.VISIBLE);
                        btnSelesai.setVisibility(View.VISIBLE);

                        btnDiatasi.setEnabled(false);
                        btnSelesai.setEnabled(false);
                    }
                } else {
                    lapor.setText("Tidak ada laporan.");
                    btnSegeraDitangani.setVisibility(View.GONE);
                    btnDiatasi.setVisibility(View.GONE);
                    btnSelesai.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReportCheckActivity.this, "Gagal mengambil data laporan", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol kembali
        TextView tombolKembali = findViewById(R.id.tombolkembali);
        tombolKembali.setOnClickListener(view -> {
            // Ambil nama dari Intent dan mengirimkannya kembali ke HomeActivity
            String str_nama = getIntent().getStringExtra("nama"); // Ambil data nama dari Intent
            Intent intent = new Intent(ReportCheckActivity.this, HomeSatpamActivity.class);
            if (str_nama != null && !str_nama.isEmpty()) {
                intent.putExtra("nama", str_nama); // Kirimkan kembali nama ke HomeActivity
            }
            startActivity(intent);
            finish(); // Pastikan Activity ini ditutup
        });

        btnSegeraDitangani.setOnClickListener(view -> {
            updateStatus("Segera Ditangani");
            btnSegeraDitangani.setBackgroundColor(getResources().getColor(R.color.gray));
            btnDiatasi.setBackgroundColor(getResources().getColor(R.color.blue));
            btnDiatasi.setEnabled(true);
        });

        btnDiatasi.setOnClickListener(view -> {
            updateStatus("Diatasi");
            btnDiatasi.setBackgroundColor(getResources().getColor(R.color.gray));
            btnSelesai.setBackgroundColor(getResources().getColor(R.color.blue));
            btnSelesai.setEnabled(true);
        });

        btnSelesai.setOnClickListener(view -> {
            updateStatus("Selesai Ditangani");
            btnSelesai.setBackgroundColor(getResources().getColor(R.color.green));
        });
    }

    private void updateStatus(String status) {
        DatabaseReference incidentReportsRef = FirebaseDatabase.getInstance().getReference("incidentReports");

        // Assume we update the last report
        incidentReportsRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot snapshot = dataSnapshot.getChildren().iterator().next();
                    String reportId = snapshot.getKey();

                    incidentReportsRef.child(reportId).child("status").setValue(status);
                    Toast.makeText(ReportCheckActivity.this, "Laporan " + status, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReportCheckActivity.this, "Gagal memperbarui status laporan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
