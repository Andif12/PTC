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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText loginId, loginPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginId = findViewById(R.id.nim); // ID sesuai dengan layout XML
        loginPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(view -> {
            String id = loginId.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            // Validasi input
            if (TextUtils.isEmpty(id)) {
                Toast.makeText(LoginActivity.this, "Masukkan ID Pengguna", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Masukkan Kata Sandi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Memeriksa data pengguna berdasarkan ID
            checkUser(id, password);
        });

        TextView tombol = findViewById(R.id.tombolkembali);
        tombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    private void checkUser(String id, String password) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        // Cek data mahasiswa terlebih dahulu
        userRef.child("mahasiswa").child(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                // Data mahasiswa ditemukan
                DataSnapshot dataSnapshot = task.getResult();
                String storedPassword = dataSnapshot.child("password").getValue(String.class);
                String storedName = dataSnapshot.child("nama").getValue(String.class);

                if (storedPassword != null && storedPassword.equals(password)) {
                    // Login berhasil sebagai mahasiswa
                    Toast.makeText(LoginActivity.this, "Selamat datang, " + storedName, Toast.LENGTH_SHORT).show();
                    navigateToHome("mahasiswa", storedName);
                } else {
                    // Password salah untuk mahasiswa
                    Toast.makeText(LoginActivity.this, "Kata sandi salah", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Jika data mahasiswa tidak ditemukan, cek data satpam
                userRef.child("satpam").child(id).get().addOnCompleteListener(satpamTask -> {
                    if (satpamTask.isSuccessful() && satpamTask.getResult().exists()) {
                        // Data satpam ditemukan
                        DataSnapshot satpamSnapshot = satpamTask.getResult();
                        String satpamPassword = satpamSnapshot.child("password").getValue(String.class);
                        String satpamName = satpamSnapshot.child("nama").getValue(String.class);

                        if (satpamPassword != null && satpamPassword.equals(password)) {
                            // Login berhasil sebagai satpam
                            Toast.makeText(LoginActivity.this, "Selamat datang, " + satpamName, Toast.LENGTH_SHORT).show();
                            navigateToHome("satpam", satpamName);
                        } else {
                            // Password salah untuk satpam
                            Toast.makeText(LoginActivity.this, "Kata sandi salah", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Data satpam tidak ditemukan
                        Toast.makeText(LoginActivity.this, "ID Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void navigateToHome(String role, String nama) {
        Intent intent;
        if ("mahasiswa".equals(role)) {
            intent = new Intent(LoginActivity.this, HomeActivity.class);
        } else if ("satpam".equals(role)) {
            intent = new Intent(LoginActivity.this, HomeSatpamActivity.class);
        } else {
            Toast.makeText(this, "Peran tidak dikenal!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kirim nama pengguna ke halaman home
        intent.putExtra("nama", nama);
        startActivity(intent);
        finish();
    }
}
