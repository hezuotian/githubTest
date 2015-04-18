import java.awt.Component;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.rsa.RSACoder;

public class Server implements Runnable {
	int port;
	Map<String,Socket> friendmap= new HashMap<String,Socket>();
	Map<String,String> privateKeys=new HashMap<String,String>();

	public Server(int port) {
		super();
		this.port = port;

	}

	static ServerSocket server;
	//static Socket client;
	static MainWindow chatWindow;

	// static DataOutputStream dos;
	// static DataInputStream dis;
	class Send_Runnable implements Runnable {
		Socket client;
		String msg;
		public Send_Runnable(Socket client,String msg){
			this.client=client;
			this.msg=msg;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				DataOutputStream dos=new DataOutputStream(client.getOutputStream());		 
					dos.writeUTF(msg);
					dos.flush();
			 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	class Recieve_Runnable implements Runnable {
		Socket client;
		public Recieve_Runnable(Socket client){
			this.client=client;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
			 
				DataInputStream din = new DataInputStream(
						client.getInputStream());
				while (true) {
					String str = din.readUTF();
					System.out.println(str);
					chatWindow.showmessge_text.append(str+"\n");

					handleRecieveMsg(str,client);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	public void init() {
		// TODO Auto-generated method stub

		try {
			server = new ServerSocket(port);
			System.out.println("服务器已启动");

			while (true) {
				Socket client=server.accept();
				new Thread(new Recieve_Runnable(client)).start();
			}

			// dos=new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//str代表完整的发送信息
	public void sendMsg(String target,String str){
		String s[]=str.split(MsgType.SPLIT);
		if(MsgObject.ALL.equals(target)){
			for(String nick_name:friendmap.keySet()){
				//'ALL'不包含自己
				 if(!s[1].equals(nick_name)){	
					 Socket client=friendmap.get(nick_name);
					 new Thread(new Send_Runnable(client,str)).start();
				 }
			}
		}else{
			Socket client;
			switch(s[2]){
				//对消息加密处理
				case MsgType.MSG:
					String priKey=privateKeys.get(s[0]);	
				try {
					byte[] encodedData=RSACoder.encryptByPrivateKey(s[3].getBytes(), priKey);
					String msg=Util.bytesToString(encodedData);
					System.out.println("server:加密后:"+msg);
					//重新拼接字符串
					str= s[0] +MsgType.SPLIT+s[1] + MsgType.SPLIT + s[2] + MsgType.SPLIT
							+ msg+MsgType.SPLIT+s[4];
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				client=friendmap.get(target);
				new Thread(new Send_Runnable(client,str)).start();
					break;
				case MsgType.MUL_MSG:
					String t[]=target.split(";");
					for(String tar:t){
						 client=friendmap.get(tar);
						//不对自己发送
						if(!s[1].equals(tar))new Thread(new Send_Runnable(client,str)).start();
					}
					break;
				default:
					client=friendmap.get(target);
					new Thread(new Send_Runnable(client,str)).start();
					break;
			}			
		}
	}

	
	//消息格式   target:me:msgtype:msg:date
	public void handleRecieveMsg(String str,Socket client) {
		String s[] = str.split(MsgType.SPLIT);

		switch (s[2]) {
		case MsgType.JOIN:
			if(!friendmap.containsKey(s[1])){
				friendmap.put(s[1],client);
				MainWindow.listModel.addElement(s[1]);
//				MainWindow.friend_list.validate();
				
				//给所有人发送消息请求，表示第一次登陆服务器
				if(s[0].equals(MsgObject.ALL)){
					Map<String, Object> keyMap;
					try {
						keyMap = RSACoder.initKey();	
						String publicKey = RSACoder.getPublicKey(keyMap);
						String privateKey = RSACoder.getPrivateKey(keyMap);
					
						privateKeys.put(s[1], privateKey);
					
					String pubKeymsg= s[1] +MsgType.SPLIT+ MsgObject.SERVER + MsgType.SPLIT + MsgType.PKEY + MsgType.SPLIT
							+ publicKey+MsgType.SPLIT+new Date().toLocaleString();
					//发回 公钥
					sendMsg(s[1],pubKeymsg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				}
			}
			break;
		case MsgType.LEFT:
			friendmap.remove(s[1]);
			MainWindow.listModel.removeElement(s[1]);
//			MainWindow.friend_list.validate();
			break;
		case MsgType.MSG:
			//对MSG类型加密
			String msg=s[3];
			System.out.println("server解密前："+msg);
			String priKey=privateKeys.get(s[1]);
			try {
				 
				byte[] decodedData=RSACoder.decryptByPrivateKey(Util.StringTobytes(msg), priKey);
				msg=new String(decodedData);
			System.out.println("server解密后："+msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//重新拼接字符串
			str= s[0] +MsgType.SPLIT+s[1] + MsgType.SPLIT + s[2] + MsgType.SPLIT
					+ msg+MsgType.SPLIT+s[4];
			break;
		default:
			break;
		}
		sendMsg(s[0],str);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		init();
	}
}
