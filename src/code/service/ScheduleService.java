package code.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import code.base.MysqlConnection;
import code.domain.Schedule;
//View, edition, and deletion on the schedule page
public class ScheduleService {

	//Database usage
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	//Obtaining the amount of notes on a given date
	public String getDayNoteCount(Date dd, String userName){
		String restr = " ";
		int num = 0;
		try {
            connection = MysqlConnection.getConnection();
            String sql = "select count(*) from schedule where username = ? and myday = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setDate(2, new java.sql.Date(dd.getTime()));
            resultSet = preparedStatement.executeQuery();
	      	while (resultSet.next()) {
	      		num = resultSet.getInt(1);
			}
        }catch (SQLException exception) {
        	exception.printStackTrace();
		}finally{
			MysqlConnection.close(connection, preparedStatement, resultSet);
		}
		if(num>0){
			restr = "(" + num + " notes)";
		}else{
			//Adding a space bar here would prevent the date number on top to fall down to the position
			//of the number of notes if there are no notes assigned to the date
			restr = "&nbsp;";//This is adding a non-breaking space (HTML)
		}
		return restr;
	}
	//Addition
	public int addNoteSchedule(Schedule schedule) {
		int isAdd = 0;
		try {
			connection = MysqlConnection.getConnection();
        	String sql = "insert into schedule (username,noteid,myday) values(?,?,?)";
        	preparedStatement = connection.prepareStatement(sql);
        	preparedStatement.setString(1, schedule.getUsername());
        	preparedStatement.setInt(2, schedule.getNoteid());
        	preparedStatement.setDate(3, new java.sql.Date(schedule.getMyday().getTime()));
        	isAdd = preparedStatement.executeUpdate();
        }catch (SQLException exception) {
        	exception.printStackTrace();
		}finally{
			MysqlConnection.close(connection, preparedStatement, resultSet);
		}
		return isAdd;
	}
	
	//Obtaining notes
	public List<Schedule> getScheduleNotes(String userName, Date myDay){
		List<Schedule> list = new ArrayList<Schedule>();
		Schedule schedule;
		try {
            connection = MysqlConnection.getConnection();
            String sql = "select sd.id, nt.id as nid, nt.title from schedule sd left join notes nt on sd.noteid=nt.id where sd.username = ? and sd.myday = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setDate(2, new java.sql.Date(myDay.getTime()));
			resultSet = preparedStatement.executeQuery();
			//Obtaining the names of all notes
			int i = 0;
	      	while (resultSet.next()) {
	      		schedule = new Schedule();
	      		schedule.setId(resultSet.getInt(1));
	      		schedule.setNoteid(resultSet.getInt(2));
	      		schedule.setTitle(resultSet.getString(3));
	      		list.add(schedule);
			}
        }catch (SQLException exception) {
        	exception.printStackTrace();
		}finally{
			MysqlConnection.close(connection, preparedStatement, resultSet);
		}
		return list;
	}
}
