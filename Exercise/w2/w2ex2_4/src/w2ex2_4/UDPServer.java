package w2ex2_4;
import java.net.*;
import java.nio.*;
import java.io.*;

public class UDPServer {
	public static void main(String[] args){
		try{
			DatagramSocket sv = new DatagramSocket(4567);
			byte[] receiveData = new byte[1500];
				while(true){
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					sv.receive(receivePacket);
					
					byte[]packetData = receivePacket.getData();
					int seq = ByteBuffer.wrap(packetData, 0 , 4).getInt();
					
					byte dataType = packetData[4];
					
					byte[] data = new byte[receivePacket.getLength() - 5];
					
					System.arraycopy(packetData, 5, data, 0, data.length);
					
					if(dataType == 0){
						String receiveMessage = new String(data);
						System.out.println("Receive sequence number: " + seq);
						System.out.println("Receive message(String): " + receiveMessage);
					}else if(dataType == 1){
						int receivedInt = Integer.parseInt(new String(data));
						System.out.println("Received sequence number: " + seq);
						System.out.println("Receive message(Integer): " + receivedInt);
					}
				}
		}catch(IOException e){} 
	}
}	