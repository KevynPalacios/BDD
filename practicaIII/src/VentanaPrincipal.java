
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentanaPrincipal extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JRadioButton[] radioButtons;
    private JLabel[] columnLabels;
    private ButtonGroup radioGroup;

    Connection conexion;
    Statement statement;
    String consulta;
    ResultSet resultado;

    public VentanaPrincipal() {

        setTitle("Tabla de Datos");
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        modelo = new DefaultTableModel();
        tabla = new JTable(modelo);

        modelo.addColumn("No-control");
        modelo.addColumn("Nombre");
        modelo.addColumn("Domicilio");
        modelo.addColumn("Ciudad");
        modelo.addColumn("Edad");
        modelo.addColumn("Oficio");

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        radioButtons = new JRadioButton[6];
        columnLabels = new JLabel[6];
        radioGroup = new ButtonGroup();

        JPanel radioPanel = new JPanel(new FlowLayout());
        
        for (int i = 0; i < 6; i++) {
            radioButtons[i] = new JRadioButton();
            columnLabels[i] = new JLabel(modelo.getColumnName(i));
            radioPanel.add(radioButtons[i]);
            radioPanel.add(columnLabels[i]);
            radioGroup.add(radioButtons[i]);

            switch (i) {
                case 0:

                    radioButtons[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mostrarDialogoNoControl();
                        }
                    });
                    break;
                case 1:
                    radioButtons[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                mostrarDialogoTexto("Nombre", "nombre");
                            } catch (SQLException ex) {
                                Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    break;
                case 2:
                    radioButtons[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                mostrarDialogoTexto("Domicilio", "domicilio");
                            } catch (SQLException ex) {
                                Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    break;
                case 3:
                    radioButtons[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                mostrarDialogoTexto("Ciudad", "ciudad");
                            } catch (SQLException ex) {
                                Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    break;
                case 4:
                    radioButtons[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mostrarDialogoEdad();
                        }
                    });
                    break;
                case 5:
                    radioButtons[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                mostrarDialogoTexto("Oficio", "oficio");
                            } catch (SQLException ex) {
                                Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    break;
            }
        }

        add(radioPanel, BorderLayout.SOUTH);

        cargarDatos();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarDatos() {
        String url = "jdbc:mysql://localhost:3306/practica2";
        String usuario = "root";
        String contraseña = "root";

        try {
            conexion = DriverManager.getConnection(url, usuario, contraseña);
            statement = conexion.createStatement();
            consulta = "SELECT * FROM persona";
            resultado = statement.executeQuery(consulta);

            while (resultado.next()) {
                int noControl = resultado.getInt("No_control");
                String nombre = resultado.getString("nombre");
                String domicilio = resultado.getString("domicilio");
                String ciudad = resultado.getString("ciudad");
                String edad = resultado.getString("edad");
                String oficio = resultado.getString("oficio");

                modelo.addRow(new Object[]{noControl, nombre, domicilio, ciudad, edad, oficio});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarDialogoNoControl() {

        JTextField campoTexto = new JTextField(20);
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new GridLayout(2, 3));

        ButtonGroup grupoRadioButtons = new ButtonGroup();

        String[] simbolos = {"<", ">", "<=", ">=", "=", "<>"};

        for (String simbolo : simbolos) {
            JRadioButton radioButton = new JRadioButton(simbolo);
            grupoRadioButtons.add(radioButton);
            radioButtonsPanel.add(radioButton);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("No-control"));
        panel.add(campoTexto);
        panel.add(radioButtonsPanel);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "No-control", JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {
            String textoIngresado = campoTexto.getText();
            String simboloSeleccionado = obtenerSimboloSeleccionado(grupoRadioButtons);

            if (simboloSeleccionado != null) {
                try {

                    String consultaSQL = "SELECT * FROM practica2.persona WHERE No_control " + simboloSeleccionado + " '" + textoIngresado + "'";
                    System.out.println("Consulta SQL generada: " + consultaSQL);

                    Statement nuevoStatement = conexion.createStatement();
                    ResultSet nuevoResultado = nuevoStatement.executeQuery(consultaSQL);

                    DefaultTableModel nuevoModelo = new DefaultTableModel();
                    tabla.setModel(nuevoModelo);

                    nuevoModelo.addColumn("No-control");
                    nuevoModelo.addColumn("Nombre");
                    nuevoModelo.addColumn("Domicilio");
                    nuevoModelo.addColumn("Ciudad");
                    nuevoModelo.addColumn("Edad");
                    nuevoModelo.addColumn("Oficio");

                    while (nuevoResultado.next()) {
                        int noControl = nuevoResultado.getInt("No_control");
                        String nombre = nuevoResultado.getString("nombre");
                        String domicilio = nuevoResultado.getString("domicilio");
                        String ciudad = nuevoResultado.getString("ciudad");
                        String edad = nuevoResultado.getString("edad");
                        String oficio = nuevoResultado.getString("oficio");

                        nuevoModelo.addRow(new Object[]{noControl, nombre, domicilio, ciudad, edad, oficio});
                    }

                    nuevoResultado.close();
                    nuevoStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debes seleccionar un símbolo.");
            }
        }
    }

    private void mostrarDialogoEdad() {

        JTextField campoTexto = new JTextField(20);
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new GridLayout(2, 3));

        ButtonGroup grupoRadioButtons = new ButtonGroup();

        String[] simbolos = {"<", ">", "<=", ">=", "=", "<>"};

        for (String simbolo : simbolos) {
            JRadioButton radioButton = new JRadioButton(simbolo);
            grupoRadioButtons.add(radioButton);
            radioButtonsPanel.add(radioButton);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Edad"));
        panel.add(campoTexto);
        panel.add(radioButtonsPanel);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Edad", JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {
            String textoIngresado = campoTexto.getText();
            String simboloSeleccionado = obtenerSimboloSeleccionado(grupoRadioButtons);

            if (simboloSeleccionado != null) {
                try {

                    String consultaSQL = "SELECT * FROM practica2.persona WHERE edad " + simboloSeleccionado + " '" + textoIngresado + "'";
                    System.out.println("Consulta SQL generada: " + consultaSQL);

                    Statement nuevoStatement = conexion.createStatement();
                    ResultSet nuevoResultado = nuevoStatement.executeQuery(consultaSQL);

                    DefaultTableModel nuevoModelo = new DefaultTableModel();
                    tabla.setModel(nuevoModelo);

                    nuevoModelo.addColumn("No-control");
                    nuevoModelo.addColumn("Nombre");
                    nuevoModelo.addColumn("Domicilio");
                    nuevoModelo.addColumn("Ciudad");
                    nuevoModelo.addColumn("Edad");
                    nuevoModelo.addColumn("Oficio");

                    while (nuevoResultado.next()) {
                        int noControl = nuevoResultado.getInt("No_control");
                        String nombre = nuevoResultado.getString("nombre");
                        String domicilio = nuevoResultado.getString("domicilio");
                        String ciudad = nuevoResultado.getString("ciudad");
                        String edad = nuevoResultado.getString("edad");
                        String oficio = nuevoResultado.getString("oficio");

                        nuevoModelo.addRow(new Object[]{noControl, nombre, domicilio, ciudad, edad, oficio});
                    }

                    nuevoResultado.close();
                    nuevoStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debes seleccionar un símbolo.");
            }
        }
    }

    private void mostrarDialogoTexto(String titulo, String dato) throws SQLException {

        JTextField campoTexto = new JTextField(20);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(titulo));
        panel.add(campoTexto);

        int opcion = JOptionPane.showConfirmDialog(this, panel, titulo, JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {

            String textoIngresado = campoTexto.getText();

            if (!textoIngresado.equals("")) {

                String consultaSQL = "SELECT * FROM practica2.persona WHERE " + dato + " = '" + textoIngresado + "'";
                System.out.println("Consulta SQL generada: " + consultaSQL);

                Statement nuevoStatement = conexion.createStatement();
                ResultSet nuevoResultado = nuevoStatement.executeQuery(consultaSQL);

                DefaultTableModel nuevoModelo = new DefaultTableModel();
                tabla.setModel(nuevoModelo);

                nuevoModelo.addColumn("No-control");
                nuevoModelo.addColumn("Nombre");
                nuevoModelo.addColumn("Domicilio");
                nuevoModelo.addColumn("Ciudad");
                nuevoModelo.addColumn("Edad");
                nuevoModelo.addColumn("Oficio");

                while (nuevoResultado.next()) {
                    int noControl = nuevoResultado.getInt("No_control");
                    String nombre = nuevoResultado.getString("nombre");
                    String domicilio = nuevoResultado.getString("domicilio");
                    String ciudad = nuevoResultado.getString("ciudad");
                    String edad = nuevoResultado.getString("edad");
                    String oficio = nuevoResultado.getString("oficio");

                    nuevoModelo.addRow(new Object[]{noControl, nombre, domicilio, ciudad, edad, oficio});
                }

                nuevoResultado.close();
                nuevoStatement.close();
            } else {
                JOptionPane.showMessageDialog(this, "Debes escribir un nombre");
            }
        }
    }

    private String obtenerSimboloSeleccionado(ButtonGroup group) {
        Enumeration<AbstractButton> buttons = group.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal();
        });
    }
}
