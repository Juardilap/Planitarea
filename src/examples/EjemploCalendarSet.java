import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Clase de ejemplo para mostrar el uso de Calendar en Planitarea
 * Demuestra diferentes formas de manipular fechas útiles para la gestión de tareas
 */
public class EjemploCalendarSet {
    public static void main(String[] args) {
        System.out.println("EJEMPLOS DE USO DE CALENDAR EN PLANITAREA");
        System.out.println("=========================================");
        
        // 1. Obtener la fecha actual
        Calendar hoy = Calendar.getInstance();
        System.out.println("Fecha actual: " + formatearFecha(hoy));
        
        // 2. Establecer una fecha específica para vencimiento de tarea
        Calendar vencimiento = Calendar.getInstance();
        vencimiento.set(2023, Calendar.DECEMBER, 31);
        System.out.println("Fecha de vencimiento: " + formatearFecha(vencimiento));
        
        // 3. Modificar componentes individuales de una fecha
        Calendar fechaPersonalizada = Calendar.getInstance();
        // Establecer año, mes y día
        fechaPersonalizada.set(Calendar.YEAR, 2024);
        fechaPersonalizada.set(Calendar.MONTH, Calendar.MARCH);
        fechaPersonalizada.set(Calendar.DAY_OF_MONTH, 15);
        System.out.println("Fecha personalizada: " + formatearFecha(fechaPersonalizada));
        
        // 4. Comparar fechas (útil para verificar si una tarea está vencida)
        System.out.println("\nCOMPARACIÓN DE FECHAS:");
        if (hoy.before(vencimiento)) {
            System.out.println("La tarea aún no está vencida");
            
            // Calcular días restantes
            long milisHoy = hoy.getTimeInMillis();
            long milisVencimiento = vencimiento.getTimeInMillis();
            long diferencia = milisVencimiento - milisHoy;
            int diasRestantes = (int)(diferencia / (1000 * 60 * 60 * 24));
            
            System.out.println("Faltan " + diasRestantes + " días para el vencimiento");
        } else {
            System.out.println("¡La tarea está vencida!");
        }
        
        // 5. Añadir o restar unidades de tiempo (útil para calcular fechas de vencimiento)
        System.out.println("\nMANIPULACIÓN DE FECHAS:");
        Calendar nuevaFecha = Calendar.getInstance();
        System.out.println("Fecha inicial: " + formatearFecha(nuevaFecha));
        
        nuevaFecha.add(Calendar.DAY_OF_MONTH, 7);  // Añadir una semana
        System.out.println("Fecha después de añadir 7 días: " + formatearFecha(nuevaFecha));
        
        nuevaFecha.add(Calendar.MONTH, 1);  // Añadir un mes
        System.out.println("Fecha después de añadir 1 mes: " + formatearFecha(nuevaFecha));
        
        nuevaFecha.add(Calendar.DAY_OF_MONTH, -14);  // Restar 14 días
        System.out.println("Fecha después de restar 14 días: " + formatearFecha(nuevaFecha));
        
        // 6. Obtener el día de la semana (útil para horarios semanales)
        System.out.println("\nOBTENER DÍA DE LA SEMANA:");
        String[] diasSemana = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        int diaSemana = hoy.get(Calendar.DAY_OF_WEEK) - 1; // Calendar usa 1-7, nosotros 0-6
        System.out.println("Hoy es " + diasSemana[diaSemana]);
        
        // 7. Configurar horario (útil para el temporizador Pomodoro)
        System.out.println("\nMANIPULACIÓN DE HORAS:");
        Calendar horario = Calendar.getInstance();
        horario.set(Calendar.HOUR_OF_DAY, 14); // 24h format
        horario.set(Calendar.MINUTE, 30);
        horario.set(Calendar.SECOND, 0);
        
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        System.out.println("Hora configurada: " + formatoHora.format(horario.getTime()));
        
        // Añadir 25 minutos (una sesión Pomodoro)
        horario.add(Calendar.MINUTE, 25);
        System.out.println("Después de una sesión Pomodoro: " + formatoHora.format(horario.getTime()));
    }

    /**
     * Formatea una fecha Calendar en formato dd/MM/yyyy
     * @param calendar La fecha a formatear
     * @return String formateado de la fecha
     */
    private static String formatearFecha(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(calendar.getTime());
    }
}

