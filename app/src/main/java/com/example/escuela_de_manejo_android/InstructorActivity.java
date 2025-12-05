package com.example.escuela_de_manejo_android;

import android.os.Bundle;
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

    private Instructor instructorSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        db = Escuale_db.getAppDatabase(this);

        inicializarUI();
        configurarRecycler();
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

        // Evento al seleccionar un instructor
        adapter.setOnItemClickListener(instructor -> {
            instructorSeleccionado = instructor;
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);
        });

        recyclerView.setAdapter(adapter);
    }

    private void cargarTodos() {
        List<Instructor> data = db.instructorDAO().mostrarTodos();
        listaInstructores.clear();
        listaInstructores.addAll(data);
        adapter.notifyDataSetChanged();
    }

    private void configurarClicks() {

        btnBuscar.setOnClickListener(v -> {
            String filtro = edtBuscar.getText().toString().trim();
            if (filtro.isEmpty()) {
                cargarTodos();
            } else {
                List<Instructor> result = db.instructorDAO().buscarPorCoincidencia(filtro);
                listaInstructores.clear();
                listaInstructores.addAll(result);
                adapter.notifyDataSetChanged();
            }
        });

        btnAgregar.setOnClickListener(v -> {
            // Ejemplo rápido: agregar instructor de prueba
            Instructor nuevo = new Instructor("12345", "Nuevo", "Instructor", "", false, null);
            db.instructorDAO().agregarInstructor(nuevo);
            Toast.makeText(this, "Instructor agregado", Toast.LENGTH_SHORT).show();
            cargarTodos();
        });

        btnModificar.setOnClickListener(v -> {
            if (instructorSeleccionado == null) return;

            // Ejemplo básico de edición
            instructorSeleccionado.setNombre("Editado");
            db.instructorDAO().actualizarInstructor(instructorSeleccionado);

            Toast.makeText(this, "Instructor actualizado", Toast.LENGTH_SHORT).show();
            cargarTodos();
        });

        btnEliminar.setOnClickListener(v -> {
            if (instructorSeleccionado == null) return;

            db.instructorDAO().eliminarInstructor(instructorSeleccionado);
            Toast.makeText(this, "Instructor eliminado", Toast.LENGTH_SHORT).show();
            instructorSeleccionado = null;

            btnModificar.setEnabled(false);
            btnEliminar.setEnabled(false);

            cargarTodos();
        });
    }
}

class CustomAdapterInstructor extends RecyclerView.Adapter<CustomAdapterInstructor.ViewHolder> {

    private ArrayList<Instructor> lista;

    // Click Listener opcional para seleccionar un instructor
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Instructor instructor);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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

    public CustomAdapterInstructor(ArrayList<Instructor> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public CustomAdapterInstructor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.textview_instructor, parent, false);
        return new ViewHolder(view, listener, lista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Instructor ins = lista.get(position);
        holder.tvInfo.setText(ins.getNSS()
                + " - " + ins.getNombre()
                + " " + ins.getApellidoPat()
                + " " + ins.getApellidoMat()
                + " " + ins.getMatriculaVehiculo());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // Método para refrescar la lista
    public void actualizarLista(ArrayList<Instructor> nuevaLista) {
        lista = nuevaLista;
        notifyDataSetChanged();
    }
}
