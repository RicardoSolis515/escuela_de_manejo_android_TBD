package com.example.escuela_de_manejo_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

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
        });

        btnAuto.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AutoActivity.class);
            startActivity(intent);
        });
    }
}
