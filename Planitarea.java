import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.sound.sampled.Clip;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.io.File;
import java.util.InputMismatchException;

//TODO Tal vez usar "Spinner" para campos que tomen números
//Realmente se puede usar para muchas cosas, fechas, horas, eso, sería muy útil

public class Planitarea extends JFrame implements ActionListener{
    private final Font DEFAULT_FONT = new Font("Serif", Font.PLAIN, 15);
    private final Insets DEFAULT_INSETS = new Insets(20, 20, 20, 20);
    private JPanel principal, tareas, nueva_tarea, horario, horario_nueva_tarea, panel_cambiante;
    private JScrollPane scroll;
    private Timer temporizador;
    private JLabel etiqueta_temporizador = new JLabel("25:00", SwingConstants.CENTER), etiqueta_modo_temporizador, etiqueta_ciclos_faltantes;
    private int minutos, segundos, ciclos_faltantes, ciclos_realizados;
    private GridBagConstraints gbc;
    private boolean descanso, realizando_tarea, playable;
    private Clip clip;
    private Tarea aux_tarea;
    
    //TODO GraphicsConfiguration de Java podría ser útil para el programa, o no con gridbag?    

    Planitarea(String mensaje_inicial){
        setTitle("Planitarea");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        setIconImage(new ImageIcon("./Resources/icono_planitarea.png").getImage());
        gbc = new GridBagConstraints();
        temporizador = new Timer(1000, this);
        principal(mensaje_inicial);
        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    private void principal(){
        principal("");
    }
    private void principal(String mensaje){
        if(principal == null){
            principal = new JPanel(new GridBagLayout());
            JLabel titulo_principal = new JLabel("Planitarea", SwingConstants.CENTER);
            JButton boton_tareas = new JButton("Tareas");
            JButton boton_horario = new JButton("Horario");
            JButton boton_temporizador_pomodoro = new JButton("Temporizador de Pomodoro");
            JButton boton_cerrar = new JButton("X");  //Se podría cambiar por un ícono de cerrar

            boton_tareas.addActionListener(event -> tareas());
            boton_horario.addActionListener(event -> horario());
            boton_temporizador_pomodoro.addActionListener(event -> pomodoro(""));
            boton_cerrar.addActionListener(event -> {Main.actualizar_archivo_tareas(); dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));});

            modificar_gbc(4, 0, 1, 1, 0.7, 0.2, GridBagConstraints.CENTER, GridBagConstraints.NONE, DEFAULT_INSETS);
            principal.add(titulo_principal, gbc);
            modificar_gbc(1, 1, 1, 1, 0.2, 0.25, -1, GridBagConstraints.HORIZONTAL, DEFAULT_INSETS);
            principal.add(boton_tareas, gbc);
            modificar_gbc(1, 2, 1, 1, 0.2, 0.25, -1, -1, DEFAULT_INSETS);
            principal.add(boton_horario, gbc);
            modificar_gbc(1, 3, 1, 1, 0.2, 0.25, -1, -1, DEFAULT_INSETS);
            principal.add(boton_temporizador_pomodoro, gbc);
            modificar_gbc(1, 4, 1, 1, 0.2, 0.05, -1, GridBagConstraints.NONE, DEFAULT_INSETS);
            principal.add(boton_cerrar, gbc);

            titulo_principal.setFont(new Font("Serif", Font.PLAIN, 60));
        }

        for(int i=0; i<3; i++) if(principal.getComponent(principal.getComponentCount()-1) instanceof JLabel) principal.remove(principal.getComponentCount()-1);

        JLabel etiqueta_texto_porcentaje = new JLabel("", SwingConstants.CENTER);
        JLabel etiqueta_porcentaje = new JLabel("", SwingConstants.CENTER);

        if(Main.get_tareas_existentes().length > 0){
            etiqueta_texto_porcentaje.setText("Porcentaje de Tareas Completadas");
            etiqueta_porcentaje.setText(String.format("%.1f%%", Main.get_porcentaje()));
            
            modificar_gbc(4, 2, 1, 1, 0.7, 0.25, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, DEFAULT_INSETS);
            principal.add(etiqueta_texto_porcentaje, gbc);
            modificar_gbc(4, 3, 1, 1, 0.7, 0.25, -1, -1, DEFAULT_INSETS);
            principal.add(etiqueta_porcentaje, gbc);
        }

        JLabel etiqueta_titulo = new JLabel(mensaje, SwingConstants.CENTER);
        etiqueta_titulo.setForeground(new Color(236, 28, 28));

        modificar_gbc(4, 1, 1, 1, 0.7, 0.25, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, DEFAULT_INSETS);
        principal.add(etiqueta_titulo, gbc);
        
