package q2;
import java.net.*;
import java.util.*;

public class Client {
	private static final String SERVER_HOST = "localhost";
	private static final int SERVER_PORT = 9999;
	
	private static void sendPacket(DatagramSocket socket, InetAddress addr, int port, String message) throws Exception {
		byte[] data = message.getBytes("UTF-8");
		DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
		socket.send(packet);
	}
	
	public static void main(String[] args)throws Exception{
		DatagramSocket socket = new DatagramSocket();
		InetAddress serverAddr = InetAddress.getByName(SERVER_HOST);
		Scanner scanner = new Scanner(System.in);
		int sequenceNumber = 0;
		
		System.out.println("Connect successfully to server: " + SERVER_HOST + ":" + SERVER_PORT + "\n");
		while(true){
			System.out.println("Sensor Input Menu\n" 
					+ "1. Room Temperature\n"
					+ "2. Humidity\n"
					+ "3. Door status\n"
					+ "4. Exit");
			String choiceStr = scanner.nextLine().trim();
			int choice;
			try{
				choice = Integer.parseInt(choiceStr);
			}catch(NumberFormatException e){
				System.out.println("Please enter again\n");
				continue;
			}
			switch(choice) {
			case 1:
                System.out.print("Enter room temperature (float): ");
                String tempStr = scanner.nextLine().trim();
                try {
                    float  temp = Float.parseFloat(tempStr);
                    String pkt1 = sequenceNumber + " temp " + temp;
                    sendPacket(socket, serverAddr, SERVER_PORT, pkt1);
                    System.out.println("[SENT] \"" + pkt1 + "\"\n");
                    sequenceNumber++;
            
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] Invalid float value. Please try again.\n");
                }
                break;
            case 2:
                System.out.print("Enter humidity (integer, 0-100): ");
                String humStr = scanner.nextLine().trim();
                try {
                    int hum  = Integer.parseInt(humStr);
                    String pkt2 = sequenceNumber + " hum " + hum;
                    sendPacket(socket, serverAddr, SERVER_PORT, pkt2);
                    System.out.println("[SENT] \"" + pkt2 + "\"\n");
                    sequenceNumber++;
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR] Invalid integer value. Please try again.\n");
                    }
                    break;
            case 3:
                System.out.print("Enter door status (OPEN / CLOSED): ");
                String door = scanner.nextLine().trim().toUpperCase();
                if (!door.equals("OPEN") && !door.equals("CLOSED")) {
                    System.out.println("[ERROR] Door status must be OPEN or CLOSED.\n");
                    break;
                }
                String pkt3 = sequenceNumber + " door " + door;
                sendPacket(socket, serverAddr, SERVER_PORT, pkt3);
                System.out.println("[SENT] \"" + pkt3 + "\"\n");
                sequenceNumber++;
                break;
            case 4:
                String endPkt = sequenceNumber + " END ";
                sendPacket(socket, serverAddr, SERVER_PORT, endPkt);
                System.out.println("[SENT] END signal → \"" + endPkt + "\"");
                System.out.println("Client shutting down. Goodbye!");
                socket.close();
                scanner.close();
                return;

                default:
                    System.out.println("[ERROR] Invalid choice. Enter 1, 2, 3, or 4.\n");
			}
		}
		
		
	}
}
