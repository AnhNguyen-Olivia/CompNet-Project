package q1_a;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 1234);

            BufferedReader Network_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream Network_out = new DataOutputStream(s.getOutputStream());

            Scanner keyboard = new Scanner(System.in);

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

            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}