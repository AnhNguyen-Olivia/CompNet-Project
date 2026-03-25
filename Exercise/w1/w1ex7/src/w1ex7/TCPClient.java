package w1ex7;
import java.net.*;
import java.io.*;
import java.util.*;

public class TCPClient {
	public static void main(String [] args){
		try{
			Scanner sc = new Scanner(System.in);
			Socket s = new Socket("localhost", 6769);
			BufferedWriter Network_out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			BufferedReader Network_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			System.out.println("Enter a text (or type 'bye' to end): ");
			while (true) {
				String text = sc.nextLine();
				if (text.equals("bye")) break;
				
				Network_out.write(text + "\r\n");
				Network_out.flush();
				
				String serverText = Network_in.readLine();
				if(serverText.equals("bye")) break;
				System.out.println("Server: " + serverText);
			}
			s.close();
		}catch(IOException e){e.printStackTrace();}
	}
}
