package com.mhaerulanam.validasiloginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    EditText txtUsername, txtPassword, txtEmail, txtNamaLengkap,
            txtAsalSekolah, txtAlamat;
    Button btnSimpan;
    TextView textViewPassword;

    public static final String FILENAME = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Halaman Depan");

        txtUsername = findViewById(R.id.txt_username);
        txtPassword = findViewById(R.id.txt_password);
        txtEmail = findViewById(R.id.txt_email);
        txtNamaLengkap = findViewById(R.id.txt_nama_lengkap);
        txtAsalSekolah = findViewById(R.id.txt_asal_sekolah);
        txtAlamat = findViewById(R.id.txt_alamat);
        textViewPassword = findViewById(R.id.txt_view_password);

        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setVisibility(View.GONE);
        textViewPassword.setVisibility(View.GONE);
        txtUsername.setEnabled(false);
        txtPassword.setVisibility(View.GONE);
        txtEmail.setEnabled(false);
        txtNamaLengkap.setEnabled(false);
        txtAsalSekolah.setEnabled(false);
        txtAlamat.setEnabled(false);

        bacaFileLogin();
    }

    void bacaFileLogin() {
        File sdcard = getFilesDir();
        File file = new File(sdcard, FILENAME);
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line != null) {
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String data = text.toString();
            String[] dataUser = data.split(";");
            bacaDataUser(dataUser[0]);
        }
    }

    void bacaDataUser(String fileName) {
        File sdcard = getFilesDir();
        File file = new File(sdcard, fileName);
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line != null) {
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String data = text.toString();
            String[] dataUser = data.split(";");

            txtUsername.setText(dataUser[0]);
            txtEmail.setText(dataUser[2]);
            txtNamaLengkap.setText(dataUser[3]);
            txtAsalSekolah.setText(dataUser[4]);
            txtAlamat.setText(dataUser[5]);
        } else {
            Toast.makeText(this, "User Tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            tampilkanDialogKonfirmasiLogout();
        }
        return super.onOptionsItemSelected(item);
    }

    void hapusFile() {
        File file = new File(getFilesDir(), FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

    void tampilkanDialogKonfirmasiLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah anda yakin ingin Logout?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                    hapusFile();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}