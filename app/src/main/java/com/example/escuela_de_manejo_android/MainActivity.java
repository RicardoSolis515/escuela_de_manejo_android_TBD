package com.example.escuela_de_manejo_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import db.Escuale_db;

public class MainActivity extends AppCompatActivity {

    private Button btnInstructor, btnAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInstructor = findViewById(R.id.btn_instructor);
        btnAuto = findViewById(R.id.btn_auto);

        btnInstructor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InstructorActivity.class);
            startActivity(intent);
            /*try {
                Escuale_db.getAppDatabase(getApplicationContext());
                Toast.makeText(this, "BD creada correctamente", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }*/

        });

        btnAuto.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AutoActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "Boton funciona", Toast.LENGTH_SHORT).show();

        });
    }
}
