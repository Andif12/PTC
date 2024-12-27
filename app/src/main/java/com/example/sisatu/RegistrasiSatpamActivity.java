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

public class RegistrasiSatpamActivity extends AppCompatActivity {

    EditText editTextNama, editTextIdPegawai, editTextJabatan, editTextEmail, editTextPassword;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_satpam);

        editTextNama = findViewById(R.id.NamaLengkap);
        editTextIdPegawai = findViewById(R.id.idPegawai);
        editTextJabatan = findViewById(R.id.Jabatan);
        editTextEmail = findViewById(R.id.Email);
        editTextPassword = findViewById(R.id.Password);

        Button btnRegissatpam = findViewById(R.id.btn_regis);
        btnRegissatpam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama, idPegawai, jabatan, email, password;
                nama = editTextNama.getText().toString();
                idPegawai = editTextIdPegawai.getText().toString();
                jabatan = editTextJabatan.getText().toString();
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                // Validasi input
                if (TextUtils.isEmpty(nama)) {
                    editTextNama.setError("Masukkan nama");
                    editTextNama.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(idPegawai)) {
                    editTextIdPegawai.setError("Masukkan ID Pegawai");
                    editTextIdPegawai.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(jabatan)) {
                    editTextJabatan.setError("Masukkan Jabatan");
                    editTextJabatan.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Masukkan Email");
                    editTextEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Masukkan Password");
                    editTextPassword.requestFocus();
                    return;
                }

                // Simpan ke Firebase
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users/satpam");
                HelperClass helperClass = new HelperClass(nama, idPegawai, jabatan, email, password);
                reference.child(idPegawai).setValue(helperClass);

                // Simpan idPegawai ke SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("idPegawai", idPegawai);
                editor.apply();

                Toast.makeText(RegistrasiSatpamActivity.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();

                // Berpindah ke LoginActivity
                startActivity(new Intent(RegistrasiSatpamActivity.this, LoginActivity.class));
            }
        });

        // Tombol kembali
        TextView tombol = findViewById(R.id.tombolkembali);
        tombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrasiSatpamActivity.this, RegistrasiAsActivity.class));
            }
        });
    }
}
