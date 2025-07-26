/**
 * Clase de ejemplo para mostrar el uso del método split en Planitarea
 * Demuestra cómo separar strings para el procesamiento de datos
 */
public class EjemploSplit {
    public static void main(String[] args) {
        System.out.println("EJEMPLOS DE USO DE SPLIT EN PLANITAREA");
        System.out.println("=====================================");
        
        // 1. Ejemplo básico con lista de frutas
        String texto = "Manzana,Plátano,Uva,Piña";
        System.out.println("String original: " + texto);
        System.out.println("\nSplit básico usando coma:");
        
        String[] frutas = texto.split(",");
        for (String fruta : frutas) {
            System.out.println("- " + fruta);
        }
        
        // 2. Ejemplo con fecha (relevante para Planitarea)
        System.out.println("\nProcesamiento de fechas:");
        String fecha = "25/12/2023";
        System.out.println("Fecha: " + fecha);
        
        String[] partesFecha = fecha.split("/");
        if (partesFecha.length == 3) {
            int dia = Integer.parseInt(partesFecha[0]);
            int mes = Integer.parseInt(partesFecha[1]);
            int año = Integer.parseInt(partesFecha[2]);
            
            System.out.println("Día: " + dia);
            System.out.println("Mes: " + mes);
            System.out.println("Año: " + año);
            
            // Verificar validez de la fecha
            boolean fechaValida = true;
            if (mes < 1 || mes > 12) {
                fechaValida = false;
            } else if (dia < 1 || dia > 31) {
                fechaValida = false;
            } else if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
                fechaValida = false;
            } else if (mes == 2) {
                // Verificación de año bisiesto simplificada
                boolean esBisiesto = (año % 4 == 0 && año % 100 != 0) || (año % 400 == 0);
                if ((esBisiesto && dia > 29) || (!esBisiesto && dia > 28)) {
                    fechaValida = false;
                }
            }
            
            System.out.println("¿Fecha válida? " + fechaValida);
        }
        
        // 3. Ejemplo con tiempo (relevante para el horario en Planitarea)
        System.out.println("\nProcesamiento de tiempo:");
        String tiempo = "14:30";
        System.out.println("Hora: " + tiempo);
        
        String[] partesHora = tiempo.split(":");
        if (partesHora.length == 2) {
            int horas = Integer.parseInt(partesHora[0]);
            int minutos = Integer.parseInt(partesHora[1]);
            
            System.out.println("Horas: " + horas);
            System.out.println("Minutos: " + minutos);
            
            // Convertir a formato de 12 horas
            String periodo = horas >= 12 ? "PM" : "AM";
            int horas12 = horas > 12 ? horas - 12 : (horas == 0 ? 12 : horas);
            
            System.out.println("Formato 12 horas: " + horas12 + ":" + 
                              (minutos < 10 ? "0" + minutos : minutos) + " " + periodo);
        }
    }
}
