package code.frame;

import java.awt.Color;
import java.awt.Font;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import code.domain.Schedule;
import code.service.ScheduleService;

//Calendar note
//View a specific day's revision plan's notes, shows a list of notes which can all be accessed by clicking on them
public class ScheduleNote {

	private String userName;
	private Date myDay;
	
	private JFrame myScheduleJF;
	private JLayeredPane layeredPane;
	
	//Note
	private JTable noteTable;
	private TableModel noteModel;
	private Object[]  noteColumn = {"ID","Note Title"};
	private Object[][] noteRow;
	
	private ScheduleService scheduleService = new ScheduleService();
	
	public ScheduleNote(String userName, Date myDay) {
		this.userName = userName;
		this.myDay = myDay;
	}
	//Generate JFrame, load component, and define event
	public void getMyDayNote(){
		myScheduleJF = new JFrame("My Plan--"+userName+"--"+myDay);
		myScheduleJF.setBackground(Color.decode("#FCF4DC"));
		layeredPane = new JLayeredPane();
		
		noteTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        refreshNoteTable();
        noteTable.setFont(new Font("Arial", Font.PLAIN, 14));
        noteTable.setRowHeight(20);
        noteTable.setBackground(Color.decode("#FCF4DC"));
        JScrollPane noteScrollPane = new JScrollPane(noteTable);
        noteScrollPane.setBounds(10,10,400,500);
		layeredPane.add(noteScrollPane);
		
		myScheduleJF.setLayeredPane(layeredPane);
		myScheduleJF.setSize(500, 600);
		myScheduleJF.setLocationRelativeTo(myScheduleJF.getOwner());  
		myScheduleJF.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		myScheduleJF.setVisible(true);
		
		//Type (subject) table action
		noteTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if(noteTable.getValueAt(noteTable.getSelectedRow(),0)!=null){
					int openId = (int) noteTable.getValueAt(noteTable.getSelectedRow(),0);
					NoteView nv = new NoteView(openId);
					nv.openNote();
				}
			}
		});
	}
	
	//Obtaining note list
	public void refreshNoteTable(){
		List<Schedule> list = scheduleService.getScheduleNotes(userName, myDay);
		Schedule schedule;
		if(list.size()>0){
			noteRow = new Object[list.size()][2];
			for(int i=0;i<list.size();i++){
				schedule = list.get(i);
				noteRow[i][0] = schedule.getNoteid();
				noteRow[i][1] = schedule.getTitle();
			}
			noteModel = new DefaultTableModel(noteRow, noteColumn);
			noteTable.setModel(noteModel);
			noteTable.getColumnModel().getColumn(0).setMinWidth(0);
			noteTable.getColumnModel().getColumn(0).setMaxWidth(0);
			noteTable.getColumnModel().getColumn(0).setPreferredWidth(0);
		}
	}
}
