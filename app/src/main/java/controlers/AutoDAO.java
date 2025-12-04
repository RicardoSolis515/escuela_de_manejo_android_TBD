package controlers;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import enteties.Auto;

@Dao
public interface AutoDAO {

    // ALTAS
    @Insert
    void agregarAuto(Auto auto);

    // BAJAS
    @Delete
    void eliminarAuto(Auto auto);

    @Query("DELETE FROM auto WHERE matricula = :matricula")
    void eliminarAutoPorMatricula(String matricula);

    // CAMBIOS
    @Update
    void actualizarAuto(Auto auto);

    @Query("UPDATE auto SET marca = :marca, modelo = :modelo, kilometraje = :km, asignado = :asignado WHERE matricula = :matricula")
    void actualizarAutoPorMatricula(String marca, String modelo, String km, boolean asignado, String matricula);

    // CONSULTAS
    @Query("SELECT * FROM auto")
    List<Auto> mostrarTodos();

    @Query("SELECT * FROM auto WHERE marca = :marca")
    List<Auto> mostrarPorMarca(String marca);

    @Query("SELECT * FROM auto WHERE matricula LIKE :pattern")
    List<Auto> buscarPorMatriculaSimilar(String pattern);

    @Query("SELECT * FROM auto WHERE matricula LIKE :matricula || '%'")
    List<Auto> mostrarPorMatricula(String matricula);

    @Query("SELECT * FROM auto WHERE matricula LIKE '%' || :filtro || '%'")
    List<Auto> buscarPorCoincidencia(String filtro);
}
