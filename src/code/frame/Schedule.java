package code.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import code.service.ScheduleService;

//Scheduling
//Since the revision plan (calendar) is closely linked to dates, in order to let the users see their revision plan for the whole week, this frame will be displayed as a calendar, and allows the user to add or view notes
public class Schedule {

	private String userName;
	private JFrame scheduleJF;
	private JLayeredPane layeredPane;
	//Calendar table
	private JTable dateTable;
	private TableModel dateModel;
	private JComboBox yearBox;
	private JComboBox monBox;
	private Date nowDate = new Date();
	private int yearNow = nowDate.getYear() + 1900;
	private int monthNow = nowDate.getMonth() + 1;
	
	private String[] dateColumn = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday" , "Friday", "Saturday"};
	private Object[][] dateRows;
	
	private ScheduleService scheduleService = new ScheduleService();
	
	public Schedule(String userName){
		this.userName = userName;
	}
	
	public void getMySchedule() {
		scheduleJF = new JFrame("My Plan--"+userName);
		scheduleJF.setBackground(Color.decode("#FCF4DC"));
		layeredPane = new JLayeredPane();
		//Year selection box
		yearBox = new JComboBox();
		yearBox.setModel(new DefaultComboBoxModel(getYears()));
		yearBox.setSelectedIndex(nowDate.getYear());
		yearBox.setBounds(10, 10, 100, 30);
		layeredPane.add(yearBox,JLayeredPane.MODAL_LAYER);
		JLabel yLabel = new JLabel("Year");
		yLabel.setBounds(120, 10, 40, 30);
		layeredPane.add(yLabel,JLayeredPane.MODAL_LAYER);
		//Month selection box
		monBox = new JComboBox();
		monBox.setModel(new DefaultComboBoxModel(new String[] {"01","02","03","04","05","06","07","08","09","10","11","12"}));
		monBox.setSelectedIndex(monthNow-1);
		monBox.setBounds(170, 10, 80, 30);
		layeredPane.add(monBox,JLayeredPane.MODAL_LAYER);
		JLabel mLabel = new JLabel("Month");
		mLabel.setBounds(260, 10, 40, 30);
		layeredPane.add(mLabel,JLayeredPane.MODAL_LAYER);
		
		dateTable = new JTable(){
			//Disabling edit button so that the content in the table (calendar) cannot be altered
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 50, 1340, 640);
		layeredPane.add(scrollPane);
		scrollPane.setViewportView(dateTable);
		dateTable.setRowHeight(100);
		dateTable.setFont(new Font("Arial", Font.BOLD, 24));
		dateTable.setBackground(Color.decode("#FCF4DC"));
		
		//Click to show pop-up menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem viewItem = new JMenuItem("View");
        JMenuItem addItem = new JMenuItem("Add");
        viewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Date clickDate = getDay();
				ScheduleNote sn = new ScheduleNote(userName, clickDate);
				sn.getMyDayNote();
            }
        });
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Date clickDate = getDay();
            	ScheduleAdd sa = new ScheduleAdd(userName, clickDate);
				sa.getMyNote();
            }
        });
        popupMenu.add(viewItem);
        popupMenu.add(addItem);
        dateTable.setComponentPopupMenu(popupMenu);
		//Loading date data
		refreshDateTable();
		
		scheduleJF.setLayeredPane(layeredPane);
