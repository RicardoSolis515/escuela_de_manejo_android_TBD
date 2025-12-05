package enteties;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "auto")
public class Auto {

    @PrimaryKey
    @NonNull
    private String matricula;

    private String marca;
    private String modelo;
    private String kilometraje;
    private boolean asignado;

    public Auto(){}

    public Auto(@NonNull String matricula, boolean asignado, String marca, String modelo, String kilometraje) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.kilometraje = kilometraje;
        this.asignado = asignado;
    }

    @NonNull
    public String getMatricula() { return matricula; }

    public void setMatricula(@NonNull String matricula) { this.matricula = matricula; }

    public String getMarca() { return marca; }

    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }

    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getKilometraje() { return kilometraje; }

    public void setKilometraje(String kilometraje) { this.kilometraje = kilometraje; }

    public boolean isAsignado() { return asignado; }

    public void setAsignado(boolean asignado) { this.asignado = asignado; }
}
