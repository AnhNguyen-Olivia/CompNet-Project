package q1_a;

import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(1234);
            Socket con = ss.accept();

            DataInputStream in = new DataInputStream(con.getInputStream());
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));

            int RandomNumber = NumberGenerator();

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

            con.close();
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static int NumberGenerator() {
        int R = (int) (Math.random() * 100);
        return R;
    }
}