//		scheduleJF.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		scheduleJF.setSize(1366, 768);
		//Center
		scheduleJF.setLocationRelativeTo(null);
		//Removing resizability
		scheduleJF.setResizable(false);
		scheduleJF.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		scheduleJF.setVisible(true);
		
		//Adding yaer and month selection boxes' action listeners
		yearBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(e.getStateChange()==ItemEvent.SELECTED){
					yearNow = Integer.parseInt(yearBox.getSelectedItem().toString());
					monthNow = Integer.parseInt(monBox.getSelectedItem().toString());
					refreshDateTable();
				}
			}
		});
		monBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(e.getStateChange()==ItemEvent.SELECTED){
					yearNow = Integer.parseInt(yearBox.getSelectedItem().toString());
					monthNow = Integer.parseInt(monBox.getSelectedItem().toString());
					refreshDateTable();
				}
			}
		});
		//Subject table action
		dateTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if(dateTable.getValueAt(dateTable.getSelectedRow(),0)!=null){
					popupMenu.show(dateTable, e.getX(), e.getY());
				}
			}
		});
	}
	
	//Obtain the value in the year selection box
	public String[] getYears(){
		String[] years=null;
		Date d = new Date();
		years = new String[d.getYear()+1];
		for(int i=0; i<=d.getYear(); i++){
			years[i] = String.valueOf(i+1900);
		}
		return years;	
	}
	
	//Obtain the rows of every months
	public void refreshDateTable(){
		Date d = new Date(yearNow-1900 ,monthNow-1,1);
		int weekFirstDay = d.getDay();
		int day = 0;
		boolean flag = true;
		String noteStr = "";
		int count = 1;
		int ddy = 0;
		dateRows = new Object[6][7];
		dateModel = new DefaultTableModel(dateRows, dateColumn);
		if (monthNow == 1 || monthNow == 3 || monthNow == 5 || monthNow == 7 || monthNow == 8 || monthNow == 10 || monthNow == 12){
			day = 31;
		}else if (monthNow == 4 || monthNow == 6 || monthNow == 9 || monthNow == 11){
			day = 30;
		}else if (monthNow == 2 && yearNow%4 == 0){
			day = 29;
		}else if(monthNow == 2 && yearNow%4 != 0){
			day = 28;
		}
		if (weekFirstDay == 0) {
			if (flag) {
				for (int i = 0; i < 6; i++) {
					for (int k = 0; k < 7; k++) {
						if (count > day) {
							flag = false;
							break;
						}
						ddy = count++;
						//Searching for the amount of notes assigned to the given date
						noteStr = scheduleService.getDayNoteCount(new Date(yearNow-1900 ,monthNow-1,ddy), userName);
						dateModel.setValueAt("<html>"+ddy+"<br><font color='#FF0000'>"+noteStr+"</font></html>", i, k);
					}
				}
			}
		} else {
			for (int k = weekFirstDay; k < 7; k++) {
				ddy = count++;
				//Searching for the amount of notes assigned to the given date
				noteStr = scheduleService.getDayNoteCount(new Date(yearNow-1900 ,monthNow-1,ddy), userName);
				dateModel.setValueAt("<html>"+ddy+"<br><font color='#FF0000'>"+noteStr+"</font></html>", 0, k);
			}
			if(flag){
				for (int i = 1; i < 6; i++) {
					for (int k = 0; k < 7; k++) {
						if (count > day) {
							flag = false;
							break;
						}
						ddy = count++;
						//Searching for the amount of notes assigned to the given date
						noteStr = scheduleService.getDayNoteCount(new Date(yearNow-1900 ,monthNow-1,ddy), userName);
						dateModel.setValueAt("<html>"+ddy+"<br><font color='#FF0000'>"+noteStr+"</font></html>", i, k);
					}
				}
			}
		}
		dateTable.setModel(dateModel);
	}
	
	//Obtain the clicked date
	public Date getDay(){
		int row = dateTable.getSelectedRow();
		int col = dateTable.getSelectedColumn();
		String dstr = dateTable.getValueAt(row, col).toString();
		dstr = dstr.substring(dstr.indexOf("<html>")+6, dstr.indexOf("<br>"));
		int getDay = Integer.parseInt(dstr);
		int getYear = Integer.parseInt(yearBox.getModel().getSelectedItem().toString());
		int getMonth = Integer.parseInt(monBox.getModel().getSelectedItem().toString());
		return new Date(getYear-1900, getMonth-1, getDay);
	}
	
}
