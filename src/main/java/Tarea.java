import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;

abstract class Tarea implements Comparable<Tarea>{
    protected String nombre;
    protected String descripcion;
    protected String finalidad;
    protected Calendar vencimiento;
    protected boolean completa = false;

    // Constructor de Tarea con nombre y vencimiento
    public Tarea(String nombre, String vencimiento){
        set_nombre(nombre);
        set_vencimiento(vencimiento);
    }

    // Constructor de Tarea con nombre, vencimiento y completa
    public Tarea(String nombre, String vencimiento, boolean completa) throws InputMismatchException {
        this(nombre, vencimiento);
        set_completa(completa);
    }

    // Constructor de Tarea con nombre, descripción, finalidad y vencimiento
    public Tarea(String nombre, String descripcion, String finalidad, String vencimiento){
        this(nombre, vencimiento);
        set_descripcion(descripcion);
        set_finalidad(finalidad);
    }

    // Constructor de Tarea con nombre, descripción, finalidad, vencimiento y completa
    public Tarea(String nombre, String descripcion, String finalidad, String vencimiento, boolean completa){
        this(nombre, descripcion, finalidad, vencimiento);
        set_completa(completa);
    }

    //Constructor de Tarea que crea un clon de un objeto Tarea
    public Tarea(Tarea origen){
        this(origen.nombre, origen.descripcion, origen.finalidad, origen.get_vencimiento(), origen.completa);
    }

    // Método setter nombre de la tarea
    private void set_nombre(String nombre) throws InputMismatchException{
        if (nombre.trim().isEmpty()){
            // Si el nombre está vacío, solicita al usuario que ingrese un nombre válido
            throw new InputMismatchException("El nombre de la tarea no puede estar vacío");
        }
        this.nombre = nombre;
    }

    // Método setter descripción de la tarea
    public void set_descripcion(String descripcion){
        this.descripcion = descripcion.trim();
    }

    // Método setter finalidad de la tarea
    public void set_finalidad(String finalidad){
        this.finalidad = finalidad.trim();
    }

    // Método setter fecha de vencimiento de la tarea
    public void set_vencimiento(String vencimiento) throws InputMismatchException{
        do {
            try {
                // Divide el String de fecha en día, mes y año
                String[] partesFecha = vencimiento.split("/");
                if (partesFecha.length != 3){
                    // Si el formato de fecha es incorrecto, muestra un mensaje de error y solicita
                    // una fecha válida
                    throw new NumberFormatException();
                }

                // Convierte las partes de la fecha de Strings a int
                int dia = Integer.parseInt(partesFecha[0]);
                int mes = Integer.parseInt(partesFecha[1]);
                int anio = Integer.parseInt(partesFecha[2]);

                //Si la fecha no tiene sentido, triggerea el catch, que avisa al programa principal del error
                if((mes < 1 || mes > 12 || dia < 1 || dia > 32) || ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && (dia > 30)) || (mes == 2 && (dia > 29 || (!new GregorianCalendar().isLeapYear(anio) && dia > 28)))){
                    throw new NumberFormatException();
                }

                // Crea un objeto Calendar
                Calendar calendar = Calendar.getInstance();

                // Establece el día, el mes y el año en el objeto Calendar
                calendar.set(anio, mes - 1, dia); // Mes es 0-based en Calendar
                if(calendar.compareTo(Calendar.getInstance()) <= 0){
                    throw new InputMismatchException("Por favor ingresa una fecha posterior a la de hoy");
                }

                this.vencimiento = calendar;
            } catch (NumberFormatException e){
                throw new InputMismatchException("Por favor ingresa una fecha válida");
            }
        } while (vencimiento == null || vencimiento.trim().isEmpty());
    }

    // Método setter tarea completa
    public void set_completa(boolean completa){
        this.completa = completa;
    }

    // Método getter nombre de la tarea
    public String get_nombre(){
        return nombre;
    }

    // Método getter descripción
    public String get_descripcion(){
        return descripcion;
    }

    // Método getter finalidad
    public String get_finalidad(){
        return finalidad;
    }

    // Método getter fecha de vencimiento en formato "dd/mm/aaaa"
    public String get_vencimiento(){
        /*
         * Se crea una instancia de SimpleDateFormat, que es una clase que permite
         * formatear fechas y horas en diferentes formatos.
         * En este caso, se especifica el formato deseado "dd/mm/aaaa" para que el
         * objeto formato pueda formatear la fecha en ese formato
         */
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        /*
         * se toma la fecha de vencimiento (vencimiento) y se llama al método getTime()
         * para obtener un objeto Date.
         * Luego, se utiliza el objeto formato para formatear la fecha en el formato
         * "dd/mm/aaaa" y devuelve la fecha formateada como una cadena de texto
         */
        return formato.format(vencimiento.getTime());
    }

    // Método getter tarea completa
    public boolean get_completa(){
        return completa;
    }

    @Override public int compareTo(Tarea otra){
        //En importancia se toma principalmente la fecha de vencimiento, porque se quiere que se muestren las tareas en ese orden
        //1 significa que "otra" es el más importante, -1 que "otra" es el menos importante
        int resultado;
        if(this.completa == otra.completa){
            if(this.vencimiento.compareTo(otra.vencimiento) > 0){
                resultado = 1;
            }else if(this.vencimiento.compareTo(otra.vencimiento) == 0){
                //Si está clasificado en la matriz de Eisenhower como "Importante", es más importante que uno que no esté clasificado ahí, lo mismo con "Urgente"
                if(this.getClass() == otra.getClass()){
                    resultado = 0;
                }else if(otra instanceof ImportanteUrgente || this instanceof NiImportanteNiUrgente){
                    resultado = 1; 
                }else if(this instanceof ImportanteUrgente || otra instanceof NiImportanteNiUrgente){
                    resultado = -1;
                }else if(otra instanceof ImportanteNoUrgente){
                    resultado = 1;
                }else{
                    resultado = -1;
                }
            }else{
                resultado = -1;
            }
        }else if(this.completa){
            resultado = 1;
        }else{
            resultado = -1;
        }
        return resultado;
    }

    public String toString(boolean mostrar_fecha){
        String resultado = "";
        resultado += "Nombre: " + nombre;
        if(!descripcion.equals("")){
            resultado += "\nDescripción: " + descripcion;
        }
        if(!finalidad.equals("")){
            resultado += "\nFinalidad: " + finalidad;
        }
        if(mostrar_fecha){
            resultado += "\nFecha de vencimiento: " + get_vencimiento();
        }
        return resultado;
    }
    public String toString(){
        return toString(true);
    }
}

