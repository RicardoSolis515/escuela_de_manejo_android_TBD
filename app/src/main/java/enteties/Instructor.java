package enteties;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor")
public class Instructor {

    @PrimaryKey
    @NonNull
    private String NSS;

    private String nombre;
    private String apellidoPat;
    private String apellidoMat;
    private boolean senior;
    private String matriculaVehiculo;

    public Instructor(){}

    public Instructor(String NSS, String nombre, String apellidoPat, String apellidoMat, boolean senior, String matriculaVehiculo) {
        this.NSS = NSS;
        this.nombre = nombre;
        this.apellidoPat = apellidoPat;
        this.apellidoMat = apellidoMat;
        this.senior = senior;
        this.matriculaVehiculo = matriculaVehiculo;
    }

    @NonNull
    public String getNSS(){
        return NSS;
    }

    public void setNSS(@NonNull String NSS){
        this.NSS = NSS;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPat() {
        return apellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        this.apellidoPat = apellidoPat;
    }

    public String getApellidoMat() {
        return apellidoMat;
    }

    public void setApellidoMat(String apellidoMat) {
        this.apellidoMat = apellidoMat;
    }

    public boolean isSenior() {
        return senior;
    }

    public void setSenior(boolean senior) {
        this.senior = senior;
    }

    public String getMatriculaVehiculo() {
        return matriculaVehiculo;
    }

    public void setMatriculaVehiculo(String matriculaVehiculo) {
        this.matriculaVehiculo = matriculaVehiculo;
    }
}
