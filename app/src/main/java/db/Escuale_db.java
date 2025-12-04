package db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import controlers.InstructorDAO;
import controlers.AutoDAO;
import enteties.Auto;
import enteties.Instructor;

@Database(entities = {Instructor.class, Auto.class}, version = 1, exportSchema = false)
public abstract class Escuale_db extends RoomDatabase {

    private static volatile Escuale_db INSTANCE;

    public abstract InstructorDAO instructorDAO();
    public abstract AutoDAO autoDAO();

    public static Escuale_db getAppDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (Escuale_db.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    Escuale_db.class,
                                    "BD_Escuela"
                            )
                            .allowMainThreadQueries() // quitar en producción, usar con ViewModel
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
