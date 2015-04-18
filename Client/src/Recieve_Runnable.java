

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import com.rsa.RSACoder;

public class Recieve_Runnable implements Runnable {
	Socket client;
	static Thread shake_thread;
	public Recieve_Runnable(Socket client) {
		super();
		this.client = client;
		shake_thread=new Thread(new Shake_Message_Runnable());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataInputStream dis = new DataInputStream(
					client.getInputStream());
			while (true) {
				String str = dis.readUTF();
				
				handMsg(str);
				System.out.println("recieve:" + str);

				// MainWindow.showmessge_text.append(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void handMsg(String str){
		String s[]=str.split(MsgType.SPLIT);
		switch(s[2]){
			case MsgType.JOIN:
				if(!ClientStart.listModel.contains(s[1])){
					ClientStart.listModel.addElement(s[1]);
					ClientStart.friend_list.invalidate();
					ClientStart.client.sayHello(s[1]);
				}
				break;
			case MsgType.LEFT:		 
				ClientStart.listModel.removeElement(s[1]);
				break;
			case MsgType.MSG:	
				//没有这个窗口
				String msg=s[3];
				System.out.println("client:解密前："+msg);
				try {
					byte[] dencodedData=RSACoder.decryptByPublicKey(Util.StringTobytes(msg), ClientStart.publicKey);
					msg=new String(dencodedData);
				System.out.println("client解密后："+msg);
				str=s[0] +MsgType.SPLIT+ s[1] + MsgType.SPLIT +s[2] + MsgType.SPLIT
						+ msg+MsgType.SPLIT+s[4];;
				} catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!ClientStart.chatwindowMap.containsKey(s[1])){
					ClientStart.nowMsg=str;
					shake_thread.run();
				}else{
					MainWindow mainWindow=ClientStart.chatwindowMap.get(s[1]);
					mainWindow.showmessge_text.append(MsgType.handleMsgStr(str));
				};
				break;
			case MsgType.MUL_MSG:
				//没有这个窗口，多人消息
				if(!ClientStart.chatwindowMap.containsKey(s[0])){
					ClientStart.nowMsg=str;
					shake_thread.run();
				}else{
					MainWindow mainWindow=ClientStart.chatwindowMap.get(s[0]);
					mainWindow.showmessge_text.append(MsgType.handleMsgStr(str));
				};
			
				break;
			case MsgType.PKEY:
				System.out.println("getPublicKey:"+s[3]);
				ClientStart.publicKey=s[3];
				break;
		}
	}
}
