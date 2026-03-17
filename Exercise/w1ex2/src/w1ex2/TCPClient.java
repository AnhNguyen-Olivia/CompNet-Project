package w1ex2;
import java.net.*;
import java.io.*;
import java.util.*;

public class TCPClient {
	public static void main(String [] args) {
		try{
			Socket s = new Socket("localhost", 8777);
			s.setSoTimeout(5000);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
	
			Scanner keyboard = new Scanner(System.in);
			
			while (true) {
				System.out.println("Please enter the product name: ");
				String product_name = keyboard.nextLine();
				out.write(product_name + "\r\n");
				out.flush();
				if (product_name.equals("end")) break;

				System.out.println("Please enter the quantity: ");
				String quantity = keyboard.nextLine();
				out.write(quantity + "\r\n");
				out.flush();
				
				System.out.println("Please enter the price: ");
				String unit_price = keyboard.nextLine();
				out.write(unit_price + "\r\n");
				out.flush();
				
				System.out.println(in.readLine() + "\r\n");
			}

			String total = in.readLine();
			System.out.println("Total: " + total);
			s.close();
		}catch(Exception e){e.printStackTrace();}
	}
}
