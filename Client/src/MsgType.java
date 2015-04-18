
public class  MsgType {
	public static final String JOIN="JOIN";
	public static final String LEFT="LEFT";
	public static final String MSG="MSG";
	public static final String MUL_MSG="MUL_MSG";  //群聊
	public static final String PKEY="PKEY";		//公钥
	public static final String SPLIT="#";
	
	//消息格式   target:me:msgtype:msg:date
	public static String handleMsgStr(String str){
		String s[]=str.split(SPLIT);
		return s[1]+"\t"+s[4]+"\n"+s[3]+"\n";
	}
	
 
}