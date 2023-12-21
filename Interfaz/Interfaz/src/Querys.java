import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Querys extends JFrame {

    public Querys() {
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
                ejecutarConsulta(query);
            }
        });

        // Agregar componentes al panel
        panel.add(label);
        panel.add(queryField);
        panel.add(executeButton);

        // Agregar el panel al contenido de la ventana
        setContentPane(panel);
    }

    // Método para ejecutar la consulta
    private void ejecutarConsulta(String query) {
        try (Connection connection = Bd.obtenerConexion()) {
            Statement statement = connection.createStatement();

            // Verificar si es un SELECT o no
            boolean esSelect = query.trim().toUpperCase().startsWith("SELECT");

            if (esSelect) {
                // Si es un SELECT, ejecutar y mostrar resultados en una nueva ventana
                mostrarResultados(statement.executeQuery(query));
            } else {
                // Si no es un SELECT, ejecutar y mostrar mensaje de éxito
                int filasAfectadas = statement.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Instrucción realizada. Filas afectadas: " + filasAfectadas,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("42000")) {
                // Error de falta de permisos (SQLState "42000")
                JOptionPane.showMessageDialog(this, "Error: No tiene permisos para ejecutar esta consulta.",
                        "Error de permisos", JOptionPane.ERROR_MESSAGE);
            } else {
                // Otros errores
                JOptionPane.showMessageDialog(this, "Error al ejecutar la consulta: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para mostrar los resultados de una consulta SELECT
    private void mostrarResultados(ResultSet resultSet) throws SQLException {
        // Crear un StringBuilder para construir el resultado
        StringBuilder resultBuilder = new StringBuilder();

        // Obtener metadatos para obtener el nombre de las columnas
        int columnCount = resultSet.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            resultBuilder.append(resultSet.getMetaData().getColumnName(i)).append("\t");
        }
        resultBuilder.append("\n");

        // Recorrer filas y columnas para obtener los datos
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                resultBuilder.append(resultSet.getString(i)).append("\t");
            }
            resultBuilder.append("\n");
        }

        // Mostrar resultados en una nueva ventana
        JTextArea resultTextArea = new JTextArea(resultBuilder.toString());
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);

        JFrame resultFrame = new JFrame("Resultados de la consulta");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.getContentPane().add(scrollPane);
        resultFrame.setSize(400, 300);
        resultFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // Este es solo un ejemplo para probar la ventana Querys
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Querys querys = new Querys();
                querys.setVisible(true);
            }
        });
    }
}
