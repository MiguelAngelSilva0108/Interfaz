import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Querys extends JFrame {

    private Bd bd;
    private String usuario;
    private String contrasena;

    public Querys(String usuario, String contrasena, Bd bd) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.bd = bd;

        setTitle("Realizar Query");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear componentes dentro de la ventana de Querys
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Ingrese su consulta:");
        JTextField queryField = new JTextField(20);
        JButton executeButton = new JButton("Ejecutar Query");

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener la consulta ingresada por el usuario
                String query = queryField.getText();

                // Lógica para ejecutar la consulta
                ejecutarConsulta(query, usuario, contrasena);
            }
        });

        // Agregar componentes al panel
        panel.add(label);
        panel.add(queryField);
        panel.add(executeButton);

        // Agregar el panel al contenido de la ventana
        setContentPane(panel);
    }

    private void ejecutarConsulta(String query, String usuario, String contrasena) {
        try (Connection connection = ConexionBD.obtenerConexion(usuario, contrasena)) {
            Statement statement = connection.createStatement();

            boolean esSelect = query.trim().toUpperCase().startsWith("SELECT");

            if (esSelect) {
                mostrarResultados(statement.executeQuery(query));
            } else {
                int filasAfectadas = statement.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Instrucción realizada. Filas afectadas: " + filasAfectadas,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("42000")) {
                JOptionPane.showMessageDialog(this, "Error: No tiene permisos para ejecutar esta consulta.",
                        "Error de permisos", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al ejecutar la consulta: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Método para mostrar los resultados de una consulta SELECT
private void mostrarResultados(ResultSet resultSet) throws SQLException {
    // Crear un modelo de tabla para los resultados
    DefaultTableModel tableModel = new DefaultTableModel();

    // Obtener información sobre las columnas
    java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
    int columnCount = metaData.getColumnCount();

    // Agregar las columnas al modelo de la tabla
    for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
        tableModel.addColumn(metaData.getColumnName(columnIndex));
    }

    // Agregar las filas al modelo de la tabla
    while (resultSet.next()) {
        Object[] row = new Object[columnCount];
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            row[columnIndex - 1] = resultSet.getObject(columnIndex);
        }
        tableModel.addRow(row);
    }

    // Crear una tabla y mostrarla en un cuadro de diálogo
    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);
    JOptionPane.showMessageDialog(this, scrollPane, "Resultados de la consulta", JOptionPane.INFORMATION_MESSAGE);
}


    public static void main(String[] args) {
        // Este es solo un ejemplo para probar la ventana Querys
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               
            }
        });
    }
}
