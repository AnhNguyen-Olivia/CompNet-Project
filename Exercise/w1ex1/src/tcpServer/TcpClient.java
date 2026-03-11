package tcpServer;
import java.net.*;
import java.io.*;
import java.util.*;

public class TcpClient {
	public static void main(String [] args) {
		try{
			Socket s = new Socket("localhost", 1234);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Please input a string: ");
			String data = keyboard.nextLine();
			
			out.write(data + "\r\n");
			out.flush();
			String result = in.readLine();
			System.out.println("Data from Server: " + result);
			
			s.close();
		}catch(Exception e){e.printStackTrace();}
	}
}
