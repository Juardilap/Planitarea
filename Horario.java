import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.InputMismatchException;

public abstract class Horario{
    protected Calendar dia_creacion;

    Horario(){
        dia_creacion = Calendar.getInstance();
    }
    Horario(String dia_creacion){
        set_dia_creacion(dia_creacion);
    }

    protected void set_dia_creacion(String dia_creacion){
        // Divide la cadena de fecha en día, mes y año.
        String[] partes = dia_creacion.split("/");
        try{
            if(partes.length != 3){
                // Si el formato de fecha es incorrecto, lanza una excepción.
                throw new NumberFormatException();
            }

            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]) - 1; // Los meses en Calendar comienzan en 0
            int anio = Integer.parseInt(partes[2]);

            // Valida que el mes esté en el rango de 1 a 12
            if(mes < 0 || mes > 11){
                throw new InputMismatchException("Mes fuera de rango (debe ser entre 1 y 12).");
            }

            // Valida el día en función del mes
            int maxDias = 31; // Máximo de 31 días por defecto
            if(mes == 3 || mes == 5 || mes == 8 || mes == 10){
                maxDias = 30; // Meses con 30 días
            }else if (mes == 1){
                // Febrero: 28 o 29 días (verificación de año bisiesto)
                if(anio % 4 == 0 && (anio % 100 != 0 || anio % 400 == 0)){
                    maxDias = 29;
                } else{
                    maxDias = 28;
                }
            }

            // Verifica si el día está dentro del rango válido (1 a maxDias)
            if(dia < 1 || dia > maxDias){
                throw new InputMismatchException("Día fuera de rango para el mes especificado.");
            }

            // Crea un objeto Calendar con la fecha de creación.
            this.dia_creacion = Calendar.getInstance();
            this.dia_creacion.set(anio, mes, dia);
        }catch(NumberFormatException e){
            // Maneja excepciones en caso de error en la conversión.
            throw new InputMismatchException("Formato de fecha de creación incorrecto.");
        }
    }

    // Retorna la fecha de creación del horario.
    public Calendar get_dia_creacion(){
        return dia_creacion;
    }
    public String get_dia_creacion(boolean retornar_como_string){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(dia_creacion.getTime());
    }
}