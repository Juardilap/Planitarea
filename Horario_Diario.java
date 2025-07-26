import java.util.ArrayList;
import java.util.InputMismatchException;

public class Horario_Diario extends Horario{
    private ArrayList<Tiempo_Tarea> tareas = new ArrayList<>();

    Horario_Diario(){
        super();
    }
    // Constructor que recibe una representación en cadena de la fecha de creación.
    Horario_Diario(String dia_creacion){
        super(dia_creacion);
    }

    // Constructor que toma un número variable de objetos Tiempo_Tarea que representan tareas para el día.
    Horario_Diario(Tiempo_Tarea ... tareas){
        this();
        for(Tiempo_Tarea tarea : tareas){
            this.add_tarea(tarea);
        }
    }

    // Constructor que toma un número variable de objetos Tiempo_Tarea que representan tareas para el día.
    Horario_Diario(String dia_creacion, Tiempo_Tarea ... tareas){
        this(dia_creacion); // Llama al constructor que toma una representación en cadena de la fecha de creación
        for(Tiempo_Tarea tarea : tareas){
            this.add_tarea(tarea);
        }
    }

    // Retorna la lista de tareas programadas para el día.
    public ArrayList<Tiempo_Tarea> get_tareas_existentes(){
        return tareas;
    }

    // Agrega una nueva tarea al horario.
    public void add_tarea(Tiempo_Tarea nueva) throws InputMismatchException{
        for(Tiempo_Tarea t : tareas){
            //Comprueba si la hora de inicio de la nueva tarea está dentro del rango de tiempo de una tarea existente.
            if(t.get_hora_inicio() <= nueva.get_hora_inicio() && nueva.get_hora_inicio() < t.get_hora_fin()){ //t.inicio <= nueva.inicio < t.fin
                throw new InputMismatchException("La nueva tarea entra en el rango de tiempo de otra");
            }else if(t.get_hora_inicio() < nueva.get_hora_fin() && nueva.get_hora_fin() <= t.get_hora_fin()){ //t.inicio < nueva.fin <= t.fin
                throw new InputMismatchException("La nueva tarea entra en el rango de tiempo de otra");
            }
        }
        tareas.add(nueva); // Agrega la nueva tarea a la lista de tareas.
    }

    // Elimina una tarea por su nombre y retorna el objeto Tarea eliminado.
    public Tarea quitarTarea(String nombre_tarea){
        for(Tiempo_Tarea t : tareas){
            if (t.get_tarea().get_nombre().equals(nombre_tarea)){
                tareas.remove(t);
                return t.get_tarea();
            }
        }
        return null;
    }
}