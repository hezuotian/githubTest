

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;

import com.rsa.RSACoder;
 
public class Client implements Runnable {
	String ip;
	int port;

	DataOutputStream dos;
	Socket client;

 

	public Client(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	public void init() {
		try {
			client = new Socket(ip, port);

			// pw=new PrintWriter(client.getOutputStream());

			dos = new DataOutputStream(client.getOutputStream());

		 
			sayHello("ALL");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new Thread(new Recieve_Runnable(client)).start();

	}

	// ������������ ��ʽ(Ŀ���ַ��������ַ����Ϣ���ͣ���Ϣ����)
 
	
	public void sayHello(String target) {
		sendMessage(target, MsgType.JOIN, "",new Date().toLocaleString());
	}


	// ������������
	public void sayBay(String target) {
		sendMessage(target, MsgType.LEFT, "",new Date().toLocaleString());
	}

	// �������������Ϣ  ����ƴ�Ӻõ��ַ���������ǰ�����ģ�
	public String sendMessage(String target, String type, String msg,String date) {

		System.out.println("client:����ǰ��" + msg);
		String unencodeStr = target +MsgType.SPLIT+ ClientStart.nick_text.getText().toString() + MsgType.SPLIT + type + MsgType.SPLIT
				+ msg+MsgType.SPLIT+date;
		//����msg���͵���Ϣ
		if(MsgType.MSG.equals(type)){
			//�ù�Կ����
		;

			try {
				byte[] data = msg.getBytes();
				byte[] encodedData = RSACoder.encryptByPublicKey(data, ClientStart.publicKey);
				
//				System.out.println("encodedData:"+encodedData);
				 
				msg=Util.bytesToString(encodedData);
				System.out.println("client���ܺ�:"+msg);
				
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
		String str = target +MsgType.SPLIT+ ClientStart.nick_text.getText().toString() + MsgType.SPLIT + type + MsgType.SPLIT
						+ msg+MsgType.SPLIT+date;
		new Thread(new Send_Runnable(client, str)).start();
		
		return unencodeStr;
	}

 

	@Override
	public void run() {
		// TODO Auto-generated method stub
		init();
	}
}
