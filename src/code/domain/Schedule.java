package code.domain;

import java.util.Date;
//Revision plan (calendar with scheduler)
public class Schedule {
	
	private int id;
	//Note ID
	private int noteid;
	//Planned revision date
	private Date myday;
	//User name（users can only see their own plans）
	private String username;
	//For presentation
	private String title;
	
	public Schedule(){}
	//Constructor
	public Schedule(int noteid, Date myday, String username){
		this.noteid = noteid;
		this.myday = myday;
		this.username = username;
	}
	
	//Setters and getters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNoteid() {
		return noteid;
	}
	public void setNoteid(int noteid) {
		this.noteid = noteid;
	}
	public Date getMyday() {
		return myday;
	}
	public void setMyday(Date myday) {
		this.myday = myday;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
