package w1ex2;
import java.io.*;
import java.net.*;

public class TCPServer{
	public static void main(String [] args) {
		try{
			ServerSocket ss = new ServerSocket(8777);
			Socket con = ss.accept();
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			
			double total = 0;
			while (true) {
				String product_name = in.readLine();
				if (product_name.equals("end")) break;
				int quantity = Integer.parseInt(in.readLine());
				float unit_price = Float.parseFloat(in.readLine());
				double product_price = quantity * unit_price;
				total += product_price;
				
				String product = "Product: " + product_name + ", " + "quantity: " + quantity + ", price: " + unit_price;
				System.out.println(product + "\r\n");
				out.write(product+ "\r\n");
				out.flush();
				
			}
			System.out.println("Total: " + total + "\r\n");
			out.write(total + "\r\n");
			out.flush();
			
		}catch (Exception e) {e.printStackTrace();}

	}
}