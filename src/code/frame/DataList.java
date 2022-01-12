package code.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.text.Document;
import code.domain.Notes;
import code.service.NotesService;

//Note list
//This is the main interface of the app, which appears when the user successfully logs into the system
//This interface can allow the user to add, edit and delete notes
//Following my user's request, the notes can be organized into different subjects
//The interface also allows the user to search for the desired note by typing in the search box
//For better usability and convinience, I have compiled the major functions to this page 
//so that the user does not have to constantly switch between frames
//The interface consists of several sections, on the left are the subjects and the search box,
//in the middle is the note list section, which allows the user to view, edit, and delete notes by clicking on the notes
public class DataList {
	
	private String userName;
	
	private JFrame dateJF;
	//Creating a JLayeredPane for layering
	private JLayeredPane layeredPane;
	private JTextField txtSearch;//Search box
	//Subject
	private JTable typeTable;
	//Note table
	private JTable noteTable;
	private TableModel noteModel;
	
	private Object[]  noteColumn = {"ID","Note Title"};
	private Object[][] noteRow;
	
	//The pop up menu that shows when left clicks on the note table
	private JPopupMenu popupMenu;
	
	//Editing note
	private JTextField txtTitle;//Title
	private JComboBox<String> txtType;//Subject selection
	private JTextArea txtContent;//Note content
	
	//Save button
	private JButton saveButton;
	private int openId = 0;
	
	//Schedule button
	private JButton scheduleButton;
	
	private NotesService notesService = new NotesService();
	
