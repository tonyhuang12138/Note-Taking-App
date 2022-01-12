package code.frame;

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import code.domain.Users;
import code.service.UsersService;
import code.service.LoginService;

import java.awt.event.ActionListener;
import java.util.List;

//Login
//The user can login by entering the username and password and clicking the login button,
//and checking the whether if they match the information in the database 
//(especially the password, as it needs to be encrypted then compared)
public class Login {
	//Login interface, this is the first interface the user will see
	private JFrame loginJF;
	//Creating a JLayeredPane for layering the different components
	private JLayeredPane layeredPane;
	//Creating a panel and a label for storing background images
	private JPanel backgroundJP;
	private JLabel backgroundJL;
	private ImageIcon backgroundImg;
	
	private JTextField txtName;
	private JPasswordField txtPass;
	//Polymorphism
	private UsersService usersService = new LoginService();
	
	//Creating the login JFrame window
	public void toLogin(){
		//Setting the name of the JFrame
		loginJF = new JFrame("Notebook System Login");//This can be seen on the top of the frame
		layeredPane = new JLayeredPane();
		//Background image setting
		backgroundImg = new ImageIcon("src/img/background.jpg");
		backgroundJP = new JPanel();
		backgroundJP.setBounds(0,0,backgroundImg.getIconWidth(),backgroundImg.getIconHeight());    
		backgroundJL = new JLabel(backgroundImg);
		backgroundJP.add(backgroundJL);
		layeredPane.add(backgroundJP,JLayeredPane.DEFAULT_LAYER);//Loaded in the bottom layer as this is the background
		
		//Username and password components
	    JLabel labelName = new JLabel("User Name:");
	    labelName.setBounds(300,150,100,100);
	    layeredPane.add(labelName,JLayeredPane.MODAL_LAYER);
	    
	    txtName = new JTextField();
	    txtName.setBounds(380,180,150,30);
	    layeredPane.add(txtName,JLayeredPane.MODAL_LAYER);
	    
	    JLabel labelPass = new JLabel("Password:");
	    labelPass.setBounds(300,200,100,100);
	    layeredPane.add(labelPass,JLayeredPane.MODAL_LAYER);
	    
	    txtPass = new JPasswordField();
	    txtPass.setBounds(380,230,150,30);
	    layeredPane.add(txtPass,JLayeredPane.MODAL_LAYER);
	    
	    //Buttons
	    JButton loginButton = new JButton("Sign in");
	    loginButton.setBounds(280,300,80,30);
	    layeredPane.add(loginButton,JLayeredPane.MODAL_LAYER);
	    
	    JButton closeButton = new JButton("Close");
	    closeButton.setBounds(365,300,80,30);
	    layeredPane.add(closeButton,JLayeredPane.MODAL_LAYER);
	    
	    JButton registerButton = new JButton("Register");
	    registerButton.setBounds(450,300,85,30);
	    layeredPane.add(registerButton,JLayeredPane.MODAL_LAYER);
	    
	    //Window settings
		loginJF.setLayeredPane(layeredPane);
		//loginJF.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//Setting the height and width to be the same as the picture
		loginJF.setSize(backgroundImg.getIconWidth(),backgroundImg.getIconHeight());
		//Center
		loginJF.setLocationRelativeTo(null);
		//Setting the way to close the frame
		loginJF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Allowing the JFrame to be seen
		loginJF.setVisible(true);
		
		//Login button action
		//The user can login by entering the username and password and clicking the login button,
		//and checking the whether if they match the information in the database 
		//(especially the password, as it needs to be encrypted then compared)
		loginButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String userName = txtName.getText();
                String password = txtPass.getText();
                Users user = new Users(userName, password);
                List<Users> userList = usersService.getUsers(user);
                //Determining whether if user information can be found by using the username and password
                //The length of userList is greater than 0，then the user can be found，
                //which means the username and password have been inputted correctly,
                //therefore, login success
                if(userList.size()>0){
                    //Login success, close the login jFrame and open the list JFrame
                	loginJF.setVisible(false);
                	DataList dl = new DataList(userName);
                	//Loading data
                	dl.getList();
                } else{
                //The length of userList is 0，then the user cannot be found，
                //which means the username and password have been inputted incorrectly,
                //therefore, login failed
                	//Display error message
                	JOptionPane.showMessageDialog(null, "Error in username or password, please re-enter !", "Tips", JOptionPane.ERROR_MESSAGE);
                	//Clearing the search box
                	txtName.setText("");
                	txtPass.setText("");
                }
            }
        });
        //Close button action
        closeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        //Register button action
        registerButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	Register rr = new Register();
        		rr.openRegister();
            }
        });
		
	}
}
