package w4ex2;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.*;

public class w4ex2b_Server{
	private static final int PORT = 5000;
	private static final int MAX_THREADS = 3;
	private static final int BUFFER_SIZE = 1500;
	
	public static void main(String[] args){
		ExecutorService workerThreadPool = Executors.newFixedThreadPool(MAX_THREADS);
		
		try(DatagramSocket serverSocket = new DatagramSocket(PORT)){
			System.out.println("UDP Uppercase Server runnong on port " + PORT);
			
			while(true){
				byte[] receiveBuffer = new byte[BUFFER_SIZE];
				DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
				
				serverSocket.receive(receivePacket);
				
				String expression = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
				InetAddress clientAddress = receivePacket.getAddress();
				int clientPort = receivePacket.getPort();
				
				System.out.println("Received from " + clientAddress + ":" + clientPort + " -> " + expression);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			workerThreadPool.shutdown();	
		}
	}
}

class UDPWorkerTask implements Runnable{
	private final DatagramSocket socket;
	private final InetAddress address;
	private final int port;
	private final String message;
	
	public UDPWorkerTask(DatagramSocket socket, InetAddress address, int port, String message){
		this.socket = socket;	
		this.address = address;
		this.port = port;
		this.message = message;
	}
	
	@Override
	public void run(){
		String result = message.toUpperCase();
		byte[] sendData = result.getBytes();
		
		try{
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
			socket.send(sendPacket);
			System.out.println(Thread.currentThread().getName() + " sent result to " + address);
		}catch(Exception e){
			System.err.println("Failed to send UDP response: " + e.getMessage());
		}
	}
}