	public DataList(String userName){
		this.userName = userName;
	}
	//Generate the JFrame, load the components, and define events for the component
	public void getList(){
		//JFrame name + background color
		dateJF = new JFrame("My Notes--"+userName);//To show which user is currently using the program
		dateJF.setBackground(Color.decode("#D2C28D"));
		layeredPane = new JLayeredPane();
		//Search box
		txtSearch = new JTextField();
		txtSearch.setBackground(Color.decode("#FFFFFF"));
		txtSearch.setBounds(10,10,150,20);
		layeredPane.add(txtSearch,JLayeredPane.MODAL_LAYER);
		//Subject table
		//Table title
        Object[] typeColumn = {"Type"};
        //Table data
        Object[][] typeRow = {
        		{"Recent notes"},
                {"Chinese"},
                {"CS"},
                {"Chemistry"},
                {"English"},
                {"Economics"},
                {"History"}
        };
        //Subject list
        typeTable = new JTable(typeRow, typeColumn){
        	//Disable double click to edit
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //Setting font and color
        JTableHeader typeHeader = typeTable.getTableHeader();
        typeHeader.setBackground(Color.decode("#FCF4DC"));
        typeHeader.setBounds(10,50,150,30);
        typeHeader.setFont(new Font("Arial", Font.PLAIN, 24));
		layeredPane.add(typeHeader,JLayeredPane.MODAL_LAYER);
		typeTable.setBackground(Color.decode("#AABDC1"));
		typeTable.setSelectionBackground(Color.decode("#BE9D61"));
		typeTable.setBounds(10,80,150,300);
		typeTable.setFont(new Font("Arial", Font.PLAIN, 20));
		typeTable.setRowHeight(50);
		//Center
		DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
		cr.setHorizontalAlignment(JLabel.CENTER);
		typeTable.setDefaultRenderer(Object.class, cr);
		layeredPane.add(typeTable,JLayeredPane.MODAL_LAYER);
		
		//Note table
        //All data in table
        noteTable = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //Loading note data
        refreshNoteTable("", "");
        //Setting font, height, and background color
        noteTable.setFont(new Font("Arial", Font.PLAIN, 20));
        noteTable.setRowHeight(50);
        noteTable.setBackground(Color.decode("#D2C28D"));
        //Click to show pop up menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem viewItem = new JMenuItem("View");
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
        //Creating an action listener for view
        viewItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	NoteView nv = new NoteView(openId);
				nv.openNote();
            }
        });
        //Creating an action listener for edit
        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setEdit();
            }
        });
        //Creating an action listener for delete
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//Delete confirmation box
            	int isConfirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete it ?", "Tips", JOptionPane.YES_NO_OPTION);
            	//If the user clicked on yes
            	if(isConfirm == JOptionPane.YES_OPTION){
            		//Delete note
            		int isDelete = notesService.deleteNote(openId);
            		//If the note is deleted successfully
            		if(isDelete == 1){
            			//Delete success message
            			JOptionPane.showMessageDialog(null, "Your note has been successfully deleted", "Tips", JOptionPane.PLAIN_MESSAGE);
            			//Delete success, reload subject table
            			refreshNoteTable("", "");
    					openId = 0;
            		}else{
            			//Delete failed message
            			JOptionPane.showMessageDialog(null, "Delete failed, please try again later !", "Tips", JOptionPane.ERROR_MESSAGE);
            		}
            	}
            }
        });
        //Load right click pop up menu
        popupMenu.add(viewItem);
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        noteTable.setComponentPopupMenu(popupMenu);
        //Conditions for scrolling bar to appear in table: if the bond is exceeded, then the scrolling bar appears
        JScrollPane noteScrollPane = new JScrollPane(noteTable);
        noteScrollPane.setBounds(180,10,250,680);
		layeredPane.add(noteScrollPane);
		
		//Editing note
		JLabel labelTitle = new JLabel("Note Title:");
		labelTitle.setBounds(500,30,100,30);
	    layeredPane.add(labelTitle,JLayeredPane.MODAL_LAYER);
		txtTitle = new JTextField();
		txtTitle.setBounds(600,30,750,30);
		layeredPane.add(txtTitle,JLayeredPane.MODAL_LAYER);
		
		JLabel labelType = new JLabel("Note Type:");
		labelType.setBounds(500,80,100,30);
	    layeredPane.add(labelType,JLayeredPane.MODAL_LAYER);
	    txtType = new JComboBox<String>();
	    //Subject selection
	    txtType.addItem("Chinese");
	    txtType.addItem("CS");
	    txtType.addItem("Chemistry");
	    txtType.addItem("English");
	    txtType.addItem("Economics");
	    txtType.setBounds(600,80,300,30);
	    layeredPane.add(txtType,JLayeredPane.MODAL_LAYER);
	    
	    JLabel labelContent = new JLabel("Notes:");
	    labelContent.setBounds(500,130,100,30);
	    layeredPane.add(labelContent,JLayeredPane.MODAL_LAYER);
	    txtContent = new JTextArea();
	    txtContent.setLineWrap(true);
	    txtContent.setWrapStyleWord(true);
	    //Conditions for scrolling bar to appear in JTextArea
	    JScrollPane txtScrollPane = new JScrollPane(txtContent);
	    txtScrollPane.setBounds(600,130,750,500);
		layeredPane.add(txtScrollPane);
		
		//Save button
		saveButton = new JButton("Save");
		saveButton.setBounds(1200,650,80,30);
		saveButton.setBackground(Color.decode("#BE9D61"));
	    layeredPane.add(saveButton,JLayeredPane.MODAL_LAYER);
	    
	    //Schedule button
	    scheduleButton = new JButton("Plan");
	    scheduleButton.setBounds(20,650,120,40);
	    scheduleButton.setBackground(Color.decode("#BE9D61"));
	    layeredPane.add(scheduleButton,JLayeredPane.MODAL_LAYER);
		
		dateJF.setLayeredPane(layeredPane);
	    //dateJF.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		dateJF.setSize(1366, 768);
		//Center
		dateJF.setLocationRelativeTo(null);
		//Removing resizability
		dateJF.setResizable(false);
		dateJF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dateJF.setVisible(true);
		
		//The search bar can listen to input, delete and edit
		Document document = txtSearch.getDocument();
		document.addDocumentListener(new DocumentListener(){

			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				String txt = txtSearch.getText();
				refreshNoteTable("", txt);
			}

			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				String txt = txtSearch.getText();
				refreshNoteTable("", txt);
			}

			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				String txt = txtSearch.getText();
				refreshNoteTable("", txt);
			}
		});
		
		//Clicking on the type (subject) table
		typeTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if(typeTable.getValueAt(typeTable.getSelectedRow(),0)!=null){
					String typeValue = (String) typeTable.getValueAt(typeTable.getSelectedRow(),0);
					//Click type to refresh the note table data
					if("Recentnotes".equals(typeValue.replace(" ", ""))){
						refreshNoteTable("", "");
					}else{
						refreshNoteTable(typeValue, "");
					}
				}
			}
		});
		
		//Clicking on the note table to display the menu
		noteTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if(noteTable.getValueAt(noteTable.getSelectedRow(),0)!=null){
					openId = (int) noteTable.getValueAt(noteTable.getSelectedRow(),0);
					popupMenu.show(noteTable, e.getX(), e.getY());
				}
			}
		});
		
		//Save button action
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	String titleSave = txtTitle.getText();
            	if(!"".equals(titleSave)){
            		String typeSave = txtType.getSelectedItem().toString();
            		String contentSave = txtContent.getText();
            		Notes saveNotes = new Notes();
            		saveNotes.setTitle(titleSave);
            		saveNotes.setType(typeSave);
            		saveNotes.setContent(contentSave);
            		saveNotes.setUsername(userName);
            		//Save note
            		int isSave = notesService.saveNote(saveNotes, openId);
            		//Determining whether if it is saved successfully
            		if(isSave == 1){
            			JOptionPane.showMessageDialog(null, "Your note has been saved", "Tips", JOptionPane.PLAIN_MESSAGE);
            			//Refreshing note table
            			refreshNoteTable("", "");
            			//Clearing title and content
    					txtTitle.setText("");
    					txtContent.setText("");
    					openId = 0;
            		}else{
            			JOptionPane.showMessageDialog(null, "Save failed, please try again later!", "Tips", JOptionPane.ERROR_MESSAGE);
            		}
            	}else{
            		JOptionPane.showMessageDialog(null, "Note title should not be empty!", "Tips", JOptionPane.ERROR_MESSAGE);
            	}
            }
        });
        //Schedule (calendar) button
        scheduleButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		Schedule sd = new Schedule(userName);
            	sd.getMySchedule();
        	}
        });
	}
	
	//Obtaining note file list (for the subject table and the search box)
	public void refreshNoteTable(String type, String title){//type is for searching the type (subject) and title is for searching the title
		Notes note = new Notes();
		//If it is not empty then set the searching condition
		if(!"".equals(type)){
			note.setType(type);
        }else if(!"".equals(title)){
        	note.setTitle(title);
        }
		//Searching data
		List<Notes> noteList = notesService.getNotes(note);
		if(noteList.size()>0){
			//Initializing noteRow according to the size of noteList
	      	noteRow = new Object[noteList.size()][2];
	      	Notes notefor = new Notes();
	      	for(int i=0;i<noteList.size();i++){
	      		notefor = noteList.get(i);
	      		noteRow[i][0] = notefor.getId();
	      		noteRow[i][1] = notefor.getTitle();
	      	}
		}
		//Reload table to refresh the data
		noteModel = new DefaultTableModel(noteRow, noteColumn);
		noteTable.setModel(noteModel);
		//Hiding the first column (id, which is for locating which specific note it is to be deleted and edited)
		noteTable.getColumnModel().getColumn(0).setMinWidth(0);
        noteTable.getColumnModel().getColumn(0).setMaxWidth(0);
        noteTable.getColumnModel().getColumn(0).setPreferredWidth(0);
	}

	//Editing assignment
	public void setEdit(){
		Notes note = new Notes();
		note.setId(openId);
		//Searching data
		List<Notes> noteList = notesService.getNotes(note);
		if(noteList.size()>0){
			Notes idNote = noteList.get(0);
			openId = idNote.getId();
      		txtTitle.setText(idNote.getTitle());
			txtContent.setText(idNote.getContent());
			String typeValue = idNote.getType();
			//Setting the value in the subject selection drop down menu
			if("Chinese".equals(typeValue.replace(" ", ""))){
				txtType.setSelectedIndex(0);
			}else if("CS".equals(typeValue.replace(" ", ""))){
				txtType.setSelectedIndex(1);
			}else if("Chemistry".equals(typeValue.replace(" ", ""))){
				txtType.setSelectedIndex(2);
			}else if("English".equals(typeValue.replace(" ", ""))){
				txtType.setSelectedIndex(3);
			}else if("Economics".equals(typeValue.replace(" ", ""))){
				txtType.setSelectedIndex(4);
			}else if("History".equals(typeValue.replace(" ", ""))){
				txtType.setSelectedIndex(4);
			}
		}
	}
}
