import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    public boolean probarConexion(String usuario, String contrasena) {
        try (Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/Compra_Ventas", usuario,
                contrasena)) {
            // La conexión se realiza con las credenciales proporcionadas por el usuario
            return true; // Si la conexión tiene éxito, las credenciales son válidas
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si hay una excepción, las credenciales no son válidas
        }
    }

}
