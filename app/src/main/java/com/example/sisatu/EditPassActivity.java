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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPassActivity extends AppCompatActivity {

    private EditText editTextOldPassword, editTextNewPassword, editTextConfirmPassword;
    private Button btnUpdatePassword;
    private DatabaseReference reference;
    private String idPegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);

        editTextOldPassword = findViewById(R.id.lama);
        editTextNewPassword = findViewById(R.id.baru);
        editTextConfirmPassword = findViewById(R.id.baru2);
        btnUpdatePassword = findViewById(R.id.btn_editt);

        // Tombol kembali
        TextView tombol = findViewById(R.id.tombolkembali);
        tombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditPassActivity.this, SettingsActivity.class));
            }
        });

        // Ambil idPegawai dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        idPegawai = sharedPreferences.getString("idPegawai", "");

        if (TextUtils.isEmpty(idPegawai)) {
            Toast.makeText(this, "ID Pegawai tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inisialisasi Firebase Reference
        reference = FirebaseDatabase.getInstance().getReference("users/satpam");

        btnUpdatePassword.setOnClickListener(view -> updatePassword());
    }

    private void updatePassword() {
        String oldPassword = editTextOldPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (!validateInput(oldPassword, newPassword, confirmPassword)) return;

        // Validasi password lama di Firebase dan update jika sesuai
        reference.child(idPegawai).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String currentPassword = task.getResult().child("password").getValue(String.class);
                if (oldPassword.equals(currentPassword)) {
                    // Update password baru
                    reference.child(idPegawai).child("password").setValue(newPassword)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Password berhasil diupdate.", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Gagal mengupdate password: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    editTextOldPassword.setError("Password lama salah");
                    editTextOldPassword.requestFocus();
                }
            } else {
                Toast.makeText(this, "Data tidak ditemukan.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private boolean validateInput(String oldPassword, String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(oldPassword)) {
            editTextOldPassword.setError("Masukkan password lama");
            editTextOldPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(newPassword)) {
            editTextNewPassword.setError("Masukkan password baru");
            editTextNewPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            editTextConfirmPassword.setError("Konfirmasi password baru");
            editTextConfirmPassword.requestFocus();
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Password baru tidak cocok");
            editTextConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }
}