        etiqueta_titulo.setFont(new Font("Serif", Font.PLAIN, 20));
        etiqueta_texto_porcentaje.setFont(new Font("Serif", Font.PLAIN, 30));
        etiqueta_porcentaje.setFont(new Font("Serif", Font.PLAIN, 30));

        set_content(principal);
    }

    private void tareas(){
        tareas("Elige la opción que desees tomar");
    }
    private void tareas(String message){
        tareas(message, false);
    }
    private void tareas(String message, boolean error){
        if(tareas == null){
            tareas = new JPanel(new GridBagLayout());
            JButton boton_nueva_tarea = new JButton("Ingresar nueva tarea");
            JButton boton_lista_tareas = new JButton("Ver lista de tareas");
            JButton boton_volver = new JButton("←");

            boton_nueva_tarea.addActionListener(event -> nueva_tarea("Por favor, ingresa los siguientes datos", false));
            boton_lista_tareas.addActionListener(event -> lista_tareas());
            boton_volver.addActionListener(event -> principal());

            modificar_gbc(2, 1, 1, 1, 0.5, 0.35, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20, 50, 20, 50));
            tareas.add(boton_nueva_tarea, gbc);
            modificar_gbc(2, 2, 1, 1, 0.5, 0.35, -1, -1, null);
            tareas.add(boton_lista_tareas, gbc);
            modificar_gbc(0, 3, 1, 1, 0.25, 0.1, -1, GridBagConstraints.NONE, DEFAULT_INSETS);
            tareas.add(boton_volver, gbc);

        }

        if(tareas.getComponent(tareas.getComponentCount()-1) instanceof JLabel) tareas.remove(tareas.getComponentCount()-1);

        JLabel etiqueta_titulo = new JLabel(message, SwingConstants.CENTER);
        etiqueta_titulo.setForeground(error ? new Color(236, 28, 28) : Color.BLACK);

        modificar_gbc(2, 0, 1, 1, 0.5, 0.2, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, DEFAULT_INSETS);
        tareas.add(etiqueta_titulo, gbc);
        etiqueta_titulo.setFont(new Font("Serif", Font.PLAIN, 20));

        set_content(tareas);
    }

    private void nueva_tarea(String message, boolean error){
        //TODO Que se ingresen / solos en el apartado de fecha
        if(nueva_tarea == null){
            nueva_tarea = new JPanel(new GridBagLayout());
            JButton boton_volver = new JButton("←");
            JButton boton_enter = new JButton(">");
            
            JLabel etiqueta_nombre = new JLabel("Nombre de la tarea:");
            JTextField texto_nombre = new JTextField("");
            JLabel etiqueta_descripcion = new JLabel("Descripción de la tarea:");
            JTextArea texto_descripcion = new JTextArea("");
            JLabel etiqueta_finalidad = new JLabel("Asignatura o ámbito para el que realizarás la tarea:");
            JTextField texto_finalidad = new JTextField("");
            JLabel etiqueta_vencimiento = new JLabel("Fecha de vencimiento de la tarea:");
            JTextField texto_vencimiento = new JTextField("dd/mm/aaaa");

            boton_volver.addActionListener(event -> tareas());
            boton_enter.addActionListener(event -> {
                asignar_Eisenhower(texto_nombre.getText(), texto_descripcion.getText(), texto_finalidad.getText(), texto_vencimiento.getText());
                //Si son solo "whitespaces", lo settea al string vacío, si no, lo settea a los valores ya guardados, para que el usuario no pierda lo escrito sin necesidad
                texto_nombre.setText(texto_nombre.getText().trim() == "" ? "":texto_nombre.getText());
                texto_descripcion.setText(texto_descripcion.getText().trim() == "" ? "":texto_descripcion.getText());
                texto_finalidad.setText(texto_finalidad.getText().trim() == "" ? "":texto_finalidad.getText());
                texto_vencimiento.setText("dd/mm/aaaa");
            });

            texto_nombre.setEditable(true);
            texto_descripcion.setEditable(true);
            texto_finalidad.setEditable(true);
            texto_vencimiento.setEditable(true);

            etiqueta_nombre.setFont(new Font("Serif", Font.PLAIN, 18));
            etiqueta_descripcion.setFont(new Font("Serif", Font.PLAIN, 18));
            etiqueta_finalidad.setFont(new Font("Serif", Font.PLAIN, 18));
            etiqueta_vencimiento.setFont(new Font("Serif", Font.PLAIN, 18));

            Insets label_text_insets = new Insets(0, 50, 0, 50);
            modificar_gbc(2, 3, 1, 1, 0.5, 0.05625, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, label_text_insets);
            nueva_tarea.add(etiqueta_nombre, gbc);
            modificar_gbc(2, 4, 1, 1, 0.5, 0.1125, GridBagConstraints.CENTER, GridBagConstraints.BOTH, label_text_insets);
            nueva_tarea.add(texto_nombre, gbc);

            modificar_gbc(2, 5, 1, 1, 0.5, 0.05625, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, label_text_insets);
            nueva_tarea.add(etiqueta_descripcion, gbc);
            modificar_gbc(2, 6, 1, 1, 0.5, 0.225, GridBagConstraints.CENTER, GridBagConstraints.BOTH, label_text_insets);
            nueva_tarea.add(texto_descripcion, gbc);

            modificar_gbc(2, 7, 1, 1, 0.5, 0.05625, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, label_text_insets);
            nueva_tarea.add(etiqueta_finalidad, gbc);
            modificar_gbc(2, 8, 1, 1, 0.5, 0.1125, GridBagConstraints.CENTER, GridBagConstraints.BOTH, label_text_insets);
            nueva_tarea.add(texto_finalidad, gbc);

            modificar_gbc(2, 9, 1, 1, 0.5, 0.05625, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, label_text_insets);
            nueva_tarea.add(etiqueta_vencimiento, gbc);
            modificar_gbc(2, 10, 1, 1, 0.5, 0.1125, GridBagConstraints.CENTER, GridBagConstraints.BOTH, label_text_insets);
            nueva_tarea.add(texto_vencimiento, gbc);

            modificar_gbc(3, 10, 1, 1, 0.1, 0.1125, GridBagConstraints.CENTER, GridBagConstraints.NONE, DEFAULT_INSETS);
            nueva_tarea.add(boton_enter, gbc);

            modificar_gbc(0, 11, 1, 1, 0.15, 0.05, GridBagConstraints.CENTER, GridBagConstraints.NONE, DEFAULT_INSETS);
            nueva_tarea.add(boton_volver, gbc);

        }
        if(nueva_tarea.getComponent(nueva_tarea.getComponentCount()-1) instanceof JLabel) nueva_tarea.remove(nueva_tarea.getComponentCount()-1);

        JLabel etiqueta_titulo = new JLabel(message, SwingConstants.CENTER);
        etiqueta_titulo.setForeground(error ? new Color(236, 28, 28) : Color.BLACK);
        etiqueta_titulo.setFont(new Font("Serif", Font.PLAIN, 20));

        modificar_gbc(2, 1, 1, 1, 0.5, 0.225, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, DEFAULT_INSETS);
        nueva_tarea.add(etiqueta_titulo, gbc);

        set_content(nueva_tarea);
    }

    private void asignar_Eisenhower(String nombre, String descripcion, String finalidad, String vencimiento){
        aux_tarea = Main.buscar_tarea(nombre);
        if(aux_tarea == null){
            try{
                new ImportanteUrgente(nombre, descripcion, finalidad, vencimiento);

                panel_cambiante = new JPanel(new GridBagLayout());
                JLabel etiqueta_titulo = new JLabel("Clasifica la tarea según su importancia y urgencia");
                JButton boton_imp_urg = new JButton("Hacer");
                JButton boton_imp_no_urg = new JButton("Planificar");
                JButton boton_no_imp_urg = new JButton("Delegar");
                JButton boton_ni_imp_ni_urg = new JButton("Ignorar");
                JLabel etiqueta_imp = new JLabel("Importante");
                JLabel etiqueta_no_imp = new JLabel("NO Importante");
                JLabel etiqueta_urg = new JLabel("Urgente");
                JLabel etiqueta_no_urg = new JLabel("NO Urgente");

                etiqueta_titulo.setFont(new Font("Serif", Font.PLAIN, 25));
                etiqueta_imp.setFont(new Font("Serif", Font.PLAIN, 20));
                etiqueta_no_imp.setFont(new Font("Serif", Font.PLAIN, 20));
                etiqueta_urg.setFont(new Font("Serif", Font.PLAIN, 20));
                etiqueta_no_urg.setFont(new Font("Serif", Font.PLAIN, 20));

                boton_imp_urg.setFont(new Font("Serif", Font.PLAIN, 25));
                boton_imp_urg.setBackground(Color.CYAN);
                boton_imp_urg.setOpaque(true);
                boton_imp_urg.addActionListener(event -> guardar_tarea(1, nombre, descripcion, finalidad, vencimiento));

                boton_imp_no_urg.setFont(new Font("Serif", Font.PLAIN, 25));
                boton_imp_no_urg.setBackground(Color.ORANGE);
                boton_imp_no_urg.setOpaque(true);
                boton_imp_no_urg.addActionListener(event -> guardar_tarea(2, nombre, descripcion, finalidad, vencimiento));

                boton_no_imp_urg.setFont(new Font("Serif", Font.PLAIN, 25));
                boton_no_imp_urg.setBackground(Color.YELLOW);
                boton_no_imp_urg.setOpaque(true);
                boton_no_imp_urg.addActionListener(event -> guardar_tarea(3, nombre, descripcion, finalidad, vencimiento));

                boton_ni_imp_ni_urg.setFont(new Font("Serif", Font.PLAIN, 25));
                boton_ni_imp_ni_urg.setBackground(Color.GRAY);
                boton_ni_imp_ni_urg.setOpaque(true);
                boton_ni_imp_ni_urg.addActionListener(event -> guardar_tarea(4, nombre, descripcion, finalidad, vencimiento));

                modificar_gbc(0, 0, 3, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, DEFAULT_INSETS);
                panel_cambiante.add(etiqueta_titulo, gbc);
                modificar_gbc(0, 2, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, DEFAULT_INSETS);
                panel_cambiante.add(etiqueta_imp, gbc);
                modificar_gbc(0, 3, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, DEFAULT_INSETS);
                panel_cambiante.add(etiqueta_no_imp, gbc);
                modificar_gbc(1, 1, 1, 1, 0, 0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, DEFAULT_INSETS);
                panel_cambiante.add(etiqueta_urg, gbc);
                modificar_gbc(2, 1, 1, 1, 0, 0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, DEFAULT_INSETS);
                panel_cambiante.add(etiqueta_no_urg, gbc);

                modificar_gbc(1, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, DEFAULT_INSETS);
                panel_cambiante.add(boton_imp_urg, gbc);
                modificar_gbc(2, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, DEFAULT_INSETS);
                panel_cambiante.add(boton_imp_no_urg, gbc);
                modificar_gbc(1, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, DEFAULT_INSETS);
                panel_cambiante.add(boton_no_imp_urg, gbc);
                modificar_gbc(2, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, DEFAULT_INSETS);
                panel_cambiante.add(boton_ni_imp_ni_urg, gbc);
                                
                set_content(panel_cambiante);

            }catch(InputMismatchException error){
                nueva_tarea(error.getMessage(), true);
            }
        }else{
            nueva_tarea(aux_tarea.get_finalidad() == "" ? "Ya existe una tarea con ese nombre, está designada para: " + aux_tarea.get_finalidad() : "Ya existe una tarea con ese nombre", true);
        }
    }

    private void guardar_tarea(int tipo, String nombre, String descripcion, String finalidad, String vencimiento){
        aux_tarea = null;
        switch(tipo){
            case 1:
                aux_tarea = new ImportanteUrgente(nombre, descripcion, finalidad, vencimiento);
                break;
            case 2:
                aux_tarea = new ImportanteNoUrgente(nombre, descripcion, finalidad, vencimiento);
                break;
            case 3:
                aux_tarea = new NoImportanteUrgente(nombre, descripcion, finalidad, vencimiento);
                break;
            case 4:
                aux_tarea = new NiImportanteNiUrgente(nombre, descripcion, finalidad, vencimiento);
                break;
        }
        if(Main.nueva_tarea(aux_tarea)){
            tareas("Tarea añadida con éxito");
        }else{
            tareas("Ocurrió un error inesperado y no se guardó la tarea", true);
        }
    }

    private void lista_tareas(){
        Tarea[] tareas_existentes = Main.get_tareas_existentes();
        if(tareas_existentes.length == 0 || tareas_existentes[0].get_completa()){   //tareas_existentes está organizado de tal forma que primero van las tareas incompletas y luego las completas, si la primera está completa, todas las demás también
            tareas("No tienes tareas pendientes actualmente");
        }else{
            panel_cambiante = new JPanel(new GridBagLayout());
            JButton boton_volver = new JButton("←");

            int pos_final = tareas_existentes.length-1;
            for(int i=tareas_existentes.length-1; i>=0; i--){
                if(!tareas_existentes[i].get_completa()){
                    pos_final = i;
                    break;
                }
            }
            tareas_existentes = java.util.Arrays.copyOfRange(tareas_existentes, 0, pos_final+1);
            int num_gridy = tareas_existentes.length%2 == 0 ? tareas_existentes.length : tareas_existentes.length+1;
            
            JTextArea[] texto_tareas = new JTextArea[tareas_existentes.length];
            JButton[] botones_completo = new JButton[tareas_existentes.length];
            JButton[] botones_modificar = new JButton[tareas_existentes.length];

            boton_volver.addActionListener(event -> tareas());

            for(int i=0; i<tareas_existentes.length; i++){
                final Tarea tarea_temporal = new ImportanteUrgente(tareas_existentes[i]);
                texto_tareas[i] = new JTextArea(tareas_existentes[i].toString());
                botones_completo[i] = new JButton("Completa");
                botones_modificar[i] = new JButton("Modificar información");
                
                texto_tareas[i].setFont(new Font("Serif", Font.PLAIN, 15));
                texto_tareas[i].setEditable(false);
                botones_completo[i].setFont(new Font("Serif", Font.PLAIN, 12));
                botones_modificar[i].setFont(new Font("Serif", Font.PLAIN, 12));
                botones_modificar[i].addActionListener(event -> modificar_tarea(tarea_temporal));
                botones_completo[i].addActionListener(event -> {
                    tarea_temporal.set_completa(true); 
                    if(!Main.actualizar_tarea(tarea_temporal)){
                        JOptionPane.showMessageDialog(panel_cambiante, "No fue posible marcar la tarea como completada", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
                    }else{
                        lista_tareas();
                    }
                });

                if(i%2 == 0){   //Si es par, va en una tarea de la izquierda, si no, de la derecha
                    modificar_gbc(1, i, 2, 2, 0.45, 0.45, GridBagConstraints.CENTER, GridBagConstraints.BOTH, DEFAULT_INSETS);
                    panel_cambiante.add(texto_tareas[i], gbc);
                    modificar_gbc(1, i+1, 1,1, 0.225, 0.225, GridBagConstraints.SOUTH, GridBagConstraints.NONE, DEFAULT_INSETS);
                    panel_cambiante.add(botones_modificar[i], gbc);
                    modificar_gbc(2, i+1, 1,1, 0.225, 0.225, GridBagConstraints.SOUTH, GridBagConstraints.NONE, DEFAULT_INSETS);
                    panel_cambiante.add(botones_completo[i], gbc);
                }else{
                    modificar_gbc(3, i-1, 2, 2, 0.45, 0.45, GridBagConstraints.CENTER, GridBagConstraints.BOTH, DEFAULT_INSETS);
                    panel_cambiante.add(texto_tareas[i], gbc);
                    modificar_gbc(3, i, 1,1, 0.225, 0.225, GridBagConstraints.SOUTH, GridBagConstraints.NONE, DEFAULT_INSETS);
                    panel_cambiante.add(botones_modificar[i], gbc);
                    modificar_gbc(4, i, 1,1, 0.225, 0.225, GridBagConstraints.SOUTH, GridBagConstraints.NONE, DEFAULT_INSETS);
                    panel_cambiante.add(botones_completo[i], gbc);
                }
            }

            modificar_gbc(0, num_gridy, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.NONE, DEFAULT_INSETS);
            panel_cambiante.add(boton_volver, gbc);

            set_content(panel_cambiante);
        }
    }

    //TODO Voy acá en poner GridbagLayout
    private void modificar_tarea(Tarea modificar){
        //TODO Que se ingresen / solos en el apartado de fecha
        //setText(String)?
        //https://docs.oracle.com/javase/tutorial/uiswing/components/generaltext.html#doclisteners
        //https://stackoverflow.com/questions/10717623/how-to-add-document-listener-to-the-jtextfields-inside-a-panel
        
        //this https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html
        //o Spinner

        panel_cambiante = new JPanel(null);
        JLabel etiqueta_titulo = new JLabel("Ingresa la información que deseas modificar", SwingConstants.CENTER);
        JButton boton_volver = new JButton("←");
        JButton boton_enter = new JButton(">");
        JLabel etiqueta_descripcion = new JLabel("Descripción de la tarea:");
        JTextArea texto_descripcion = new JTextArea(modificar.get_descripcion());
        JLabel etiqueta_finalidad = new JLabel("Asignatura o ámbito para el que realizarás la tarea:");
        JTextField texto_finalidad = new JTextField(modificar.get_finalidad());
        JLabel etiqueta_vencimiento = new JLabel("Fecha de vencimiento de la tarea (formato dd/mm/aaaa):");
        JTextField texto_vencimiento = new JTextField(modificar.get_vencimiento());
        
        etiqueta_titulo.setBounds(710, 250, 500, 30);

        boton_volver.setBounds(90, 900, 50, 50);
        boton_volver.addActionListener(event -> lista_tareas());
        boton_enter.setBounds(1230, 605, 50, 50);
        boton_enter.addActionListener(event -> {
            Tarea temp_tarea = new NoImportanteUrgente(modificar);  //La matriz a la que pertenece no importa porque no se puede cambiar esa información en el archivo o en tareas_existentes
            temp_tarea.set_descripcion(texto_descripcion.getText());
            temp_tarea.set_finalidad(texto_finalidad.getText()); 
            try{
                temp_tarea.set_vencimiento(texto_vencimiento.getText());
                if(Main.actualizar_tarea(temp_tarea)){
                    lista_tareas();
                }else{
                    etiqueta_titulo.setForeground(new Color(236, 28, 28));
                    etiqueta_titulo.setText("No fue posible guardar los cambios");
                }
            }catch(InputMismatchException error){
                etiqueta_titulo.setForeground(new Color(236, 28, 28));
                etiqueta_titulo.setText(error.getMessage());
            }
        });

        etiqueta_descripcion.setBounds(710, 405, 500, 20);
        texto_descripcion.setBounds(710, 425, 500, 50);
        texto_descripcion.setEditable(true);
        etiqueta_finalidad.setBounds(710, 495, 500, 20);
        texto_finalidad.setBounds(710, 515, 500, 50);
        texto_finalidad.setEditable(true);
        etiqueta_vencimiento.setBounds(710, 585, 500, 20);
        texto_vencimiento.setBounds(710, 605, 500, 50);
        texto_vencimiento.setEditable(true);

        add_multiple(panel_cambiante, etiqueta_titulo, boton_volver, boton_enter, etiqueta_descripcion, texto_descripcion, etiqueta_finalidad, texto_finalidad, etiqueta_vencimiento, texto_vencimiento);
        
        etiqueta_titulo.setFont(new Font("Serif", Font.PLAIN, 20));
        etiqueta_descripcion.setFont(new Font("Serif", Font.PLAIN, 18));
        etiqueta_finalidad.setFont(new Font("Serif", Font.PLAIN, 18));
        etiqueta_vencimiento.setFont(new Font("Serif", Font.PLAIN, 18));
        
        set_content(panel_cambiante);
    }

    private void horario(){
        //TODO Hacer esta parte del código
        //Primero podría recibir el input de la tarea y su hora de inicio y fin cuando no haya un horario existente.
        //Si ya hay un horario, que pueda añadir una tarea y lo mande al mismo menú, o simplemente quitar una tarea.
        //Visualmente, se podría hacer como la representación de una linkedlist.
        //El horario semanal muestra los días de la semana y si tienen un día asociado, y los manda al menú correspondiente de horario diario cuando se les da click
        //Un botón que permita al usuario decidir si el horario semanal es para la otra semana o para esta misma.

        Horario_Diario horario_dia = Main.get_horario_dia();
        Horario_Semanal horario_actual = Main.get_horario_actual();
        Horario_Semanal horario_proximo = Main.get_horario_proximo();
        if(horario_dia == null && horario_actual == null && horario_proximo == null){
            horario_nueva_tarea();
        }
        if(horario == null){
            horario = new JPanel(null);
        }
        set_content(horario);
    }

    private void horario_nueva_tarea(){
        if(horario_nueva_tarea == null){
            JComboBox<String> seleccion_formato_hora = new JComboBox<>();
            seleccion_formato_hora.addItem("");
            seleccion_formato_hora.addItem("am");
            seleccion_formato_hora.addItem("pm");
            //seleccion_formato_hora.setBounds(, , 50, 50);
        }
        //Esta parte hay que reiniciarla todo el tiempo porque las tareas existentes se pueden ir actualizando
        Tarea[] tareas_existentes = Main.get_tareas_existentes();
        JLabel etiqueta_seleccion_tarea = new JLabel("Elige una tarea para añadir al horario", SwingConstants.CENTER);
        etiqueta_seleccion_tarea.setBounds(760, 550, 400, 20);
        JComboBox<String> seleccion_tarea = new JComboBox<>();
        seleccion_tarea.addItem("");
        for(int i=0; i<tareas_existentes.length; i++){
            if(tareas_existentes[i].get_completa()) break;
            seleccion_tarea.addItem(tareas_existentes[i].get_nombre());
        }
        seleccion_tarea.setBounds(810, 580, 300, 50);
    }

    private void pomodoro(String message){
        panel_cambiante = new JPanel(null);
        Tarea[] tareas_existentes = Main.get_tareas_existentes();
        JButton boton_enter = new JButton(">");
        JButton boton_volver = new JButton("←");
        JLabel etiqueta_ciclos = new JLabel("Ingresa el número de ciclos de concentración para el temporizador", SwingConstants.CENTER);
        JTextField texto_ciclos = new JTextField("");
        JLabel etiqueta_error = new JLabel(message, SwingConstants.CENTER);
        
        if(tareas_existentes.length != 0 && !tareas_existentes[0].get_completa()){
            JLabel etiqueta_seleccion = new JLabel("Elige una tarea para realizar en este tiempo (opcional)", SwingConstants.CENTER);
            etiqueta_seleccion.setBounds(760, 550, 400, 20);
            JComboBox<String> seleccion = new JComboBox<>();
            seleccion.addItem("");
            for(int i=0; i<tareas_existentes.length; i++){
                if(tareas_existentes[i].get_completa()) break;
                seleccion.addItem(tareas_existentes[i].get_nombre());
            }
            seleccion.setBounds(810, 580, 300, 50);
            
            etiqueta_ciclos.setBounds(710, 460, 500, 20);
            texto_ciclos.setBounds(810, 480, 300, 50);
            boton_enter.setBounds(1130, 650, 50, 50);
            boton_enter.addActionListener(event -> nuevo_pomodoro(texto_ciclos.getText(), seleccion.getSelectedIndex()));

            add_multiple(panel_cambiante, etiqueta_seleccion, seleccion);
            etiqueta_seleccion.setFont(new Font("Serif", Font.PLAIN, 18));
        }else{//boton_enter cambia su posición dependiendo de la cantidad de elementos que hayan
            etiqueta_ciclos.setBounds(710, 510, 500, 20);
            texto_ciclos.setBounds(810, 540, 300, 50);
            boton_enter.setBounds(1130, 610, 50, 50);
            boton_enter.addActionListener(event -> nuevo_pomodoro(texto_ciclos.getText(), 0));
        }
            
        texto_ciclos.setEditable(true);
        boton_volver.setBounds(90, 900, 50, 50);
        boton_volver.addActionListener(event -> principal());
        etiqueta_error.setBounds(710, 250, 500, 30);
        etiqueta_error.setForeground(new Color(236, 28, 28));

        add_multiple(panel_cambiante, etiqueta_ciclos, texto_ciclos, boton_volver, boton_enter, etiqueta_error);            
        etiqueta_ciclos.setFont(new Font("Serif", Font.PLAIN, 18));
        etiqueta_error.setFont(new Font("Serif", Font.PLAIN, 20));

        set_content(panel_cambiante);
    }

    private void nuevo_pomodoro(String ciclos_str, int index){
        try{
            ciclos_faltantes = Integer.parseInt(ciclos_str);
            if(ciclos_faltantes <= 0) throw new InputMismatchException("La cantidad de ciclos debe ser mayor a 0");

            try{
                clip = javax.sound.sampled.AudioSystem.getClip();
                clip.open(javax.sound.sampled.AudioSystem.getAudioInputStream(new File("./Resources/Sonido_Pomodoro.wav")));
                clip.loop(0);
                playable = true;
            }catch(Exception e){
                playable = false;
            }

            panel_cambiante = new JPanel(null);
            etiqueta_modo_temporizador = new JLabel("Periodo de concentración", SwingConstants.CENTER);
            etiqueta_ciclos_faltantes = new JLabel("Ciclos Faltantes: " + ciclos_faltantes, SwingConstants.CENTER);
            JButton boton_volver = new JButton("←");
            JButton boton_pausar = new JButton("⏸");
            JButton boton_aumentar_ciclos = new JButton("+1 ciclo");
            JButton boton_terminar = new JButton("Terminar");

            boton_volver.setBounds(90, 900, 50, 50);
            boton_volver.addActionListener(event -> {temporizador.stop(); if(clip.isOpen()) clip.close(); principal();});
            boton_pausar.setBounds(1050, 475, 50, 50);
            boton_pausar.addActionListener(event -> alternar_boton_pausa(boton_pausar));
            boton_aumentar_ciclos.setBounds(1125, 475, 100, 50);
            boton_aumentar_ciclos.addActionListener(event -> {if(ciclos_faltantes < Integer.MAX_VALUE) ciclos_faltantes++; if(minutos != 0 || segundos != 0) etiqueta_ciclos_faltantes.setText("Ciclos Faltantes: " + ciclos_faltantes);});
            boton_terminar.setBounds(910, 570, 100, 50);
            boton_terminar.addActionListener(event -> {minutos = 0; segundos = 0; descanso = false; ciclos_faltantes = 0; if(!temporizador.isRunning()) actionPerformed(event);});
            etiqueta_temporizador.setBounds(460, 450, 1000, 100);
            etiqueta_modo_temporizador.setBounds(460, 230, 1000, 100);
            etiqueta_ciclos_faltantes.setBounds(1250, 475, 150, 50);

            add_multiple(panel_cambiante, etiqueta_temporizador, etiqueta_modo_temporizador, etiqueta_ciclos_faltantes, boton_pausar, boton_aumentar_ciclos, boton_volver, boton_terminar);

            etiqueta_temporizador.setFont(new Font("Serif", Font.PLAIN, 64));
            etiqueta_modo_temporizador.setFont(new Font("Serif", Font.PLAIN, 50));
            
            minutos = 25;
            segundos = 0;
            ciclos_realizados = 0;
            descanso = false;

            if(index == 0){//Si es 0, no seleccionó ninguna tarea
                realizando_tarea = false;
                temporizador.start();
            }else{
                index--;    //Para que represente el índice de las tareas existentes, se le quita 1 porque los índices de las tareas se movieron para añadir una opción vacía
                aux_tarea = Main.get_tareas_existentes()[index];
                realizando_tarea = true;
                JTextArea texto_tarea = new JTextArea("Tarea en progreso:\n\n" + aux_tarea.toString(false));
                texto_tarea.setBounds(460, 390, 300, 300);
                texto_tarea.setEditable(false);
                add_multiple(panel_cambiante, texto_tarea);
                temporizador.start();
            }

            set_content(panel_cambiante);

        }catch(InputMismatchException error){
            pomodoro(error.getMessage());
        }catch(NumberFormatException error){
            pomodoro("La cantidad de ciclos debe ser un número natural");
        }
    }

    private void alternar_boton_pausa(JButton pausa){
        if(temporizador.isRunning()){
            temporizador.stop();
            pausa.setText("⏵");
        }else if(minutos > 0 || segundos > 0){
            temporizador.restart();
            pausa.setText("⏸");
        }
    }

    private void set_content(JPanel content){
        scroll = new JScrollPane(content);
        scroll.updateUI();
        scroll.revalidate();
        setContentPane(scroll);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        setMinimumSize(getSize());
        pack();
        setMinimumSize(null);
    }

    private void add_multiple(JPanel panel, Component ... componentes){
        //TODO No debería usarse con GridbagLayout, si se quita y no genera ningún error o advertencia, debería estar completo el GridbagLayout
        for(int i=0; i<componentes.length; i++){
            componentes[i].setFont(DEFAULT_FONT);
            panel.add(componentes[i]);
        }
    }

    private void modificar_gbc(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets){
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        //-1 significa que debería usar los mismos que tenía antes
        if(weightx >= 0){
            gbc.weightx = weightx;
        }
        if(weighty >= 0){
            gbc.weighty = weighty;
        }
        if(anchor >= 0){
            gbc.anchor = anchor;
        }
        if(fill >= 0){
            gbc.fill = fill;
        }
        if(insets != null){
            gbc.insets = insets;
        }
    }

    @Override public void actionPerformed(ActionEvent e){
        if(minutos == 0 && segundos == 0){
            if(!descanso){
                ciclos_faltantes--;
                etiqueta_ciclos_faltantes.setText("Ciclos Faltantes: " + ciclos_faltantes);
                if(ciclos_faltantes > 0){
                    temporizador.stop();
                    if(playable){clip.setFramePosition(0); clip.start();}
                    JOptionPane.showMessageDialog(panel_cambiante, "El periodo de concentración ha acabado\nPuedes iniciar el periodo de descanso", "Temporizador Pomodoro", JOptionPane.INFORMATION_MESSAGE);
                    if(playable){
                        clip.stop();
                        clip.setFramePosition(0);
                    }
                    ciclos_realizados++;
                    if(ciclos_realizados % 4 != 0){
                        minutos = 5;
                        etiqueta_modo_temporizador.setText("Descanso corto");
                    }else{
                        minutos = 15;
                        etiqueta_modo_temporizador.setText("Descanso largo");
                    }
                    temporizador.restart();
                }else{
                    temporizador.stop();
                    etiqueta_ciclos_faltantes.setText("Ciclos Faltantes: " + 0);
                    etiqueta_modo_temporizador.setText("El temporizador ha acabado, buen trabajo");
                    if(playable){clip.setFramePosition(0); clip.start();}
                    if(realizando_tarea){
                        JButton boton_completar = new JButton("Marcar como completa");
                        boton_completar.setBounds(460, 700, 300, 50);
                        boton_completar.addActionListener(event -> {
                            aux_tarea.set_completa(true);
                            if(Main.actualizar_tarea(aux_tarea)){
                                principal();
                            }else{
                                aux_tarea.set_completa(false);
                                principal("No fue posible marcar la tarea como completa");
                            }
                        });
                        panel_cambiante.add(boton_completar);
                        panel_cambiante.updateUI();
                        realizando_tarea = false;
                    }
                }
            }else{
                temporizador.stop();
                if(playable) clip.start();
                JOptionPane.showMessageDialog(panel_cambiante, "El periodo de descanso ha acabado\nAhora iniciará el periodo de concentración", "Temporizador Pomodoro", JOptionPane.INFORMATION_MESSAGE);
                if(playable){
                    clip.stop();
                    clip.setFramePosition(0);
                }
                minutos = 25;
                etiqueta_modo_temporizador.setText("Periodo de Concentración");
                temporizador.restart();
            }
            descanso = !descanso;
        }else if(segundos == 0){
            minutos--;
            segundos = 59;
        }else{
            segundos--;
        }
        etiqueta_temporizador.setText(String.format("%02d:%02d", minutos, segundos));
    }
}