

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

	// 发送上线请求 格式(目标地址：本机地址：消息类型：消息内容)
 
	
	public void sayHello(String target) {
		sendMessage(target, MsgType.JOIN, "",new Date().toLocaleString());
	}


	// 发送离线请求
	public void sayBay(String target) {
		sendMessage(target, MsgType.LEFT, "",new Date().toLocaleString());
	}

	// 向服务器发送消息  返回拼接好的字符串（加密前的明文）
	public String sendMessage(String target, String type, String msg,String date) {

		System.out.println("client:加密前：" + msg);
		String unencodeStr = target +MsgType.SPLIT+ ClientStart.nick_text.getText().toString() + MsgType.SPLIT + type + MsgType.SPLIT
				+ msg+MsgType.SPLIT+date;
		//加密msg类型的消息
		if(MsgType.MSG.equals(type)){
			//用公钥加密
		;

			try {
				byte[] data = msg.getBytes();
				byte[] encodedData = RSACoder.encryptByPublicKey(data, ClientStart.publicKey);
				
//				System.out.println("encodedData:"+encodedData);
				 
				msg=Util.bytesToString(encodedData);
				System.out.println("client加密后:"+msg);
				
			
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
