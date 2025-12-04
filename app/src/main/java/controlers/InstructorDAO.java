package controlers;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import enteties.Instructor;

@Dao
public interface InstructorDAO {

    // ALTAS
    @Insert
    void agregarInstructor(Instructor instructor);

    // BAJAS
    @Delete
    void eliminarInstructor(Instructor instructor);

    @Query("DELETE FROM instructor WHERE NSS = :nss")
    void eliminarInstructorPorNSS(String nss);

    // CAMBIOS
    @Update
    void actualizarInstructor(Instructor instructor);

    @Query("UPDATE instructor SET nombre = :nombre, apellidoPaterno = :apPat, apellidoMaterno = :apMat, senior = :senior, matriculaVehiculo = :matriculaVehiculo WHERE NSS = :nss")
    void actualizarInstructorPorNSS(String nss, String nombre, String apPat, String apMat, boolean senior, String matriculaVehiculo);

    // CONSULTAS
    @Query("SELECT * FROM instructor")
    List<Instructor> mostrarTodos();

    @Query("SELECT * FROM instructor WHERE nombre = :nombre")
    List<Instructor> mostrarPorNombre(String nombre);

    @Query("SELECT * FROM instructor WHERE NSS LIKE :pattern")
    List<Instructor> buscarPorNSSSimilar(String pattern);

    @Query("SELECT * FROM instructor WHERE NSS LIKE :nss || '%'")
    List<Instructor> mostrarPorNSS(String nss);

    @Query("SELECT * FROM instructor WHERE NSS LIKE '%' || :filtro || '%'")
    List<Instructor> buscarPorCoincidencia(String filtro);
}
