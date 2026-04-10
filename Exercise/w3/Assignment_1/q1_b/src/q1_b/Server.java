package q1_b;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 5000;
    private static final int MAX_THREADS = 20;

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("GuessingNum Server is running on port " + PORT);
            System.out.println("Thread pool size = " + MAX_THREADS);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " +
                        clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is handling client " +
                socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

        int RandomNumber = NumberGenerator();

        try (
            DataInputStream in = new DataInputStream(socket.getInputStream());
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            out.write("Connected. Welcome to My Server :D\r\n");
            out.flush();

            while (true) {
                int guessNum = in.readInt();

                if (guessNum == RandomNumber) {
                    out.write("Congratulations! You predicted correctly.\r\n");
                    out.flush();
                    break;
                } else if (guessNum > RandomNumber) {
                    out.write("Your number is bigger. Please predict again.\r\n");
                    out.flush();
                } else {
                    out.write("Your number is smaller. Please predict again.\r\n");
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } finally {
            try {
                System.out.println("Closing connection: " +
                        socket.getInetAddress() + ":" + socket.getPort());
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static int NumberGenerator() {
        int R = (int) (Math.random() * 100);
        return R;
    }
}