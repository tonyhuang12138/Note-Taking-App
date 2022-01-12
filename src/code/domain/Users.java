package code.domain;
//Recording user information
public class Users {

	private int id;
	//Username
	private String username;
	//Password (for improved security, the passwords have been encrypted by MD5)
	private String password;
	
	public Users(){}
	//Constructors
	public Users(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public Users(int id, String username, String password){
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
