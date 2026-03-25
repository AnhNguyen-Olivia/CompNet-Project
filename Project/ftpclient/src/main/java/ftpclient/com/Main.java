package ftpclient.com;
import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        try{
            Socket s = new Socket("ftp.dlptest.com", 21);
            Scanner sc = new Scanner(System.in);

            BufferedWriter Network_out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader Network_in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            while(true){
                while(true){
                    String serverText = Network_in.readLine();
                    System.out.println("Server: " + serverText);
                    if(serverText.charAt(3) == ' ') break;
                }

                String text = sc.nextLine();
                Network_out.write(text + "\r\n");
                Network_out.flush();

                if(text.equals("quit")) break;
            }
            s.close();
        }catch(Exception e){e.printStackTrace();}
    }
}