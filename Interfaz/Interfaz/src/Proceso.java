import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Proceso extends JFrame {

    private String usuario; // Variable para almacenar el nombre de usuario

    // Constructor sin argumentos
    public Proceso() {
        setTitle("Base de Datos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Hacer visible la ventana
        setVisible(true);
    }

    // Constructor con argumento para establecer el nombre de usuario
    public Proceso(String usuario) {
        this(); // Llama al constructor sin argumentos para realizar configuraciones comunes
        this.usuario = usuario;
    }

    // Método para establecer el nombre de usuario después de la creación de la
    // instancia
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void mostrarVentana() {
        // Crear un panel principal con BorderLayout
        JPanel panel = new JPanel(new BorderLayout());

        // Crear un panel superior para el mensaje de bienvenida y botón de cerrar
        // sesión
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Bienvenido " + usuario);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Tamaño de la fuente aumentado
        welcomePanel.add(welcomeLabel, BorderLayout.WEST);

        // Añadir botón de cerrar sesión en color rojo
        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setForeground(Color.RED);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar la ventana actual y volver a la ventana de inicio de sesión
                dispose();
                Interfaz interfaz = new Interfaz();
                interfaz.setVisible(true);
            }
        });
        welcomePanel.add(logoutButton, BorderLayout.EAST);

        // Agregar el panel de bienvenida al panel principal en la parte superior
        panel.add(welcomePanel, BorderLayout.NORTH);

        // Crear un panel central con BorderLayout para organizar mejor los componentes
        JPanel centralPanel = new JPanel(new BorderLayout());

        // Crear un nuevo panel para mostrar las tablas en forma de tabla
        JPanel tablesTablePanel = new JPanel();
        tablesTablePanel.setLayout(new BorderLayout()); // Añadir esta línea para asegurar el diseño adecuado

        // Crear un modelo de tabla para almacenar los datos de las tablas
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Tablas de la base de datos");

        try (Connection connection = Bd.obtenerConexion()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, null, new String[] { "TABLE" });

            // Agregar las tablas al modelo de tabla (excluyendo "sys_config")
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                if (!tableName.equals("sys_config")) {
                    tableModel.addRow(new Object[] { tableName });
                }
            }

            // Crear la tabla con el modelo de tabla
            JTable tablesTable = new JTable(tableModel);

            // Agregar la tabla a un JScrollPane para permitir el desplazamiento
            JScrollPane scrollPane = new JScrollPane(tablesTable);

            // Agregar el JScrollPane al panel de tablas en forma de tabla
            tablesTablePanel.add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción de SQL
            JOptionPane.showMessageDialog(this, "Error al obtener las tablas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Agregar el panel de tablas en forma de tabla al panel central
        centralPanel.add(tablesTablePanel, BorderLayout.CENTER);

        // Crear un nuevo panel para el botón
        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Añadir un botón para abrir la ventana de Querys
        JButton queryButton = new JButton("Realizar Query");
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir la ventana de Querys
                Querys querysWindow = new Querys();
                querysWindow.setVisible(true);
            }
        });

        // Agregar el botón al panel
        queryPanel.add(queryButton);

        // Agregar el panel de Querys al panel central en la parte inferior
        centralPanel.add(queryPanel, BorderLayout.SOUTH);

        // Agregar el panel central al panel principal
        panel.add(centralPanel, BorderLayout.CENTER);

        // Actualizar la variable de usuario y repintar la ventana
        welcomeLabel.setText("Bienvenido " + usuario);
        revalidate();
        repaint();

        // Agregar el panel principal al marco
        setContentPane(panel);

        // Hacer visible la ventana
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        // Este es solo un ejemplo para probar la ventana Proceso
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Proceso proceso = new Proceso();
                proceso.setUsuario("EjemploUsuario");
                proceso.mostrarVentana();
            }
        });
    }
}
