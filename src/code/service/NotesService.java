package code.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import code.base.MysqlConnection;
import code.domain.Notes;
public class NotesService {

	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public List<Notes> getNotes(Notes notes){
		List<Notes> noteList = new ArrayList<Notes>();
		Notes note;
		try {
            connection = MysqlConnection.getConnection();
            StringBuffer sql = new StringBuffer("select id,title,content,updatetime,type,username from notes where 1=1 ");
            if(!"".equals(notes.getId())&&notes.getId()>0){
            	sql.append("and id="+notes.getId()+" ");
            }
            if(!"".equals(notes.getUsername())&&notes.getUsername()!=null){
            	sql.append("and username='"+notes.getUsername()+"' ");
            }
            if(!"".equals(notes.getTitle())&&notes.getTitle()!=null){
            	sql.append("and title like '%"+notes.getTitle()+"%' ");
            }
            if(!"".equals(notes.getType())&&notes.getType()!=null){
            	sql.append("and type='"+notes.getType()+"' ");
            }
            sql.append("order by updatetime desc");
            preparedStatement = connection.prepareStatement(sql.toString());
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				note = new Notes(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getTimestamp(4), resultSet.getString(5), resultSet.getString(6));
				noteList.add(note);
            }
        }catch (SQLException exception) {
        	exception.printStackTrace();
		}finally{
			MysqlConnection.close(connection, preparedStatement, resultSet);
		}
		return noteList;
	}
	
	public int saveNote(Notes notes, int openId){
		int isSave = 0;
		String sql = "";
		try {
			connection = MysqlConnection.getConnection();
            if(openId == 0){
            	sql = "insert into notes (title,content,type,username,updatetime) values(?,?,?,?,?)";
            	preparedStatement = connection.prepareStatement(sql);
            	preparedStatement.setString(1, notes.getTitle());
            	preparedStatement.setString(2, notes.getContent());
            	preparedStatement.setString(3, notes.getType());
            	preparedStatement.setString(4, notes.getUsername());
            	preparedStatement.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
            } else{
            	sql = "update notes set title = ?,content = ?,type = ?,updatetime = ? where id = ?";
            	preparedStatement = connection.prepareStatement(sql);
            	preparedStatement.setString(1, notes.getTitle());
            	preparedStatement.setString(2, notes.getContent());
            	preparedStatement.setString(3, notes.getType());
            	preparedStatement.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
            	preparedStatement.setInt(5, openId);
            }
            isSave = preparedStatement.executeUpdate();
        }catch (SQLException exception) {
        	exception.printStackTrace();
		}finally{
			MysqlConnection.close(connection, preparedStatement, resultSet);
		}
		return isSave;
	}
	public int deleteNote(int openId){
		int isDelete = 0;
		try {
			connection = MysqlConnection.getConnection();
			String sql = "delete from notes where id = ?";
        	preparedStatement = connection.prepareStatement(sql);
        	preparedStatement.setInt(1, openId);
            isDelete = preparedStatement.executeUpdate();
        }catch (SQLException exception) {
        	exception.printStackTrace();
		}finally{
			MysqlConnection.close(connection, preparedStatement, resultSet);
		}
		return isDelete;
	}
	public List<Notes> getNoteByTime(String userName, String radioTxt, String searchTxt) {
		List<Notes> noteList = new ArrayList<Notes>();
		Notes note;
		try {
            connection = MysqlConnection.getConnection();
            String sql = "select id,title from notes where username = ? and updatetime>=? and title like ? order by updatetime desc";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            Date nowDate = new Date();
            long nowTime = nowDate.getTime();
            long serachTime = 0;
            if("Nearly 30 days".equals(radioTxt)){
            	serachTime = nowTime - (long)30 * 24 * 60 * 60 * 1000;
            }else if("Nearly 60 days".equals(radioTxt)) {
            	serachTime = nowTime - (long)60 * 24 * 60 * 60 * 1000;
            }else if("Nearly 100 days".equals(radioTxt)) {
            	serachTime = nowTime - (long)100 * 24 * 60 * 60 * 1000;
            }else if("All".equals(radioTxt)) {
            	serachTime = new Date(0,0,1).getTime();
            }else{
            	serachTime = nowTime - (long)30 * 24 * 60 * 60 * 1000;
            }
            preparedStatement.setDate(2, new java.sql.Date(serachTime));
            if("".equals(searchTxt)){
            	preparedStatement.setString(3, "%%");
            }else{
            	preparedStatement.setString(3, "%" + searchTxt + "%");
            }
			resultSet = preparedStatement.executeQuery();
			int i = 0;
	      	while (resultSet.next()) {
				note = new Notes(resultSet.getInt(1), resultSet.getString(2));
				noteList.add(note);
			}
        }catch (SQLException exception) {
        	exception.printStackTrace();
		}finally{
			MysqlConnection.close(connection, preparedStatement, resultSet);
		}
		return noteList;
	}
	
}
