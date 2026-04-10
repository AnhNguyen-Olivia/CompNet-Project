package w4ex2;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class W4ex2a_Server{
	private static final int PORT = 5000;
	private static final int MAX_THREADS = 3;
	
	public static void main(String[] args){
		ExecutorService pool =  Executors.newFixedThreadPool(MAX_THREADS);
		
		try(
			ServerSocket serverSocket = new ServerSocket(PORT)
		){
			System.out.println("Uppercase Server is running on port " + PORT);
			System.out.println("Thread pool size = " + MAX_THREADS);
			
			while(true){
				Socket clientSocket = serverSocket.accept();
				System.out.println("New client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
				
				pool.execute(new ClientHandler(clientSocket));
			}
		}catch(IOException e){
			e.printStackTrace();
		} finally{
			pool.shutdown();
		}
	}
}

class ClientHandler implements Runnable{
	private final Socket socket;
	
	public ClientHandler(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run(){
		System.out.println(Thread.currentThread().getName() + " is handling client " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
		
		try(
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
		){
			out.println("Connected. Welcome to My Server :D");
			String line = in.readLine();
			System.out.println(line);
			out.println(line.toUpperCase());
			System.out.println(line.toUpperCase());
		}catch(IOException e){
			System.out.println("Client disconnected: " + e.getMessage());
		}finally{
			try{
				System.out.println("Closing connection: " + socket.getInetAddress() + ":" + socket.getPort());
				socket.close();
			}catch(IOException e){e.printStackTrace();}
		}
	}
}