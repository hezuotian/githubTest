import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;

 

import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;


public class MainWindow {

    JFrame frame;
	JTextArea send_text;
    JTextArea showmessge_text;
	JButton send_btn;
//	Client client;
	String target;  //多个发送目标用分号隔开
	private JLabel my_nickname_label;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	String msgType;
	/**
	 * Create the application.
	 */
	public MainWindow(String target,String msgType) {
		this.target=target;
		this.msgType=msgType;
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
		
//		client=new Client(ip,port);
//		new Thread(client).start();
	 
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("与"+target+"聊天、");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		send_btn = new JButton("SEND");
 
 
		send_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String str=ClientStart.client.sendMessage(target,msgType,send_text.getText(),new Date().toLocaleString());	
//				showmessge_text.append("me:"+new Date()+"\n");
				showmessge_text.append(MsgType.handleMsgStr(str));
				send_text.setText("");
			}
		});
		send_btn.setBounds(331, 218, 93, 23);
		frame.getContentPane().add(send_btn);
		
		my_nickname_label = new JLabel(ClientStart.nick_text.getText().toString());
		my_nickname_label.setBounds(354, 14, 54, 15);
		frame.getContentPane().add(my_nickname_label);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 200, 320, 51);
		frame.getContentPane().add(scrollPane);
		
		send_text = new JTextArea();
		scrollPane.setViewportView(send_text);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 14, 320, 177);
		frame.getContentPane().add(scrollPane_1);
		
		showmessge_text = new JTextArea();
		scrollPane_1.setViewportView(showmessge_text);
		showmessge_text.setEditable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				ClientStart.chatwindowMap.remove(target);
			}
			
		});
	}
	
	
 
	
}
