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

public class RegistrasiMhsActivity extends AppCompatActivity {

    EditText editTextNama, editTextNim, editTextProdi, editTextJurusan, editTextEmail, editTextPassword;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_mhs);

        // Inisialisasi View
        editTextNama = findViewById(R.id.NamaLengkap);
        editTextNim = findViewById(R.id.NIM);
        editTextProdi = findViewById(R.id.Prodi);
        editTextJurusan = findViewById(R.id.Jurusan);
        editTextEmail = findViewById(R.id.Email);
        editTextPassword = findViewById(R.id.Password);

        Button btnRegismhs = findViewById(R.id.btn_regis);

        btnRegismhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = editTextNama.getText().toString().trim();
                String nim = editTextNim.getText().toString().trim();
                String prodi = editTextProdi.getText().toString().trim();
                String jurusan = editTextJurusan.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Validasi input
                if (TextUtils.isEmpty(nama)) {
                    editTextNama.setError("Masukkan Nama");
                    editTextNama.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(nim)) {
                    editTextNim.setError("Masukkan NIM");
                    editTextNim.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(prodi)) {
                    editTextProdi.setError("Masukkan Program Studi");
                    editTextProdi.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(jurusan)) {
                    editTextJurusan.setError("Masukkan Jurusan");
                    editTextJurusan.requestFocus();
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

                // Simpan data ke Firebase
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users/mahasiswa");

                HelperClass helperClass = new HelperClass(nama, nim, prodi, jurusan, email, password);
                reference.child(nim).setValue(helperClass);

                // Simpan NIM ke SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nim", nim);
                editor.apply();

                // Tampilkan pesan sukses
                Toast.makeText(RegistrasiMhsActivity.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();

                // Arahkan ke LoginActivity
                startActivity(new Intent(RegistrasiMhsActivity.this, LoginActivity.class));
            }
        });

        // Tombol kembali
        TextView tombol = findViewById(R.id.tombolkembali);
        tombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrasiMhsActivity.this, RegistrasiAsActivity.class));
            }
        });
    }
}
