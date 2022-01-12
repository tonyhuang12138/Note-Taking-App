package code.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import code.base.MysqlConnection;
import code.domain.Users;
import code.util.Encryption;
//Operating, searching and saving users' data
public class UsersService {
	//If the variables are set as private, then the subclass can inherit but cannot use them
	//Database usage
	public Connection connection = null;
	public PreparedStatement preparedStatement = null;
	public ResultSet resultSet = null;
	//Looking up user
	public List<Users> getUsers(Users users){
		System.out.println("-----------BaseService-----------");
		List<Users> userList = new ArrayList<Users>();
		Users user;
		try {
			//Establishing connection to the database
            connection = MysqlConnection.getConnection();
            //1=1 is for preventing getting an error message from "where" when there is no search condition
            StringBuffer sql = new StringBuffer("select id,username,password from users where 1=1 ");
            //The non-empty parts in users are all used as searching conditions
            if(!"".equals(users.getId())){
            	sql.append("and id="+users.getId()+" ");
            }
            if(!"".equals(users.getUsername())&&users.getUsername()!=null){
            	sql.append("and username='"+users.getUsername()+"' ");
            }
            if(!"".equals(users.getPassword())&&users.getPassword()!=null){
            	sql.append("and password='"+users.getPassword()+"' ");
            }
            //Precompiling
            preparedStatement = connection.prepareStatement(sql.toString());
			//Executing sql, obtaining result set
			resultSet = preparedStatement.executeQuery();
			//Determining whether if the value exists
			while(resultSet.next()){
				//Exists, create new users
				user = new Users(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
				//Putting users into list
				userList.add(user);
            }
        }catch (SQLException exception) {
        	exception.printStackTrace();
		}finally{
			//Closing jdbc resource
			MysqlConnection.close(connection, preparedStatement, resultSet);
		}
		return userList;
	}
	//Saving registered user
	public int saveUser(Users users) {
		int result = 0;
		try {
			connection = MysqlConnection.getConnection();
        	String sql = "insert into users (username,password) values(?,?)";
        	preparedStatement = connection.prepareStatement(sql);
        	preparedStatement.setString(1, users.getUsername());
        	//Encrypt password with MD5
        	preparedStatement.setString(2, Encryption.encrypt(users.getPassword()));
        	result = preparedStatement.executeUpdate();
        }catch (SQLException exception) {
        	exception.printStackTrace();
		}finally{
			MysqlConnection.close(connection, preparedStatement, resultSet);
		}
		return result;
	}
}
