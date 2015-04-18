import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JTextArea;

 

import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;


public class MainWindow {

	private JFrame frame;
	static JTextArea showmessge_text;
	//static JList<String> friend_list;
	static	DefaultListModel listModel;
	private JLabel label;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
 

	/**
	 * Create the application.
	 */
	public MainWindow( ) {
	 
		initialize();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {			
					 
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
	
		frame = new JFrame();
	
		frame.setTitle("·þÎñÆ÷");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		listModel = new DefaultListModel();
		
		label = new JLabel("\u5728\u7EBF\u5217\u8868");
		label.setBounds(340, 14, 54, 15);
		frame.getContentPane().add(label);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 14, 285, 226);
		frame.getContentPane().add(scrollPane);
		
		showmessge_text = new JTextArea();
		scrollPane.setViewportView(showmessge_text);
		showmessge_text.setWrapStyleWord(true);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(339, 38, 85, 202);
		frame.getContentPane().add(scrollPane_1);
	//	friend_list = new JList<String>(listModel);
	//	scrollPane_1.setViewportView(friend_list);
		
  
		
 
	}
}
