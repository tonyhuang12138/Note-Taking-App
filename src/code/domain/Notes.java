package code.domain;

import java.util.Date;
//Recording note content
public class Notes {

	private int id;
	//Title of the note
	private String title;
	//Content of the note
	private String content;
	//Update time of the note（the notes are sorted in the order of last edited）
	private Date updatetime;
	//Type (subject)
	private String type;
	//Username（the users can only see their own notes）
	private String username;
	
	public Notes(){}
	
	public Notes(int id, String title){
		this.id = id;
		this.title = title;
	}
	//Constructor
	public Notes(int id, String title, String content, Date updatetime, String type, String username){
		this.id = id;
		this.title = title;
		this.content = content;
		this.updatetime = updatetime;
		this.type = type;
		this.username = username;
	}
	//Setters and getters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
