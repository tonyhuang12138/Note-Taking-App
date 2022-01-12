package code.frame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import code.domain.Users;
import code.service.UsersService;

//Register
public class Register {

	private JFrame registerJF;
	private JLayeredPane layeredPane;
	private JTextField txtUser;
	private JPasswordField txtPass;
	private JPasswordField txtRpass;
	
	private UsersService usersService = new UsersService();
	
	public void openRegister() {
		registerJF = new JFrame("User Registration");
		registerJF.setBackground(Color.decode("#BE9D61"));
		layeredPane = new JLayeredPane();
		
	    JLabel labelName = new JLabel("User Name:");
	    labelName.setBounds(50,50,80,30);
	    layeredPane.add(labelName,JLayeredPane.MODAL_LAYER);
		txtUser = new JTextField();
		txtUser.setBounds(150,50,250,30);
		layeredPane.add(txtUser,JLayeredPane.MODAL_LAYER);
		
		JLabel labelPass = new JLabel("Password:");
		labelPass.setBounds(50,120,80,30);
	    layeredPane.add(labelPass,JLayeredPane.MODAL_LAYER);
		txtPass = new JPasswordField();
		txtPass.setBounds(150,120,250,30);
		layeredPane.add(txtPass,JLayeredPane.MODAL_LAYER);
		
		JLabel labelRpass = new JLabel("Again:");
		labelRpass.setBounds(50,190,80,30);
	    layeredPane.add(labelRpass,JLayeredPane.MODAL_LAYER);
		txtRpass = new JPasswordField();
		txtRpass.setBounds(150,190,250,30);
		layeredPane.add(txtRpass,JLayeredPane.MODAL_LAYER);
		
		JButton registerButton = new JButton("Register");
		registerButton.setBounds(150,280,85,30);
	    layeredPane.add(registerButton,JLayeredPane.MODAL_LAYER);
	    
	    JButton closeButton = new JButton("Close");
	    closeButton.setBounds(250,280,80,30);
	    layeredPane.add(closeButton,JLayeredPane.MODAL_LAYER);
		
		registerJF.setLayeredPane(layeredPane);
		registerJF.setSize(500, 400);
		registerJF.setLocationRelativeTo(registerJF.getOwner());  
		registerJF.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		registerJF.setVisible(true);
		
		registerButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	String userName = txtUser.getText();
            	String password = txtPass.getText();
            	String rpassword = txtRpass.getText();
            	if("".equals(userName)){
            		JOptionPane.showMessageDialog(null, "enter one user name !", "Tips", JOptionPane.ERROR_MESSAGE);
            		return;
            	}
            	if("".equals(password)){
            		JOptionPane.showMessageDialog(null, "Please input a password !", "Tips", JOptionPane.ERROR_MESSAGE);
            		return;
            	}
            	if("".equals(rpassword)){
            		JOptionPane.showMessageDialog(null, "Please enter a confirmation password !", "Tips", JOptionPane.ERROR_MESSAGE);
            		return;
            	}
            	if(!password.equals(rpassword)){
            		JOptionPane.showMessageDialog(null, "The inputs are inconsistent!", "Tips", JOptionPane.ERROR_MESSAGE);
            		txtPass.setText("");
            		txtRpass.setText("");
            		return;
            	}
            	Users user = new Users();
            	user.setUsername(userName);
            	List<Users> userList = usersService.getUsers(user);//This is a lot more efficient than looping and checking comparing with every element
            	if(userList.size()>0){
            		JOptionPane.showMessageDialog(null, "User name already exists !", "Tips", JOptionPane.ERROR_MESSAGE);
            		txtUser.setText("");
            		return;
            	}
            	user.setPassword(password);
            	int isSave = usersService.saveUser(user);
        		if(isSave == 1){
        			JOptionPane.showMessageDialog(null, "Registration is successful !", "Tips", JOptionPane.PLAIN_MESSAGE);
        			registerJF.setVisible(false);
        		}else{
        			JOptionPane.showMessageDialog(null, "Registration failed, please try again later !", "Tips", JOptionPane.ERROR_MESSAGE);
        		}
            }
        });
        closeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	registerJF.setVisible(false);
            }
        });
	}
	
}
