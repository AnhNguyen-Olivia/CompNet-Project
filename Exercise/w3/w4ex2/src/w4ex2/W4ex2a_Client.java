package w4ex2;
import java.io.*;
import java.net.*;
import java.util.*;

public class W4ex2a_Client{
	public static void main(String[] args){
		String serverAddress = "localhost";
		int serverPort = 5000;
		
		try(
			Socket socket = new Socket(serverAddress, serverPort);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			Scanner scanner = new Scanner(System.in)
		){
			String welcomeMessage = in.readLine();
			System.out.println("Server: " + welcomeMessage);
			System.out.print("Enter your string: ");
			String words = scanner.nextLine();
			out.println(words);
			String response = in.readLine();
			System.out.println("Server: " + response);
			socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
