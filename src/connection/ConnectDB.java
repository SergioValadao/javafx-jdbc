package connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.Statement;

public class ConnectDB {
	
	private static Connection conn = null;
	
	public static Connection DbConnection() {
		if(conn == null) {
			Properties props = loadPropriedades();
			String url = props.getProperty("dburl");
			try {
				conn = DriverManager.getConnection(url, props);
			} catch (SQLException e) {
				throw new ConnException("Falha na conexão na base de dados! Verifique.");				
			}
		}		
		return conn;
	}
	
	public static void CloseConnection() {
		if(conn != null ) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void CloseStatement(Statement st) {
		if(st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void CloseResultSet(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Properties loadPropriedades() {

		try (FileInputStream fs = new FileInputStream("db.properties")) {			
			Properties prop = new Properties();
			prop.load(fs);
			return prop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e)		{
			e.printStackTrace();
		}
		return null;
	}
}
