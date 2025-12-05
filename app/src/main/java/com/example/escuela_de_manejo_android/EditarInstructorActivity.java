package com.example.escuela_de_manejo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import db.Escuale_db;
import enteties.Instructor;

public class EditarInstructorActivity extends AppCompatActivity {

    private EditText editNSS, editNombre, editApellidoPat, editApellidoMat, editMatriculaVehiculo;
    private Switch switchSenior;
    private Button btnActualizarInstructor;

    private Instructor instructorActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_instructor);

        editNSS = findViewById(R.id.editNSS);
        editNombre = findViewById(R.id.editNombre);
        editApellidoPat = findViewById(R.id.editApellidoPat);
        editApellidoMat = findViewById(R.id.editApellidoMat);
        editMatriculaVehiculo = findViewById(R.id.editMatriculaVehiculo);
        switchSenior = findViewById(R.id.switchSenior);
        btnActualizarInstructor = findViewById(R.id.btnActualizarInstructor);

        String NSS_recibido = getIntent().getStringExtra("NSS_INSTRUCTOR");
        if (NSS_recibido == null) {
            Toast.makeText(this, "Error: No se recibió NSS", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        cargarInstructor(NSS_recibido);

        btnActualizarInstructor.setOnClickListener(v -> actualizarInstructor());
    }

    private void cargarInstructor(String NSS) {
        try {
            Escuale_db db = Escuale_db.getAppDatabase(getApplicationContext());
            instructorActual = db.instructorDAO().buscarPorNSS(NSS);

            if (instructorActual == null) {
                Toast.makeText(this, "Instructor no encontrado", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            editNSS.setText(instructorActual.getNSS());
            editNombre.setText(instructorActual.getNombre());
            editApellidoPat.setText(instructorActual.getApellidoPat());
            editApellidoMat.setText(instructorActual.getApellidoMat());
            editMatriculaVehiculo.setText(instructorActual.getMatriculaVehiculo());
            switchSenior.setChecked(instructorActual.isSenior());

        } catch (Exception e) {
            Toast.makeText(this, "Error cargando datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void actualizarInstructor() {
        if (instructorActual == null) {
            Toast.makeText(this, "No se puede actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        instructorActual.setNombre(editNombre.getText().toString().trim());
        instructorActual.setApellidoPat(editApellidoPat.getText().toString().trim());
        instructorActual.setApellidoMat(editApellidoMat.getText().toString().trim());
        instructorActual.setMatriculaVehiculo(editMatriculaVehiculo.getText().toString().trim());
        instructorActual.setSenior(switchSenior.isChecked());

        try {
            Escuale_db db = Escuale_db.getAppDatabase(getApplicationContext());
            db.instructorDAO().actualizarInstructor(instructorActual);

            Toast.makeText(this, "Instructor actualizado correctamente", Toast.LENGTH_LONG).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
