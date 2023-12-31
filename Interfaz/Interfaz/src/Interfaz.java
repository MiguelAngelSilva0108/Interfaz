import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame {

    private JTextField usuarioField;
    private JPasswordField passwordField;
    private ConexionBD conexionBD; // Cambio de Bd a ConexionBD

    public Interfaz() {
        setTitle("Inicio de Sesión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        conexionBD = new ConexionBD(); // Cambio de Bd a ConexionBD

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel usuarioLabel = new JLabel("Usuario:");
        JLabel passwordLabel = new JLabel("Contraseña:");
        usuarioField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Iniciar Sesión");

        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);

        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(usuarioLabel, gbc);

        gbc.gridx++;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(usuarioField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(passwordLabel, gbc);

        gbc.gridx++;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Iniciando sesión...");
                String usuario = usuarioField.getText();
                String contrasena = new String(passwordField.getPassword());

                if (conexionBD.probarConexion(usuario, contrasena)) { // Cambio de bd.probarConexion a conexionBD.probarConexion
                    System.out.println("Inicio de sesión exitoso para " + usuario);
                    // Iniciar la ventana Proceso con el usuario actual
                    Proceso proceso = new Proceso(usuario);
                    proceso.mostrarVentana();
                    // Cerrar la ventana de inicio de sesión
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(Interfaz.this, "Nombre de usuario o contraseña incorrectos.",
                            "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel);

        setResizable(true);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Interfaz();
            }
        });
    }
}

