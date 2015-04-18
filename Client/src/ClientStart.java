import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JScrollPane;

public class ClientStart extends JDialog {

	private final JPanel contentPanel = new JPanel();
	static JTextField ip_text;
	static JTextField port_text;
	static Client client;
	static JTextField nick_text;
	static DefaultListModel listModel;
	static JButton message_btn;
	static boolean isShake=false;   //消息提示是否在闪动
	static String nowMsg;  //刚接受到的消息
	JButton join_btn;
	static JList friend_list;
	static Map<String,MainWindow> chatwindowMap=new HashMap<String,MainWindow>();   //key 为target
	static String publicKey="";
	boolean isOnline = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ClientStart dialog = new ClientStart();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ClientStart() {
		this.setTitle("客户端启动");
		setBounds(100, 100, 280, 600);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			ip_text = new JTextField();
			ip_text.setText("127.0.0.1");
			ip_text.setBounds(63, 508, 87, 21);
			contentPanel.add(ip_text);
			ip_text.setColumns(10);
		}

		JLabel lblIp = new JLabel("IP:");
		lblIp.setBounds(10, 511, 54, 15);
		contentPanel.add(lblIp);

		JLabel lblPort = new JLabel("port:");
		lblPort.setBounds(10, 536, 54, 15);
		contentPanel.add(lblPort);

		port_text = new JTextField();
		port_text.setText("1234");
		port_text.setBounds(63, 536, 87, 21);
		contentPanel.add(port_text);
		port_text.setColumns(10);

		final JButton btnConn = new JButton("\u4E0A\u7EBF");
		btnConn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// ClientStart.this.setVisible(false);
				if (!isOnline) {
					client = new Client(ip_text.getText(), Integer
							.parseInt(port_text.getText()));
					new Thread(client).start();
					btnConn.setText("离线");
					
					//昵称不可编辑
					nick_text.setEditable(false);
					join_btn.setVisible(true);
					isOnline = true;
				} else {
					btnConn.setText("上线");
					join_btn.setVisible(false);
					isOnline = false;
					listModel.clear();		
					
					//关闭所有聊天窗口
					for(MainWindow mw:chatwindowMap.values()){
						 mw.frame.dispose();
					}
					
					//昵称设置可编辑状态
					nick_text.setEditable(true);
					client.sayBay("ALL");
				}
			}
		});

		btnConn.setBounds(151, 532, 113, 23);
		contentPanel.add(btnConn);

		listModel = new DefaultListModel();

		JLabel label = new JLabel("\u5728\u7EBF\u5217\u8868");
		label.setBounds(10, 10, 54, 15);
		contentPanel.add(label);

		JLabel label_1 = new JLabel("\u6635\u79F0\uFF1A");
		label_1.setBounds(10, 479, 54, 15);
		contentPanel.add(label_1);

		nick_text = new JTextField();
		nick_text.setBounds(65, 476, 85, 21);
		contentPanel.add(nick_text);
		nick_text.setColumns(10);

		join_btn = new JButton("\u521B\u5EFA\u8BA8\u8BBA\u7EC4");
		join_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String target=nick_text.getText().toString();
				for(Object o:friend_list.getSelectedValues()){
					target+=";"+o.toString();
					System.out.println(o.toString());
				}
				chatwindowMap.put(target, new MainWindow(target,MsgType.MUL_MSG));	
			}
		});
		join_btn.setVisible(false);
		join_btn.setBounds(151, 507, 113, 23);
		contentPanel.add(join_btn);
		
		ImageIcon icon=new ImageIcon("images\\message.jpg");
		message_btn = new JButton(icon);
		message_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(isShake){
					isShake=false;
					 String s[]=nowMsg.split(MsgType.SPLIT);
					 String target=s[1];
					 //多人聊天，发送目标设置为多人目标
					 if(s[2].equals(MsgType.MUL_MSG))   target=s[0];
					 MainWindow mainWindow=new MainWindow(target,s[2]);
					 mainWindow.showmessge_text.append(MsgType.handleMsgStr(nowMsg)+"\n");
					 chatwindowMap.put(target, mainWindow);
					
				}
			}
		});
		message_btn.setContentAreaFilled(false);
		message_btn.setBorderPainted(false);
		message_btn.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		message_btn.setBounds(161, 475, 93, 23);
		contentPanel.add(message_btn);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 35, 214, 434);
		contentPanel.add(scrollPane);
		friend_list = new JList(listModel);
		scrollPane.setViewportView(friend_list);
		friend_list.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {

				System.out.println("clicked");
				if (friend_list.getSelectedIndex() != -1) {
//					if (e.getClickCount() == 1)
//
//						  oneClick(friend_list.getSelectedValue());

						if (e.getClickCount() == 2)
					twoClick(friend_list.getSelectedValue());

				}
			}

	 
			private void twoClick(Object value) {
				// 双击处理

				System.out.println("启动与" + value);
				 
				//发送消息  单人
				chatwindowMap.put(value.toString(), new MainWindow(value.toString(),MsgType.MSG));
			}
		});

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(isOnline)	client.sayBay("ALL");
				System.exit(0);	
			}
		});
		
		 
	}
}
