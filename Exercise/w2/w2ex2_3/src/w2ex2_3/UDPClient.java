package w2ex2_3;
import java.net.*;
import java.nio.*;
import java.io.*;
import java.util.*;


public class UDPClient {
	public static void main(String[] args){
		try{
			DatagramSocket cl = new DatagramSocket();
			Scanner keyboard = new Scanner(System.in);
			
			int seq = 1;
			byte[] intToByte = ByteBuffer.allocate(4).putInt(seq).array();
			System.out.println("Please input an integer: ");
			int x = keyboard.nextInt();
			keyboard.skip("\\R");
			byte buffInt[] = String.valueOf(x).getBytes();
			
			byte[] dataInt = new byte[4 + 1 + buffInt.length];
			System.arraycopy(intToByte, 0, dataInt, 0, 4);
			
			dataInt[4] = 1;
			System.arraycopy(buffInt, 0, dataInt, 5, buffInt.length);
			
			InetAddress addsv = InetAddress.getByName("localhost");
			DatagramPacket pInt = new DatagramPacket(dataInt, dataInt.length, addsv, 4567);
			
			cl.send(pInt);
			
			seq++;
			intToByte = ByteBuffer.allocate(4).putInt(seq).array();
			System.out.println("Please input a string: ");
			String text = keyboard.nextLine();
			byte buffText[] = String.valueOf(text).getBytes();
			
			byte[] dataText = new byte[4 + 1 + buffText.length];
			System.arraycopy(intToByte, 0, dataText, 0, 4);
			
			dataText[4] = 0;
			System.arraycopy(buffText, 0, dataText, 5, buffText.length);
			DatagramPacket pStr = new DatagramPacket(dataText, dataText.length, addsv, 4567);
			
			cl.send(pStr);
			cl.close();
		}catch(IOException e){}
	}
}
