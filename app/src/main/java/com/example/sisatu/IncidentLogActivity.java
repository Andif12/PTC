package com.example.sisatu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IncidentLogActivity extends AppCompatActivity {

    private Spinner spinner;
    private TextView logTextView, logTextView2, logTextView3;
    private Button tombolselesai, tombolselesai2, tombolselesai3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_log);

        spinner = findViewById(R.id.dropdown);
        logTextView = findViewById(R.id.logkejadian);
        tombolselesai = findViewById(R.id.tombolSelesai);

        logTextView2 = findViewById(R.id.logkejadian2);
        tombolselesai2 = findViewById(R.id.tombolSelesai2);

        logTextView3 = findViewById(R.id.logkejadian3);
        tombolselesai3 = findViewById(R.id.tombolSelesai3);

        TextView tombolKembali = findViewById(R.id.tombolkembali);
        tombolKembali.setOnClickListener(view -> {
            String str_nama = getIntent().getStringExtra("nama");
            Intent intent = new Intent(IncidentLogActivity.this, HomeSatpamActivity.class);
            if (str_nama != null && !str_nama.isEmpty()) {
                intent.putExtra("nama", str_nama);
            }
            startActivity(intent);
            finish();
        });

        String[] items = {"Pilih berdasarkan", "Lihat berdasarkan Waktu", "Lihat Berdasarkan Tanggal"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, items);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 1:
                        fetchDataFromFirebase("waktu_dan_pesan");
                        break;
                    case 2:
                        fetchDataFromFirebase("tanggal_dan_pesan");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle case when nothing is selected (if needed)
            }
        });

        fetchDataFromFirebase(null);
    }

    private void fetchDataFromFirebase(String filter) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("incidentReports");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String[]> laporanList = new ArrayList<>();
                List<String> reportIds = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String reportId = snapshot.getKey();
                    String waktu = snapshot.child("waktu").getValue(String.class);
                    String tanggal = snapshot.child("tanggal").getValue(String.class);
                    String message = snapshot.child("message").getValue(String.class);
                    String status = snapshot.child("status").getValue(String.class);
                    String lokasi = snapshot.child("lokasi").getValue(String.class);

                    if (status == null) {
                        status = "Menunggu Validasi";
                    }

                    if (tanggal != null && waktu != null && message != null) {
                        laporanList.add(new String[]{waktu, tanggal, message, status, lokasi});
                        reportIds.add(reportId);
                    }
                }

                // Sort laporan berdasarkan waktu terbaru
                Collections.sort(laporanList, (lap1, lap2) -> lap2[0].compareTo(lap1[0]));

                // Reset visibility
                resetLogContainers();

                // Tampilkan maksimal 3 laporan terbaru
                for (int i = 0; i < Math.min(laporanList.size(), 3); i++) {
                    String[] laporan = laporanList.get(i);
                    String reportId = reportIds.get(i);
                    TextView logTextViewCurrent;
                    Button tombolSelesaiCurrent;

                    if (i == 0) {
                        logTextViewCurrent = logTextView;
                        tombolSelesaiCurrent = tombolselesai;
                    } else if (i == 1) {
                        logTextViewCurrent = logTextView2;
                        tombolSelesaiCurrent = tombolselesai2;
                    } else {
                        logTextViewCurrent = logTextView3;
                        tombolSelesaiCurrent = tombolselesai3;
                    }

                    StringBuilder laporanBuilder = new StringBuilder();

                    if ("tanggal_dan_pesan".equals(filter)) {
                        laporanBuilder.append("Tanggal: ").append(laporan[1])
                                .append("\nPesan: ").append(laporan[2]);
                    } else if ("waktu_dan_pesan".equals(filter)) {
                        laporanBuilder.append("Waktu: ").append(laporan[0])
                                .append("\nPesan: ").append(laporan[2]);
                    } else {
                        laporanBuilder.append("Waktu: ").append(laporan[0])
                                .append("\nTanggal: ").append(laporan[1])
                                .append("\nPesan: ").append(laporan[2])
                                .append("\nLokasi: ").append(laporan[4]);
                    }

                    logTextViewCurrent.setText(laporanBuilder.toString());
                    setButtonTextBasedOnStatus(tombolSelesaiCurrent, laporan[3], reportId);

                    logTextViewCurrent.setVisibility(View.VISIBLE);
                    tombolSelesaiCurrent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(IncidentLogActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetLogContainers() {
        logTextView.setVisibility(View.GONE);
        tombolselesai.setVisibility(View.GONE);

        logTextView2.setVisibility(View.GONE);
        tombolselesai2.setVisibility(View.GONE);

        logTextView3.setVisibility(View.GONE);
        tombolselesai3.setVisibility(View.GONE);
    }

    private void setButtonTextBasedOnStatus(Button button, String status, String reportId) {
        if ("Menunggu Validasi".equals(status)) {
            button.setText("Segera Ditangani");
        } else if ("Segera Ditangani".equals(status)) {
            button.setText("Segera Ditangani");
        } else if ("Diatasi".equals(status)) {
            button.setText("Diatasi");
        } else {
            button.setText("Selesai");
        }

        button.setOnClickListener(v -> {
            if ("Menunggu Validasi".equals(status)) {
                updateStatusInFirebase(reportId, "Segera Ditangani", button);
            } else if ("Segera Ditangani".equals(status)) {
                updateStatusInFirebase(reportId, "Diatasi", button);
            }
        });
    }

    private void updateStatusInFirebase(String reportId, String newStatus, Button button) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("incidentReports").child(reportId);

        databaseReference.child("status").setValue(newStatus)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setButtonTextBasedOnStatus(button, newStatus, reportId);
                        Toast.makeText(IncidentLogActivity.this, "Status updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(IncidentLogActivity.this, "Failed to update status", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
