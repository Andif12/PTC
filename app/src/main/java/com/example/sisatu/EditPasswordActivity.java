package com.example.sisatu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPasswordActivity extends AppCompatActivity {

    EditText editTextOldPassword, editTextNewPassword, editTextConfirmPassword;
    Button btnUpdatePassword;
    FirebaseDatabase database;
    DatabaseReference reference;
    String nim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        editTextOldPassword = findViewById(R.id.lama);
        editTextNewPassword = findViewById(R.id.baru);
        editTextConfirmPassword = findViewById(R.id.baru2);
        btnUpdatePassword = findViewById(R.id.btn_edit);

        // Tombol kembali
        TextView tombol = findViewById(R.id.tombolkembali);
        tombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditPasswordActivity.this, SettingsActivity.class));
            }
        });

        // Ambil nim dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        nim = sharedPreferences.getString("nim", "");

        // Pastikan nim valid
        if (TextUtils.isEmpty(nim)) {
            Toast.makeText(this, "NIM tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users/mahasiswa");

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = editTextOldPassword.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                // Validasi input
                if (TextUtils.isEmpty(oldPassword)) {
                    editTextOldPassword.setError("Masukkan password lama");
                    editTextOldPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(newPassword)) {
                    editTextNewPassword.setError("Masukkan password baru");
                    editTextNewPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    editTextConfirmPassword.setError("Konfirmasi password baru");
                    editTextConfirmPassword.requestFocus();
                    return;
                }
                if (!newPassword.equals(confirmPassword)) {
                    editTextConfirmPassword.setError("Password baru tidak cocok");
                    editTextConfirmPassword.requestFocus();
                    return;
                }

                // Validasi password lama dan update di Firebase
                reference.child(nim).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String currentPassword = snapshot.child("password").getValue(String.class);
                            if (currentPassword != null && currentPassword.equals(oldPassword)) {
                                reference.child(nim).child("password").setValue(newPassword);
                                Toast.makeText(EditPasswordActivity.this, "Password berhasil diupdate.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                editTextOldPassword.setError("Password lama salah");
                                editTextOldPassword.requestFocus();
                            }
                        } else {
                            Toast.makeText(EditPasswordActivity.this, "Data mahasiswa tidak ditemukan.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditPasswordActivity.this, "Gagal mengupdate password: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
