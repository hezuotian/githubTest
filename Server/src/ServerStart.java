import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.border.BevelBorder;

import java.awt.Color;


public class ServerStart extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField port_text;
	static Server server;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ServerStart dialog = new ServerStart();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ServerStart() {
		setTitle("\u670D\u52A1\u5668\u542F\u52A8\u7A0B\u5E8F");
		setBounds(100, 100, 247, 97);
  		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblPort = new JLabel("port:");
			lblPort.setBounds(10, 23, 54, 15);
			contentPanel.add(lblPort);
		}
		{
			port_text = new JTextField();
			port_text.setText("1234");
			port_text.setBounds(74, 21, 66, 18);
			contentPanel.add(port_text);
			port_text.setColumns(10);
		}
		{
			JButton btnStart = new JButton("Start");
			btnStart.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
								
					server=new Server(Integer.parseInt(port_text.getText()));
					new Thread(server).start();
					ServerStart.this.setVisible(false);
					new MainWindow();
				}
			});
			btnStart.setBounds(150, 19, 66, 23);
			contentPanel.add(btnStart);
		}
	}

}
