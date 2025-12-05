package com.example.escuela_de_manejo_android;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import db.Escuale_db;
import enteties.Instructor;

public class AgregarInstructorActivity extends AppCompatActivity {

    private EditText editNSS, editNombre, editApellidoPat, editApellidoMat, editMatriculaVehiculo;
    private Switch switchSenior;
    private Button btnGuardarInstructor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_instructor);

        editNSS = findViewById(R.id.editNSS);
        editNombre = findViewById(R.id.editNombre);
        editApellidoPat = findViewById(R.id.editApellidoPat);
        editApellidoMat = findViewById(R.id.editApellidoMat);
        editMatriculaVehiculo = findViewById(R.id.editMatriculaVehiculo);
        switchSenior = findViewById(R.id.switchSenior);
        btnGuardarInstructor = findViewById(R.id.btnGuardarInstructor);

        btnGuardarInstructor.setOnClickListener(v -> guardarInstructor());
    }

    private void guardarInstructor() {

        String NSS = editNSS.getText().toString().trim();
        String nombre = editNombre.getText().toString().trim();
        String apellidoPat = editApellidoPat.getText().toString().trim();
        String apellidoMat = editApellidoMat.getText().toString().trim();
        String matriculaVehiculo = editMatriculaVehiculo.getText().toString().trim();
        boolean senior = switchSenior.isChecked();

        if (NSS.isEmpty() || nombre.isEmpty() || apellidoPat.isEmpty()) {
            Toast.makeText(this, "Campos obligatorios vacíos", Toast.LENGTH_SHORT).show();
            return;
        }

        Instructor instructor = new Instructor(
                NSS,
                nombre,
                apellidoPat,
                apellidoMat,
                senior,
                matriculaVehiculo
        );

        try {
            Escuale_db db = Escuale_db.getAppDatabase(getApplicationContext());
            db.instructorDAO().agregarInstructor(instructor);

            Toast.makeText(this, "Instructor guardado", Toast.LENGTH_LONG).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Error guardando instructor: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
