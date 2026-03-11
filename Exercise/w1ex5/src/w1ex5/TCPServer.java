package w1ex5;
import java.net.*;
import java.io.*;

public class TCPServer {

	public static void main(String [] args){
		try{
			ServerSocket ss = new ServerSocket(1000);
			Socket con = ss.accept();
				
			DataInputStream in = new DataInputStream(con.getInputStream());
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
				
			int num1 = in.readInt();
			int num2 = in.readInt();
				
			for(int i = num1; i <= num2; i++){
				if(i % 2 == 0){
					out.writeInt(i);
					out.flush();
				}
			}
				
			con.close();
		}catch(Exception e){e.printStackTrace();}
	}
}