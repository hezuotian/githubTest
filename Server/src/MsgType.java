
public class  MsgType {
	public static final String JOIN="JOIN";
	public static final String LEFT="LEFT";
	public static final String MSG="MSG";
	public static final String MUL_MSG="MUL_MSG";  //Ⱥ��
	public static final String PKEY="PKEY";		//��Կ
	public static final String SPLIT="#";
	
	//��Ϣ��ʽ   target:me:msgtype:msg:date
	public static String handleMsgStr(String str){
		String s[]=str.split(SPLIT);
		return s[1]+"\t"+s[4]+"\n"+s[3]+"\n";
	}
	
 
}