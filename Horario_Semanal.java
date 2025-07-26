import java.util.InputMismatchException;

public class Horario_Semanal extends Horario{
    private Horario_Diario[] horarios_dias = new Horario_Diario[7];

    // Constructor predeterminado
    Horario_Semanal(){
        super();
    }

    // Constructor que toma una String de fecha de creación en formato "dd/mm/aaaa"
    Horario_Semanal(String dia_creacion){
        super(dia_creacion);
    }

    // Constructor que toma un arreglo de horarios diarios
    Horario_Semanal(Horario_Diario[] horarios){
        this();
        if(horarios.length != 7){
            throw new InputMismatchException("Debe haber un horario diario para cada día de la semana");
        }
        horarios_dias = horarios;
    }

    // Constructor que toma tanto la fecha de creación como un arreglo de horarios diarios
    Horario_Semanal(String dia_creacion, Horario_Diario[] horarios){
        // Llamar al constructor que toma el arreglo de horarios
        this(horarios);

        // Procesar la fecha de creación
        set_dia_creacion(dia_creacion);
    }

    // Establece todos los horarios diarios para la semana
    public void set_horarios_dias(Horario_Diario[] horarios){
        if (horarios.length != 7){
            throw new InputMismatchException("Debe haber un horario diario para cada día de la semana");
        }
        horarios_dias = horarios;
    }

    // Establece un horario diario para un día específico de la semana
    public void set_horarios_dias(Horario_Diario horario, int dia){
        if (dia < 0 || dia > 6){
            throw new InputMismatchException("El día debe ser un índice entre 0 y 6");
        }
        horarios_dias[dia] = horario;
    }

    // Obtiene todos los horarios diarios de la semana
    public Horario_Diario[] get_horarios_dias(){
        return horarios_dias;
    }

    // Obtiene el horario diario de un día específico
    public Horario_Diario get_horarios_dias(int dia){
        if (dia < 0 || dia > 6){
            throw new InputMismatchException("El día debe ser un índice entre 0 y 6");
        }
        return horarios_dias[dia];
    }
}