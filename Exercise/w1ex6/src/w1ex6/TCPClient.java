package w1ex6;
import java.net.*;
import java.io.*;
import java.util.*;

public class TCPClient {
	public static void main(String [] args){
		try{
			Scanner sc = new Scanner(System.in);
			Socket s = new Socket("localhost", 8080);
			BufferedWriter Network_out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			BufferedReader Network_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			System.out.println("Enter a text (or type 'quit' to quit): ");
			while (true) {
				String text = sc.nextLine();
				if (text.equals("quit")) break;
				
				Network_out.write(text + "\r\n");
				Network_out.flush();
				
				String echo = Network_in.readLine();
				System.out.println("Echo: " + echo);
			}
			s.close();
		}catch(IOException e){e.printStackTrace();}
	}
}