import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/Compra_Ventas?useUnicode=true&characterEncoding=UTF-8";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "administrador";

    public static Connection obtenerConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al establecer la conexión con la base de datos");
        }
    }
}
