package code.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Establishing database connection
public class MysqlConnection {
	
	private static Connection connection = null;
	//Database address: characterEncoding：to prevent scrambled code for Chinese
	private static String url = "jdbc:mysql://115.159.116.86:3306/note?useUnicode=true&characterEncoding=UTF-8";
	//User name & password
	private static String user = "root";
	private static String password = "n1cetest";
	//Registering drive
	static {
		//Catching exceptions
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Obtaining connection
	public static Connection getConnection() {
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	//Closing resource from smallest (result set) to biggest (connection)
	public static void close(Connection connection ,PreparedStatement preparedStatement ,ResultSet resultSet) {
		try {
			if(resultSet!=null) {
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(preparedStatement!=null) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					if(connection!=null) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
