package com.example.sisatu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrasiAsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_as);

        Button btnRegismhs = findViewById(R.id.btn_mhs);
        btnRegismhs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrasiAsActivity.this, RegistrasiMhsActivity.class));
            }
        });
        Button btnRegissatpam = findViewById(R.id.btn_satpam);
        btnRegissatpam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrasiAsActivity.this, RegistrasiSatpamActivity.class));
            }
        });
    }
}