package com.example.escuela_de_manejo_android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import db.Escuale_db;
import enteties.Auto;

public class EditarAutoActivity extends Activity {

    private EditText edtMatricula, edtMarca, edtModelo, edtKilometraje;
    private Button btnActualizar, btnCancelar;
    private Escuale_db db;
    private String matriculaOriginal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_auto);

        db = Escuale_db.getAppDatabase(this);

        edtMatricula = findViewById(R.id.edt_matricula_editar);
        edtMarca = findViewById(R.id.edt_marca_editar);
        edtModelo = findViewById(R.id.edt_modelo_editar);
        edtKilometraje = findViewById(R.id.edt_kilometraje_editar);

        btnActualizar = findViewById(R.id.btn_actualizar_auto);
        btnCancelar = findViewById(R.id.btn_cancelar_editar);

        // Recibir matrícula del auto a editar
        matriculaOriginal = getIntent().getStringExtra("matricula");

        if (matriculaOriginal == null) {
            Toast.makeText(this, "Error al recibir el auto", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDatosAuto(matriculaOriginal);

        btnActualizar.setOnClickListener(v -> actualizarAuto());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarDatosAuto(String matricula) {
        Auto auto = db.autoDAO().mostrarUnico(matricula);

        if (auto != null) {
            edtMatricula.setText(auto.getMatricula());
            edtMarca.setText(auto.getMarca());
            edtModelo.setText(auto.getModelo());
            edtKilometraje.setText(String.valueOf(auto.getKilometraje()));
        } else {
            Toast.makeText(this, "Auto no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void actualizarAuto() {
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
        db.autoDAO().actualizarAutoPorMatricula(marca, modelo, ""+km, false, matricula);
/*
        if (resultado) {
            Toast.makeText(this, "Auto actualizado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
        }*/
    }
}
