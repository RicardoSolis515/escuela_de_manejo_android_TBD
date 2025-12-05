package com.example.escuela_de_manejo_android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

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

        edtMatricula = findViewById(R.id.edtMatricula);
        edtMarca = findViewById(R.id.edtMarca);
        edtModelo = findViewById(R.id.edtModelo);
        edtKilometraje = findViewById(R.id.edtKilometraje);

        btnGuardar = findViewById(R.id.btnGuardarAuto);

        btnGuardar.setOnClickListener(v -> guardarAuto());
    }

    private void guardarAuto() {
        String matricula = edtMatricula.getText().toString().trim();
        String marca = edtMarca.getText().toString().trim();
        String modelo = edtModelo.getText().toString().trim();
        String kmStr = edtKilometraje.getText().toString().trim();

        if (matricula.isEmpty() || marca.isEmpty() || modelo.isEmpty() || kmStr.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int km;
        try {
            km = Integer.parseInt(kmStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Kilometraje inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si la matrícula ya existe
        Auto existente = db.autoDAO().mostrarUnico(matricula);
        if (existente != null) {
            Toast.makeText(this, "Error: La matrícula ya existe", Toast.LENGTH_SHORT).show();
            return;
        }

        Auto auto = new Auto(matricula,false, marca, modelo, ""+km);

        db.autoDAO().agregarAuto(auto);

        Toast.makeText(this, "Auto registrado correctamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}