class ImportanteUrgente extends Tarea {

    public ImportanteUrgente(String nombre, String vencimiento) throws InputMismatchException {
        super(nombre, vencimiento);
    }

    public ImportanteUrgente(String nombre, String descripcion, String finalidad, String vencimiento){
        super(nombre, descripcion, finalidad, vencimiento);
    }

    public ImportanteUrgente(String nombre, String descripcion, String finalidad, String vencimiento,
            boolean completa){
        super(nombre, descripcion, finalidad, vencimiento, completa);
    }

    public ImportanteUrgente(String nombre, String vencimiento, boolean completa) throws InputMismatchException {
        super(nombre, vencimiento, completa);
    }

    public ImportanteUrgente(Tarea origen){
        super(origen);
    }

    @Override public String toString(){
        return super.toString() + "\nEsta es una tarea Importante y Urgente";
    }
}

class ImportanteNoUrgente extends Tarea {

    public ImportanteNoUrgente(String nombre, String vencimiento) throws InputMismatchException {
        super(nombre, vencimiento);
    }

    public ImportanteNoUrgente(String nombre, String descripcion, String finalidad, String vencimiento){
        super(nombre, descripcion, finalidad, vencimiento);
    }

    public ImportanteNoUrgente(String nombre, String descripcion, String finalidad, String vencimiento,
            boolean completa){
        super(nombre, descripcion, finalidad, vencimiento, completa);
    }

    public ImportanteNoUrgente(String nombre, String vencimiento, boolean completa) throws InputMismatchException {
        super(nombre, vencimiento, completa);
    }

    public ImportanteNoUrgente(Tarea origen){
        super(origen);
    }

    @Override public String toString(){
        return super.toString() + "\nEsta es una tarea Importante pero NO Urgente";
    }
}

class NoImportanteUrgente extends Tarea {

    public NoImportanteUrgente(String nombre, String vencimiento) throws InputMismatchException {
        super(nombre, vencimiento);
    }

    public NoImportanteUrgente(String nombre, String descripcion, String finalidad, String vencimiento){
        super(nombre, descripcion, finalidad, vencimiento);
    }

    public NoImportanteUrgente(String nombre, String descripcion, String finalidad, String vencimiento,
            boolean completa){
        super(nombre, descripcion, finalidad, vencimiento, completa);
    }

    public NoImportanteUrgente(String nombre, String vencimiento, boolean completa) throws InputMismatchException {
        super(nombre, vencimiento, completa);
    }

    public NoImportanteUrgente(Tarea origen){
        super(origen);
    }

    @Override public String toString(){
        return super.toString() + "\nEsta es una tarea NO Importante pero Urgente";
    }
}

class NiImportanteNiUrgente extends Tarea {

    public NiImportanteNiUrgente(String nombre, String vencimiento) throws InputMismatchException {
        super(nombre, vencimiento);
    }

    public NiImportanteNiUrgente(String nombre, String descripcion, String finalidad, String vencimiento){
        super(nombre, descripcion, finalidad, vencimiento);
    }

    public NiImportanteNiUrgente(String nombre, String descripcion, String finalidad, String vencimiento,
            boolean completa){
        super(nombre, descripcion, finalidad, vencimiento, completa);
    }

    public NiImportanteNiUrgente(String nombre, String vencimiento, boolean completa) throws InputMismatchException {
        super(nombre, vencimiento, completa);
    }

    public NiImportanteNiUrgente(Tarea origen){
        super(origen);
    }

    @Override public String toString(){
        return super.toString() + "\nEsta es una tarea NI Importante NI Urgente";
    }
}
