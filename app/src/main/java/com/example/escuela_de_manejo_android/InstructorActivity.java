package com.example.escuela_de_manejo_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import db.Escuale_db;
import enteties.Instructor;

public class InstructorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomAdapterInstructor adapter;
    private ArrayList<Instructor> listaInstructores = new ArrayList<>();

    private EditText edtBuscar;
    private Button btnBuscar, btnAgregar, btnModificar, btnEliminar;

    private Escuale_db db;

    // 🔵 Instructor seleccionado o encontrado
    private Instructor instructorActual = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        db = Escuale_db.getAppDatabase(this);

        inicializarUI();
        configurarRecycler();
        configurarFiltroEnVivo();
        cargarTodos();
        configurarClicks();
    }

    private void inicializarUI() {
        edtBuscar = findViewById(R.id.edt_buscarNSS);
        btnBuscar = findViewById(R.id.btn_buscar_ins);
        btnAgregar = findViewById(R.id.btn_agregar_ins);
        btnModificar = findViewById(R.id.btn_editar_ins);
        btnEliminar = findViewById(R.id.btn_eliminar_ins);
        recyclerView = findViewById(R.id.lista_ins);

        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void configurarRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapterInstructor(listaInstructores);

        adapter.setOnItemClickListener(instructor -> {
            instructorActual = instructor;
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);
        });

        recyclerView.setAdapter(adapter);
    }

    // 🔵 FILTRO EN VIVO
    private void configurarFiltroEnVivo() {
        edtBuscar.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                instructorActual = null;
                btnModificar.setEnabled(false);
                btnEliminar.setEnabled(false);

                filtrarLista(s.toString());
            }
        });
    }

    private void filtrarLista(String filtro) {
        new Thread(() -> {
            List<Instructor> data;

            if (filtro.isEmpty()) {
                data = db.instructorDAO().mostrarTodos();
            } else {
                data = db.instructorDAO().buscarPorCoincidencia("%" + filtro + "%");
            }

            runOnUiThread(() -> {
                listaInstructores.clear();
                listaInstructores.addAll(data);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void cargarTodos() {
        filtrarLista("");
    }

    private void configurarClicks() {

        btnBuscar.setOnClickListener(v -> {
            String filtro = edtBuscar.getText().toString().trim();

            if (filtro.isEmpty()) {
                Toast.makeText(this, "Ingresa un NSS", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                Instructor resultado = db.instructorDAO().buscarPorNSS(filtro);

                runOnUiThread(() -> {
                    if (resultado == null) {
                        Toast.makeText(this, "No se encontró", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    instructorActual = resultado;

                    listaInstructores.clear();
                    listaInstructores.add(resultado);
                    adapter.notifyDataSetChanged();

                    btnModificar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                });
            }).start();
        });

        btnAgregar.setOnClickListener(v -> {
            startActivity(new Intent(this, AgregarInstructorActivity.class));
        });

        btnModificar.setOnClickListener(v -> {
            if (instructorActual == null) return;

            Intent intent = new Intent(this, EditarInstructorActivity.class);
            intent.putExtra("NSS_INSTRUCTOR", instructorActual.getNSS());
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> {
            if (instructorActual == null) return;

            new Thread(() -> {
                db.instructorDAO().eliminarInstructor(instructorActual);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Instructor eliminado", Toast.LENGTH_SHORT).show();
                    instructorActual = null;
                    btnModificar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                    cargarTodos();
                });
            }).start();
        });

    }
}

abstract class SimpleTextWatcher implements TextWatcher {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void afterTextChanged(Editable s) {}
}

class CustomAdapterInstructor extends RecyclerView.Adapter<CustomAdapterInstructor.ViewHolder> {

    private ArrayList<Instructor> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Instructor instructor);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
        notifyDataSetChanged();
    }

    public CustomAdapterInstructor(ArrayList<Instructor> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfo;

        public ViewHolder(View view, OnItemClickListener listener, ArrayList<Instructor> lista) {
            super(view);
            tvInfo = view.findViewById(R.id.textviewItemInstructor);

            view.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(lista.get(getAdapterPosition()));
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.textview_instructor, parent, false);

        return new ViewHolder(view, listener, lista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Instructor ins = lista.get(position);

        holder.tvInfo.setText(
                ins.getNSS() + " - " +
                        ins.getNombre() + " " +
                        ins.getApellidoPat() + " " +
                        ins.getApellidoMat() + " " +
                        ins.getMatriculaVehiculo()
        );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}