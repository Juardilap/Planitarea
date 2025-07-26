import java.util.InputMismatchException;

class Tiempo_Tarea {
    private final static int MAX_HORA = 24;
    private final static int MIN_HORA = 0;
    private Tarea tarea;
    private double hora_inicio;
    private double hora_fin;

    Tiempo_Tarea(Tarea tarea, String hora_inicio, String hora_fin) throws InputMismatchException {
        this.tarea = tarea;
        set_hora_inicio(hora_inicio);
        set_hora_fin(hora_fin);
    }
    
    public void set_hora_inicio(String tiempo) throws InputMismatchException {
        if (tiempo.matches("\\d{1,2}:\\d{2}")) {
            String[] partesTiempo = tiempo.split(":");
            int horas = Integer.parseInt(partesTiempo[0]);
            int minutos = Integer.parseInt(partesTiempo[1]);
            String periodo = tiempo.length() == 7 ? tiempo.substring(5, 7).toLowerCase() : "";

            if ((periodo.equals("am") || periodo.equals("pm") || periodo.equals("")) &&
                horas >= MIN_HORA && horas < MAX_HORA && minutos >= 0 && minutos < 60) {
                if (periodo.equals("pm") && horas != 12) {
                    horas += 12;
                } else if (periodo.equals("am") && horas == 12) {
                    horas = 0;
                }
                this.hora_inicio = horas + minutos / 60.0;
            } else {
                throw new InputMismatchException("Hora de inicio fuera del rango válido o formato inválido");
            }
        } else {
            throw new InputMismatchException("Formato de hora de inicio inválido");
        }
    }


    public void set_hora_fin(String tiempo) throws InputMismatchException {
        if (tiempo.matches("\\d{1,2}:\\d{2}")) {
            String[] partesTiempo = tiempo.split(":");
            int horas = Integer.parseInt(partesTiempo[0]);
            int minutos = Integer.parseInt(partesTiempo[1]);
            String periodo = tiempo.length() == 7 ? tiempo.substring(5, 7).toLowerCase() : "";
             if ((periodo.equals("am") || periodo.equals("pm") || periodo.equals("")) &&
                horas >= MIN_HORA && horas < MAX_HORA && minutos >= 0 && minutos < 60) {
                if (periodo.equals("pm") && horas != 12) {
                    horas += 12;
                } else if (periodo.equals("am") && horas == 12) {
                    horas = 0;
                }
                this.hora_fin = horas + minutos / 60.0;

                if (this.hora_fin <= this.hora_inicio) {
                    throw new InputMismatchException("Hora de fin debe ser después de la hora de inicio");
                }
            } else {
                throw new InputMismatchException("Hora de fin fuera del rango válido o formato inválido");
            }
        } else {
            throw new InputMismatchException("Formato de hora de fin inválido");
        }
    }

    public Tarea get_tarea() {
        return tarea;
    }

    public double get_hora_inicio() {
        return hora_inicio;
    }

    public double get_hora_fin() {
        return hora_fin;
    }
}