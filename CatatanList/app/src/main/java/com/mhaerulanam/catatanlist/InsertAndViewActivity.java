package com.mhaerulanam.catatanlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class InsertAndViewActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int REQUEST_CODE_STORAGE = 100;
    int eventID = 0;
    EditText edtFileName, edtContent;
    Button btnSimpan;
    boolean isEditable = false;
    String fileName = "";
    String tempCatatan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_and_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        edtFileName = findViewById(R.id.edt_filename);
        edtContent = findViewById(R.id.edt_content);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnSimpan.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fileName = extras.getString("filename");
            edtFileName.setText(fileName);
            getSupportActionBar().setTitle("Ubah Catatan");
        } else {
            getSupportActionBar().setTitle("Tambah Catatan");
        }
        eventID = 1;
        if (Build.VERSION.SDK_INT >= 23) {
            if (periksaIzinPenyimpanan()) {
                bacaFile();
            }
        } else {
            bacaFile();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_simpan) {
            eventID = 2;
            if (!tempCatatan.equals(edtContent.getText().toString())) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (periksaIzinPenyimpanan()) {
                        tampilkanDialogKonfirmasiPenyimpanan();
                    }
                } else {
                    tampilkanDialogKonfirmasiPenyimpanan();
                }
            }
        }
    }

    public boolean periksaIzinPenyimpanan() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (eventID == 1) {
                    bacaFile();
                } else {
                    tampilkanDialogKonfirmasiPenyimpanan();
                }
            }
        }
    }

    void bacaFile() {
        String path = Environment.getExternalStorageDirectory().toString() + "/kominfo-proyek1";
        File file = new File(path, edtFileName.getText().toString());
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
                System.out.println("Error " + e.getMessage());
            }
            tempCatatan = text.toString();
            edtContent.setText(text.toString());
        }
    }

    void buatDanUbah() {
        boolean status = false;
        String message = "";

        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        String path = Environment.getExternalStorageDirectory().toString() + "/kominfo-proyek1"+ File.separator;
        Log.d("LOCAL", path);
        File parent = new File(path);
        if (parent.exists()) {
            // edit
            File file = new File(path,
                    edtFileName.getText().toString());
            FileOutputStream outputStream;
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file);
                OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                streamWriter.append(edtContent.getText());
                streamWriter.flush();
                streamWriter.close();
                outputStream.flush();
                outputStream.close();

                status = true;

                message = "Berhasil mengedit file";
            } catch (Exception e) {
                Log.e("ERROR_FILE", e.toString());
                e.printStackTrace();
            }
        } else {
            parent.mkdir();
            File file = new File(path, edtFileName.getText().toString());
            FileOutputStream outputStream;
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file, false);
                outputStream.write(edtContent.getText().toString().getBytes());
                outputStream.flush();
                outputStream.close();

                status = true;

                message = "Berhasil membuat file";
            } catch (Exception e) {
                Log.e("ERROR_FILE2", e.toString());

                e.printStackTrace();
            }
        }

        this.finish();
        if (status)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Gagal membuat file", Toast.LENGTH_SHORT).show();
    }

    void tampilkanDialogKonfirmasiPenyimpanan() {
        new AlertDialog.Builder(this)
                .setTitle("Simpan Catatan")
                .setMessage("Apakah Anda yakin ingin menyimpan Catatan ini?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        buatDanUbah();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onBackPressed() {
        if (!tempCatatan.equals(edtContent.getText().toString())) {
//            tampilkanDialogKonfirmasiPenyimpanan();
        }
        super.onBackPressed();
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}