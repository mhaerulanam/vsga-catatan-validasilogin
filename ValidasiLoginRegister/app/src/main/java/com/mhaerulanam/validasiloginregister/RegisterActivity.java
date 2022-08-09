package com.mhaerulanam.validasiloginregister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    EditText txtUsername, txtPassword, txtEmail, txtNamaLengkap, txtAsalSekolah, txtAlamat;
    Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Register");

        txtUsername = findViewById(R.id.txt_username);
        txtPassword = findViewById(R.id.txt_password);
        txtEmail = findViewById(R.id.txt_email);
        txtNamaLengkap = findViewById(R.id.txt_nama_lengkap);
        txtAsalSekolah = findViewById(R.id.txt_asal_sekolah);
        txtAlamat = findViewById(R.id.txt_alamat);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidation()) {
                    simpanFileData();
                } else {
                    Toast.makeText(RegisterActivity.this, "Mohon lengkapi seluruh data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean isValidation() {
        if (txtUsername.getText().toString().equals("")) {
            txtUsername.setError("Harap isi field berikut");
            return false;
        } else if (txtPassword.getText().toString().equals("")) {
            txtPassword.setError("Harap isi field berikut");
            return false;
        } else if (txtNamaLengkap.getText().toString().equals("")) {
            txtNamaLengkap.setError("Harap isi field berikut");
            return false;
        } else if (txtAsalSekolah.getText().toString().equals("")) {
            txtAsalSekolah.setError("Harap isi field berikut");
            return false;
        } else if (txtAlamat.getText().toString().equals("")) {
            txtAlamat.setError("Harap isi field berikut");
            return false;
        } else {
            return true;
        }
    }

    void simpanFileData() {
        String isiFile = txtUsername.getText().toString() + ";" +
                txtPassword.getText().toString() + ";" +
                txtEmail.getText().toString() + ";" +
                txtNamaLengkap.getText().toString() + ";" +
                txtAsalSekolah.getText().toString() + ";" +
                txtAlamat.getText().toString() + ";";
        File file = new File(getFilesDir(), txtUsername.getText().toString());
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, false);
            outputStream.write(isiFile.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}