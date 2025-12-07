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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import db.Escuale_db;
import enteties.Auto;

public class AutoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAutos;
    private AutoAdapter adapter;
    private EditText filtroAuto;
    private Button btnAgregar, btnEditar, btnEliminar, btnBuscar;

    private Escuale_db baseDatos;

    private Auto autoActual = null;  // 🔵 Auto seleccionado o encontrado

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);

        baseDatos = Escuale_db.getAppDatabase(this);

        inicializarUI();
        configurarRecycler();
        configurarFiltroEnVivo();
        cargarAutos();
        configurarClicks();
    }

    private void inicializarUI() {
        recyclerViewAutos = findViewById(R.id.lista_auto);
        filtroAuto = findViewById(R.id.filtro_auto);
        btnAgregar = findViewById(R.id.btn_agregar_auto);
        btnEditar = findViewById(R.id.btn_editar_auto);
        btnEliminar = findViewById(R.id.btn_eliminar_auto);
        btnBuscar = findViewById(R.id.btn_buscar_auto);

        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void configurarRecycler() {
        recyclerViewAutos.setLayoutManager(new LinearLayoutManager(this));
    }

    // 🔵 FILTRO EN VIVO
    private void configurarFiltroEnVivo() {
        filtroAuto.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                autoActual = null;
                btnEditar.setEnabled(false);
                btnEliminar.setEnabled(false);

                filtrarAutos(s.toString());
            }
        });
    }

    private void configurarClicks() {

        btnAgregar.setOnClickListener(v -> {
            startActivity(new Intent(this, AgregarAutoActivity.class));
        });

        btnEditar.setOnClickListener(v -> {
            if (autoActual == null) {
                Toast.makeText(this, "Seleccione un auto", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, EditarAutoActivity.class);
            intent.putExtra("matricula", autoActual.getMatricula());
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> eliminarAuto());

        btnBuscar.setOnClickListener(v -> {
            String texto = filtroAuto.getText().toString().trim();

            if (texto.isEmpty()) {
                Toast.makeText(this, "Ingresa una matrícula", Toast.LENGTH_SHORT).show();
                return;
            }

            Auto resultado = baseDatos.autoDAO().mostrarUnico(texto);

            if (resultado == null) {
                Toast.makeText(this, "No se encontró el auto", Toast.LENGTH_SHORT).show();
                return;
            }

            autoActual = resultado;

            ArrayList<Auto> unicaLista = new ArrayList<>();
            unicaLista.add(resultado);

            adapter.actualizarLista(unicaLista);

            btnEditar.setEnabled(true);
            btnEliminar.setEnabled(true);
        });
    }

    private void eliminarAuto() {
        if (autoActual == null) return;

        baseDatos.autoDAO().eliminarAuto(autoActual);
        Toast.makeText(this, "Auto eliminado", Toast.LENGTH_SHORT).show();

        autoActual = null;
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);

        cargarAutos();
    }

    private void cargarAutos() {
        autoActual = null;
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);

        List<Auto> listaBD = baseDatos.autoDAO().mostrarTodos();

        adapter = new AutoAdapter(new ArrayList<>(listaBD));
        adapter.setOnItemClickListener(auto -> {
            autoActual = auto;
            btnEditar.setEnabled(true);
            btnEliminar.setEnabled(true);
        });

        recyclerViewAutos.setAdapter(adapter);
    }

    private void filtrarAutos(String texto) {
        List<Auto> filtrados = baseDatos.autoDAO().buscarPorCoincidencia(texto);

        if (filtrados == null)
            filtrados = new ArrayList<>();

        adapter.actualizarLista(new ArrayList<>(filtrados));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarAutos();
    }
}





class AutoAdapter extends RecyclerView.Adapter<AutoAdapter.ViewHolder> {

    private ArrayList<Auto> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Auto auto);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textoAuto;

        public ViewHolder(@NonNull View view) {
            super(view);
            textoAuto = view.findViewById(R.id.txt_item_auto);
        }
    }

    public AutoAdapter(ArrayList<Auto> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.textview_auto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Auto a = lista.get(position);

        holder.textoAuto.setText(
                a.getMatricula() + " - " +
                        a.getMarca() + " " +
                        a.getModelo() + " | " +
                        a.getKilometraje() + " km"
        );

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                listener.onItemClick(lista.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizarLista(ArrayList<Auto> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }
}
