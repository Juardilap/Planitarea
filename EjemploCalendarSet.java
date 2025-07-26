import java.util.Calendar;

public class EjemploCalendarSet {
    public static void main(String[] args) {
        // Obtener una instancia de Calendar con la fecha y hora actual
        Calendar calendario1 = Calendar.getInstance();

        // Forma 1: Usar set para establecer una fecha específica
        calendario1.set(2023, Calendar.OCTOBER, 15);

        // Forma 2: Usar set para establecer una fecha específica
        int anio = 2023;
        int mes = 10;  // octubre
        int dia = 15;
        Calendar calendario2 = Calendar.getInstance();
        calendario2.set(anio, mes - 1, dia);

        // Mostrar las fechas establecidas
        System.out.println("Fecha establecida (Forma 1): " + formatearFecha(calendario1));
        System.out.println("Fecha establecida (Forma 2): " + formatearFecha(calendario2));
    }

    // Método para formatear y mostrar la fecha
    private static String formatearFecha(Calendar calendar) {
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1; // Agregar 1 porque el mes se cuenta desde 0
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        return dia + "/" + mes + "/" + año;
    }
}

