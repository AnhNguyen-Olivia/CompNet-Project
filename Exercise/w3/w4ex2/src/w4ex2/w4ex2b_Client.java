package w4ex2;
import java.net.*;
import java.util.*;

public class w4ex2b_Client{
	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 5000;
	private static final int BUFFER_SIZE = 1500;
	
	public static void main(String[] args){
		try(
			DatagramSocket clientSocket = new DatagramSocket();
			Scanner scanner = new Scanner(System.in)
			){
				System.out.println("UDP Uppercase Client Started.");
				System.out.println("Enter the string: ");
				System.out.print("> ");
				String message = scanner.nextLine();
				
				byte[] sendBuffer = message.getBytes();
				InetAddress address = InetAddress.getByName(SERVER_ADDRESS);
				
				DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, SERVER_PORT);
				
				clientSocket.send(sendPacket);
				
				byte[] receiveBuffer = new byte[BUFFER_SIZE];
				DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
				
				try{
					clientSocket.receive(receivePacket);
					String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
					System.out.println("Server says: " + response);
				}catch(SocketTimeoutException e){
					System.out.println("Error: Server timed out. (Packet might be lost)");
			}
		}catch(Exception e){
				e.printStackTrace();
		}
	} 
}