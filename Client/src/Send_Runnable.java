

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Send_Runnable implements Runnable {

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
