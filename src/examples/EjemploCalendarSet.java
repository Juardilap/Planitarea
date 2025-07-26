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
        
        // 3. Comparar fechas (útil para verificar si una tarea está vencida)
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
        
        // 4. Añadir días a una fecha (útil para calcular fechas de vencimiento)
        System.out.println("\nMANIPULACIÓN DE FECHAS:");
        Calendar nuevaFecha = Calendar.getInstance();
        System.out.println("Fecha inicial: " + formatearFecha(nuevaFecha));
        
        nuevaFecha.add(Calendar.DAY_OF_MONTH, 7);  // Añadir una semana
        System.out.println("Fecha después de añadir 7 días: " + formatearFecha(nuevaFecha));
        
        nuevaFecha.add(Calendar.MONTH, 1);  // Añadir un mes
        System.out.println("Fecha después de añadir 1 mes: " + formatearFecha(nuevaFecha));
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

