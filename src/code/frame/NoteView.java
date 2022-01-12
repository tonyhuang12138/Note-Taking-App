package code.frame;

import java.awt.Color;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import code.domain.Notes;
import code.service.NotesService;
//This is the view interface for viewing the notes on fullscreen only
//DataList's view function and ScheduleNote's view function all use this class
public class NoteView {

	private JFrame noteJF;
	//Creating a JLayeredPane for layering
	private JLayeredPane layeredPane;
	
	private int openId;
	private JLabel txtTitle;//Title input box
	private JLabel txtContent;//Note content input box
	
	private NotesService notesService = new NotesService();
	
	public NoteView(int openId){
		this.openId = openId;
	}
	//Generating JFrame, loading component, and assigning values for the components
	public void openNote(){
		noteJF = new JFrame("Notebook View");
		noteJF.setBackground(Color.decode("#FCF4DC"));
		layeredPane = new JLayeredPane();
		
		txtTitle = new JLabel("", JLabel.CENTER);
		txtTitle.setBounds(10,10,1200,40);
		layeredPane.add(txtTitle,JLayeredPane.MODAL_LAYER);
		
		txtContent = new JLabel("", JLabel.NORTH_EAST);
		txtContent.setHorizontalAlignment(SwingConstants.LEFT);
		txtContent.setVerticalAlignment(SwingConstants.TOP);
		JScrollPane noteScrollPane = new JScrollPane(txtContent);
		noteScrollPane.setBounds(10,50,1300,600);
		layeredPane.add(noteScrollPane);
		
		noteJF.setLayeredPane(layeredPane);
		//noteJF.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		noteJF.setSize(1366, 768);
		//Center
		noteJF.setLocationRelativeTo(null);
		//Disabling adjustable window size
		noteJF.setResizable(false);
		noteJF.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		noteJF.setVisible(true);
		setNoteValue();
	}
	
	//Searching note
	public void setNoteValue(){
		Notes note = new Notes();
		note.setId(openId);
		//Searching data
		List<Notes> noteList = notesService.getNotes(note);
		if(noteList.size()>0){
			Notes idNote = noteList.get(0);
			txtTitle.setText(idNote.getTitle());
			txtContent.setText(idNote.getContent());
		}
	}
}
