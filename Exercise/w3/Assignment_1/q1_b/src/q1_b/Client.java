package q1_b;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 5000;

        try (
            Socket socket = new Socket(serverAddress, serverPort);
            BufferedReader Network_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream Network_out = new DataOutputStream(socket.getOutputStream());
            Scanner keyboard = new Scanner(System.in)
        ) {
            String welcomeMessage = Network_in.readLine();
            System.out.println("Server: " + welcomeMessage);

            while (true) {
                System.out.println("Please guess the number:");
                int guessNum = keyboard.nextInt();

                Network_out.writeInt(guessNum);
                Network_out.flush();

                String result = Network_in.readLine();
                System.out.println("Data from Server: " + result);

                if (result.equals("Congratulations! You predicted correctly.")) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}