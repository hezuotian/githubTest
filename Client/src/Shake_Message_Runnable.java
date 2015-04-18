import javax.swing.ImageIcon;


public class Shake_Message_Runnable implements Runnable {
	
	ImageIcon icon[]=new ImageIcon[2];
		


	public Shake_Message_Runnable() {
		icon[0]=new ImageIcon("images\\message.jpg");
		icon[1]=new ImageIcon("images\\blank.jpg");
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		ClientStart.isShake=true;
		int i=0;
		while(ClientStart.isShake){
			i=i%2;
			ClientStart.message_btn.setIcon(icon[i]);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		ClientStart.message_btn.setIcon(icon[0]);
	
	}

}
