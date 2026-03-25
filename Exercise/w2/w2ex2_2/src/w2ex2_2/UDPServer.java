package w2ex2_2;
import java.net.*;
import java.io.*;
public class UDPServer {
	public static void main(String[] args){
		try{
			DatagramSocket sv = new DatagramSocket(4567);
			
			byte buff[] = new byte[1500];
			DatagramPacket q = new DatagramPacket(buff, buff.length);
			sv.receive(q);
			
			String data = new String(q.getData(), 0, q.getLength());
			int x = Integer.parseInt(data);
			String result = (x%2 == 0)? "even number" : "odd number";
			
			byte buff2[] = new byte[1500];
			buff2 = result.getBytes();
			InetAddress addcl = q.getAddress();
			int portcl = q.getPort();
			DatagramPacket k = new DatagramPacket(buff2, buff2.length, addcl, portcl);
			sv.send(k);
		}catch(IOException e){} 
	}
}	
