package w2ex2_2;
import java.net.*;
import java.io.*;
import java.util.*;

public class UDPClient {
	public static void main(String[] args){
		try{
			DatagramSocket cl = new DatagramSocket();
			Scanner keyboard = new Scanner(System.in);
			System.out.println("Please input an integer: ");
			int x = keyboard.nextInt();
			byte buff[] = String.valueOf(x).getBytes();
			InetAddress addsv = InetAddress.getByName("localhost");
			DatagramPacket p = new DatagramPacket(buff, buff.length, addsv, 4567);
			cl.send(p);
			
			byte buff2[] = new byte[1500];
			DatagramPacket I = new DatagramPacket(buff2, buff2.length);
			cl.receive(I);
			String data = new String(I.getData(), 0, I.getLength());
			System.out.println("Result: " + data);
			
			cl.close();
		}catch(IOException e){}
	}
}
