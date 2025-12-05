package com.example.escuela_de_manejo_android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.escuela_de_manejo_android.R;

import db.Escuale_db;
import enteties.Auto;

public class AgregarAutoActivity extends Activity {

    private EditText edtMatricula, edtMarca, edtModelo, edtKilometraje;
    private Button btnGuardar, btnCancelar;
    private Escuale_db db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_auto);

        db = Escuale_db.getAppDatabase(this);

        edtMatricula = findViewById(R.id.edt_edit_matricula);
        edtMarca = findViewById(R.id.edt_edit_marca);
        edtModelo = findViewById(R.id.edt_edit_modelo);
        edtKilometraje = findViewById(R.id.edt_edit_kilometraje);

        btnGuardar = findViewById(R.id.btn_editar_auto_guardar);
        btnCancelar = findViewById(R.id.btn_editar_auto_cancelar);

        btnGuardar.setOnClickListener(v -> guardarAuto());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void guardarAuto() {
        String matricula = edtMatricula.getText().toString();
        String marca = edtMarca.getText().toString();
        String modelo = edtModelo.getText().toString();
        int km;

        try {
            km = Integer.parseInt(edtKilometraje.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Kilometraje inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (matricula.isEmpty() || marca.isEmpty() || modelo.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Auto auto = new Auto(matricula, false, marca, modelo, ""+km);
        db.autoDAO().agregarAuto(auto);
/*
        if (resultado) {
            Toast.makeText(this, "Auto registrado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error: La matrícula ya existe", Toast.LENGTH_SHORT).show();
        }*/
    }
}
