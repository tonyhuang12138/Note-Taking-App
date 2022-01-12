package code.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.Document;
import code.domain.Notes;
import code.domain.Schedule;
import code.service.NotesService;
import code.service.ScheduleService;

public class ScheduleAdd {

	private String userName;
	private Date myDay;
	
	private JFrame scheduleAddJF;
	private JLayeredPane layeredPane;
	
	private JTable noteTable;
	private TableModel noteModel;
	private Object[]  noteColumn = {"ID","Note Title"};
	private Object[][] noteRow;
	
	private ButtonGroup bg; 
	private String radioTxt = ""; 
	private JTextField noteSearch;
	private String searchTxt = ""; 
	
	private ScheduleService scheduleService = new ScheduleService();
	private NotesService notesService = new NotesService();
	
	public ScheduleAdd(String userName, Date myDay) {
		this.userName = userName;
		this.myDay = myDay;
	}
	public void getMyNote(){
		scheduleAddJF = new JFrame("Add My Plan--"+userName+"--"+myDay);
		scheduleAddJF.setBackground(Color.decode("#FCF4DC"));
		layeredPane = new JLayeredPane();
		
	    JRadioButton b1 = new JRadioButton("Nearly 30 days");
	    JRadioButton b2 = new JRadioButton("Nearly 60 days");
	    JRadioButton b3 = new JRadioButton("Nearly 100 days");
	    JRadioButton b4 = new JRadioButton("All");
	    bg = new ButtonGroup();
	    bg.add(b1);
	    bg.add(b2);
	    bg.add(b3);
	    bg.add(b4);
	    b1.setBounds(270,10,130,30);
		layeredPane.add(b1, JLayeredPane.MODAL_LAYER);
		b2.setBounds(400,10,130,30);
		layeredPane.add(b2, JLayeredPane.MODAL_LAYER);
		b3.setBounds(530,10,130,30);
		layeredPane.add(b3, JLayeredPane.MODAL_LAYER);
		b4.setBounds(660,10,60,30);
		layeredPane.add(b4, JLayeredPane.MODAL_LAYER);
		b1.setSelected(true);
		
		JLabel labelSearch = new JLabel("Note Title:");
		labelSearch.setBounds(270,60,80,30);
	    layeredPane.add(labelSearch,JLayeredPane.MODAL_LAYER);
		noteSearch = new JTextField();
		noteSearch.setBackground(Color.decode("#FFFFFF"));
		noteSearch.setBounds(350,60,200,30);
		layeredPane.add(noteSearch,JLayeredPane.MODAL_LAYER);
		
		noteTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        noteTable.setFont(new Font("Arial", Font.PLAIN, 14));
        noteTable.setRowHeight(20);
        JScrollPane noteScrollPane = new JScrollPane(noteTable);
        noteScrollPane.setBounds(10,10,250,440);
		layeredPane.add(noteScrollPane);
		refreshNoteTable();
		
		scheduleAddJF.setLayeredPane(layeredPane);
		scheduleAddJF.setSize(800, 500);
		scheduleAddJF.setLocationRelativeTo(scheduleAddJF.getOwner());  
		scheduleAddJF.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		scheduleAddJF.setVisible(true);
		
		noteTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if(noteTable.getValueAt(noteTable.getSelectedRow(),0)!=null){
					int isConfirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to add notes to the schedule ?", "Tips", JOptionPane.YES_NO_OPTION);
	            	if(isConfirm == JOptionPane.YES_OPTION){
	            		int openId = (int) noteTable.getValueAt(noteTable.getSelectedRow(),0);
	            		Schedule schedule = new Schedule(openId, myDay, userName);
	            		int isAdd = scheduleService.addNoteSchedule(schedule);
	            		if(isAdd == 1){
	            			JOptionPane.showMessageDialog(null, "Added Successfully !", "Tips", JOptionPane.PLAIN_MESSAGE);
	            		}else{
	            			JOptionPane.showMessageDialog(null, "Failed to add, please try again later !", "Tips", JOptionPane.ERROR_MESSAGE);
	            		}
	            	}
				}
			}
		});
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				radioTxt = b1.getText();
				refreshNoteTable();
			}
	    });
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				radioTxt = b2.getText();
				refreshNoteTable();
			}
	    });
		b3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				radioTxt = b3.getText();
				refreshNoteTable();
			}
	    });
		b4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				radioTxt = b4.getText();
				refreshNoteTable();
			}
	    });
		Document document = noteSearch.getDocument();
		document.addDocumentListener(new DocumentListener(){

			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				searchTxt = noteSearch.getText();
				refreshNoteTable();
			}

			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				searchTxt = noteSearch.getText();
				refreshNoteTable();
			}

			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				searchTxt = noteSearch.getText();
				refreshNoteTable();
			}
		});
		
	}
	public void refreshNoteTable() {
		List<Notes> noteList= notesService.getNoteByTime(userName, radioTxt, searchTxt);
		if(noteList.size()>0){
			Notes note;
			noteRow = new Object[noteList.size()][2];
			for(int i=0;i<noteList.size();i++){
				note = noteList.get(i);
				noteRow[i][0] = note.getId();
	      		noteRow[i][1] = note.getTitle();
			}
			noteModel = new DefaultTableModel(noteRow, noteColumn);
			noteTable.setModel(noteModel);
			noteTable.getColumnModel().getColumn(0).setMinWidth(0);
			noteTable.getColumnModel().getColumn(0).setMaxWidth(0);
			noteTable.getColumnModel().getColumn(0).setPreferredWidth(0);
		}
	}
	
}
