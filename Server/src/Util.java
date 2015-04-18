
public class Util {
	public static String bytesToString(byte[] b){
		String res="";
		for(byte bt:b)
			res+=bt+" ";
		return res;
	}	
	
	public static byte[] StringTobytes(String str){
		String s[]=str.split(" ");
		byte[] res=new byte[s.length];
		for(int i=0;i<s.length;i++){
			res[i]=Byte.parseByte(s[i].trim());
		}
		return res;	
	}
}
