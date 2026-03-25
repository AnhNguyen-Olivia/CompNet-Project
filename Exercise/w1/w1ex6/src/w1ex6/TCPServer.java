package w1ex6;
import java.net.*;
import java.io.*;

public class TCPServer {

	public static void main(String [] args){
		try{
			ServerSocket ss = new ServerSocket(8080);
			Socket con = ss.accept();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while (true) {
				String clientText = in.readLine();
				if (clientText == null || clientText.equals("quit")) break;
				
				out.write(clientText + "\r\n");
				out.flush();
			}
				
			con.close();
			ss.close();
		}catch(IOException e){e.printStackTrace();}
	}
}