package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			try {
				connection = DriverManager.getConnection(
						"jdbc:sqlserver://localhost:1433;databaseName=Project;integratedSecurity=true;");
				System.out.println("Connect DB successful");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Connect fail!!!");
			System.err.println(e.getMessage() + "/n" + e.getClass() + "/n" + e.getCause());
		}
		return connection;
	}

}
