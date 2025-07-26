import java.util.Scanner;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main{
    private static ArrayList<Tarea> tareas_existentes = new ArrayList<>();
    private static ArrayList<String> archivo_tareas_str = new ArrayList<>();
    private static ArrayList<String> archivo_horarios_str = new ArrayList<>();
    private static Horario_Diario horario_dia;
    private static Horario_Semanal horario_actual;
    private static Horario_Semanal horario_proximo;
    private static File archivo_tareas = new File("./tareas.json");
    private static File archivo_horarios = new File("./horarios.json");
    private static Scanner input;
    private static String mensaje_inicial = "";
    private static double porcentaje = 0.0;
    public static void main(String[] args){
        try{
            //TODO Guardar horarios actualizados en el archivo
            //TODO Si una tarea se completa, quitarla del horario
            //TODO Una forma de darse cuenta si el horario está vacío porque no se ingresó nada
            //TODO Advertencia de que se va a vencer una tarea o que ya se venció y dar la opción de alargar su fecha de vencimiento
            //TODO Sería bueno que las cosas en el archivo se guarden en el orden dado por tareas_existentes.sort(), pero no es necesario
            //TODO Sería bueno añadir un log para errores que sucedan durante la ejecución (posiblemente designar a Juan Pablo)
            input = new Scanner(archivo_tareas);
            while(input.hasNextLine()){
                archivo_tareas_str.add(input.nextLine());
            }
            for(int i=0; i<archivo_tareas_str.size(); i++){
                if(archivo_tareas_str.get(i).contains("Tarea")){
                    i = leer_tarea(i+1);
                }
            }
            tareas_existentes.sort(null);
            calcular_porcentaje();
            /*
            //Comentado mientras hago la funcionalidad de los horarios
            try{
                input = new Scanner(archivo_horarios);
                while(input.hasNextLine()){
                    archivo_horarios_str.add(input.nextLine());
                }

                for(int i=0; i<archivo_horarios_str.size(); i++){
                    if(archivo_horarios_str.get(i).contains("HorarioSemana") && i+1 < archivo_horarios_str.size() && archivo_horarios_str.get(i+1).contains("dia_creacion")){
                        i = leer_horario_semana(i+1);
                    }else if(archivo_horarios_str.get(i).contains("HorarioDia") && i+1 < archivo_horarios_str.size() && archivo_horarios_str.get(i+1).contains("dia_creacion")){
                        i++;
                        String dia_creacion = archivo_horarios_str.get(i).replaceFirst("\"(.*)\":\"","").replace("\",", "").trim();
                        //TODO Que no se cree el horario cuando ya pasó la fecha de creación
                        //Hacer acá, si se hace dentro de leer_horario_diario, rompería los horarios semanales
                        Integer i_Integer = i+1;
                        horario_dia = leer_horario_diario(i_Integer, dia_creacion);
                        i = i_Integer;
                    }
                }
            }catch(FileNotFoundException e){
                //TODO A este default puede que le falten cosas
                archivo_horarios_str.add("{"); 
                archivo_horarios_str.add("}");
            }catch(NumberFormatException e){
                mensaje_inicial = "El archivo horarios.json ha sido modificado inesperadamente y no se ha podido recuperar la información de los horarios";
            }
            */
        }catch(FileNotFoundException e){
            archivo_tareas_str.add("{"); 
            archivo_tareas_str.add("}");
        }finally{
            input.close();
        }

        Planitarea programa = new Planitarea(mensaje_inicial);
        programa.setVisible(true);
    }
    private static int leer_tarea(int i){
        Tarea nueva = null;
        String atributos[] = new String[4];
        boolean completa;
        for(int j=0; j<4; j++){
            atributos[j] = archivo_tareas_str.get(i).replaceFirst("\"(.*)\":", "").trim();  //Se ignora la información inicial
            atributos[j] = atributos[j].substring(1, atributos[j].length()-2);  //Se quitan las comillas alrededor del valor
            i++;
        }        
        completa = archivo_tareas_str.get(i).replaceFirst("\"(.*)\":", "").trim().equals("true,");
        i++;
        try{
            switch(Integer.parseInt(archivo_tareas_str.get(i).replaceFirst("\"(.*)\":", "").trim())){
                case 1:
                    nueva = new ImportanteUrgente(atributos[0], atributos[1], atributos[2], atributos[3], completa);
                    break;
                case 2:
                    nueva = new ImportanteNoUrgente(atributos[0], atributos[1], atributos[2], atributos[3], completa);
                    break;
                case 3:
                    nueva = new NoImportanteUrgente(atributos[0], atributos[1], atributos[2], atributos[3], completa);
                    break;
                case 4:
                    nueva = new NiImportanteNiUrgente(atributos[0], atributos[1], atributos[2], atributos[3], completa);
                    break;
                default:
                    throw new NumberFormatException();
            }
            i++;
        }catch(NumberFormatException | NoSuchElementException e){
            mensaje_inicial = "El archivo tareas.json ha sido modificado inesperadamente y no se ha podido recuperar la información de todas las tareas";
        }
        if(nueva != null){
            tareas_existentes.add(nueva);
        }
        return i;
    }
    private static int leer_horario_semana(int i){
        //Retorna la posición de justo antes de iniciar un horario nuevo, porque el for lo mueve uno hacia adelante
        Horario_Semanal aux_horario = new Horario_Semanal(archivo_horarios_str.get(i).replaceFirst("\"(.*)\":\"","").replace("\",", "").trim());
        //TODO Hacer que no se cree cuando ya pasó la semana a la que se refiere (dependiendo de si es horario_actual u horario_proximo)
        Integer i_Integer = i+1;
        for(int j=0; j<7; j++){
            aux_horario.set_horarios_dias(leer_horario_diario(i_Integer, aux_horario.get_dia_creacion(true)), j);
        }
        if(horario_actual == null){
            horario_actual = aux_horario;
        }else{
            horario_proximo = aux_horario;
        }
        return i_Integer;
    }
    private static Horario_Diario leer_horario_diario(Integer i, String dia_creacion){
        //"i" inicia con la posición del "nombre" del "objeto" horario en el archivo
        //"i" termina con la posición de iniciar un horario nuevo
        
        Horario_Diario horario_respuesta = new Horario_Diario(dia_creacion);
        
        Tarea aux_tarea;
        String hora_inicio, hora_fin;
        for(int j=++i; j<archivo_horarios_str.size(); j++){
            if(archivo_horarios_str.get(j).contains("tiempo_tarea")){
                j++;
                aux_tarea = buscar_tarea(archivo_horarios_str.get(j++).replaceFirst("\"(.*)\":\"", "").replace("\",", "").trim());
                hora_inicio = archivo_horarios_str.get(j++).replaceFirst("\"(.*)\":\"", "").replace("\",", "").trim();
                hora_fin = archivo_horarios_str.get(j++).replaceFirst("\"(.*)\":\"", "").replace("\",", "").trim();

                horario_respuesta.add_tarea(new Tiempo_Tarea(aux_tarea, hora_inicio, hora_fin));
            }else if(archivo_horarios_str.get(j).contains("}")){
                i = j+1;
                break;
            }
        }

        return horario_respuesta;
    }
    public static Tarea buscar_tarea(String nombre){
        Tarea resultado = null;
        for(int i=0; i<tareas_existentes.size(); i++){
            if(tareas_existentes.get(i).get_nombre().equals(nombre)){
                resultado = tareas_existentes.get(i);
                break;
            }
        }
        return resultado;
    }
    public static boolean nueva_tarea(Tarea tarea){
        ArrayList<String> tarea_temporal = new ArrayList<>(8);
        int eisenhower = 0;

        if(tarea instanceof ImportanteUrgente) eisenhower = 1;
        else if(tarea instanceof ImportanteNoUrgente) eisenhower = 2;
        else if(tarea instanceof NoImportanteUrgente) eisenhower = 3;
        else if(tarea instanceof NiImportanteNiUrgente) eisenhower = 4;

        if(archivo_tareas_str.get(archivo_tareas_str.size()-2).contains("}")) archivo_tareas_str.set(archivo_tareas_str.size()-2, "\t},");
        tarea_temporal.add("\t\"Tarea" + (tareas_existentes.size()+1) + "\":{");
        tarea_temporal.add("\t\t\"nombre\":\"" + tarea.get_nombre() + "\",");
        tarea_temporal.add("\t\t\"descripcion\":\"" + tarea.get_descripcion() + "\",");
        tarea_temporal.add("\t\t\"finalidad\":\"" + tarea.get_finalidad() + "\",");
        tarea_temporal.add("\t\t\"vencimiento\":\"" + tarea.get_vencimiento() + "\",");
        tarea_temporal.add("\t\t\"completa\":" + tarea.get_completa() + ",");
        tarea_temporal.add("\t\t\"Eisenhower\":" + eisenhower + "");
        tarea_temporal.add("\t}");

        archivo_tareas_str.addAll(archivo_tareas_str.size()-1, tarea_temporal);
        
        boolean respuesta = actualizar_archivo_tareas();
        if(respuesta){
            //Se usa Binary Search para buscar la posición adecuada donde poner la tarea
            int izquierda = 0, medio = tareas_existentes.size()/2, derecha = tareas_existentes.size()-1;
            while(izquierda+1 != derecha){
                if(tareas_existentes.get(medio).compareTo(tarea) < 0){
                    izquierda = medio;
                }else if(tareas_existentes.get(medio).compareTo(tarea) > 0){
                    derecha = medio;
                }else{
                    tareas_existentes.add(medio, tarea);
                    break;
                }
                medio = (derecha+izquierda)/2;
            }
            if(izquierda+1 == derecha) tareas_existentes.add(derecha, tarea);
            calcular_porcentaje();
        }
        return respuesta;
    }
    public static boolean actualizar_tarea(Tarea modificar){
        for(int i=0; i<tareas_existentes.size(); i++){
            if(modificar.get_nombre().equals(tareas_existentes.get(i).get_nombre())){
                tareas_existentes.set(i, modificar);
                tareas_existentes.sort(null);
                calcular_porcentaje();
                break;
            }
        }
        for(int i=0; i<archivo_tareas_str.size(); i++){
            if(archivo_tareas_str.get(i).equals("\t\t\"nombre\":\"" + modificar.get_nombre() + "\",")){
                archivo_tareas_str.set(i+1, "\t\t\"descripcion\":\"" + modificar.get_descripcion() + "\",");
                archivo_tareas_str.set(i+2, "\t\t\"finalidad\":\"" + modificar.get_finalidad() + "\",");
                archivo_tareas_str.set(i+3, "\t\t\"vencimiento\":\"" + modificar.get_vencimiento() + "\",");
                archivo_tareas_str.set(i+4, "\t\t\"completa\":" + modificar.get_completa() + ",");
                break;
            }
        }
        return actualizar_archivo_tareas();
    }
    public static boolean actualizar_archivo_tareas(){ //Retorna true si se actualizó correctamente
        boolean respuesta = false;
        while(true){
            try{
                PrintWriter sobreescribir = new PrintWriter(archivo_tareas);
                
                for(int i=0; i<archivo_tareas_str.size(); i++){
                    sobreescribir.println(archivo_tareas_str.get(i));
                }

                sobreescribir.close();
                respuesta = true;
                break;
            }catch(FileNotFoundException e){
                try{
                    archivo_tareas.createNewFile();
                    continue;
                }catch(IOException error){
                    respuesta = false;
                    break;
                }
            }
        }
        return respuesta;
    }
    public static Tarea[] get_tareas_existentes(){
        Tarea[] resultado = new Tarea[tareas_existentes.size()];
        for(int i=0; i<resultado.length; i++){
            resultado[i] = tareas_existentes.get(i);
        }
        return resultado;
    }
    public static Horario_Diario get_horario_dia(){
        return horario_dia;
    }
    public static Horario_Semanal get_horario_actual(){
        return horario_actual;
    }
    public static Horario_Semanal get_horario_proximo(){
        return horario_proximo;
    }
    private static void calcular_porcentaje(){
        int contador = 0;
        porcentaje = 0.0;
        for(int i=0; i<tareas_existentes.size(); i++){
            if(!tareas_existentes.get(i).get_completa()){
                contador++;
            }else{
                break;
            }
        }
        porcentaje = tareas_existentes.size()>0 ? 100*(1 - (double)contador/tareas_existentes.size()) : 0.0;
    }
    public static double get_porcentaje(){
        return porcentaje;
    }
}