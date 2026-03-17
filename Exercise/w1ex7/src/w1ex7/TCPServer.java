package w1ex7;
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class TCPServer {

	public static void main(String [] args){
		try{
			ServerSocket ss = new ServerSocket(6769);
			Socket con = ss.accept();
			Scanner scanner = new Scanner(System.in);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while (true) {
				String clientText = in.readLine();
				if (clientText == null || clientText.equals("bye")) break;
				System.out.println("Client: " + clientText);
				
				String serverText = scanner.nextLine();
				if(serverText.equals("bye")) break;
				out.write(serverText + "\r\n");
				out.flush();
			}
				
			con.close();
		}catch(IOException e){e.printStackTrace();}
	}
}