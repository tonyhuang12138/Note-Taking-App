package code.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import code.base.MysqlConnection;
import code.domain.Users;
import code.util.Encryption;

//Verifying username and password when logging in
public class LoginService extends UsersService{
	
	//Using the username and password to find the user
	public List<Users> getUsers(Users users){
		System.out.println("-----------Verify Java polymorphism：LoginService-----------");
		List<Users> userList = new ArrayList<Users>();
		Users user;
		try {
			//Establishing connection to the database
            connection = MysqlConnection.getConnection();
            //sql（? is the placeholder）
            String sql = "select id,username,password from users where username = ? and password = ?";
            //Precompiling 
            preparedStatement = connection.prepareStatement(sql);
            //Setting the parameter, replacing the placeholder
			preparedStatement.setString(1, users.getUsername());
			//Encrypt input password and search
			preparedStatement.setString(2, Encryption.encrypt(users.getPassword()));
			//Use sql and obtain the result set
			resultSet = preparedStatement.executeQuery();
			//Checking whether if the value exists in the result set
			while(resultSet.next()){
				//resultSet has the value, create a new user
				user = new Users(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
				//Put users into the list
				userList.add(user);
            }
        }catch (SQLException exception) {
        	exception.printStackTrace();
		}finally{
			//Close jdbc resource
			MysqlConnection.close(connection, preparedStatement, resultSet);
		}
		return userList;
	}
}
