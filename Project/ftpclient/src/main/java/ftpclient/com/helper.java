package ftpclient.com;

import java.io.*;
import java.net.*;

/**
 * This is a helper class, I put here and will change later
 */
public class helper {
    /**
     * Helper to send command
     * @param out
     * @param cmd
     * @throws IOException
     */
    static public void sendCommand(BufferedWriter out, String cmd) throws IOException{
        out.write(cmd + "\r\n");
        out.flush();
    }

    /**
     * Helper to read and print out server reply. Run in a loop and only break if server send a ' 'at index 3
     * @param in
     * @throws IOException
     */
    static String readReply(BufferedReader in) throws IOException{
        String serverText = null;
        while(true){
            serverText = in.readLine();
            System.out.println("[Server]> " + serverText);
            if(serverText != null && serverText.length() >= 4 && serverText.charAt(3) == ' '){
                break;
            }
        }
        return serverText;
    }

    /**
     * method to automatic login as Anonymous
     * @param in
     * @param out
     * @throws IOException
     */
    static public void CustomLogin(BufferedReader in, BufferedWriter out) throws IOException{
        helper.readReply(in);
        sendCommand(out, "USER dlpuser");
        readReply(in);
            
        sendCommand(out, "PASS rNrKYTX9g7z3RgJRmxWuGHbeu");
        readReply(in);
    }

    static public void AnonymousLogin(BufferedReader in, BufferedWriter out) throws IOException{
        helper.readReply(in);
        sendCommand(out, "USER anonymous");
        readReply(in);
            
        sendCommand(out, "PASS anonymousGgmail.com");
        readReply(in);
    }

    static public Socket createDataSocket(BufferedReader in, BufferedWriter out) throws IOException{
        sendCommand(out, "PASV");
        String serverRespond = readReply(in);
        int startIndex = serverRespond.indexOf('(') + 1;
        int endIndex = serverRespond.indexOf(')');
        String[] parts = serverRespond.substring(startIndex, endIndex).split(",");

        String ip = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
        
        int FifthNum = Integer.parseInt(parts[4]);
        int SixthNum = Integer.parseInt(parts[5]);
        int port = FifthNum * 256 + SixthNum;
        
        return new Socket(ip, port);
    }

    static public void listDirectory(BufferedReader in, BufferedWriter out) throws IOException {
        Socket dataSocket = createDataSocket(in, out);
        sendCommand(out, "LIST");
        readReply(in);

        System.out.println("================================================================================================");
        try (BufferedReader dataIn = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()))) {
            String directoryName;
            while ((directoryName = dataIn.readLine()) != null) {
                System.out.println(directoryName);
            }
        } finally {
            dataSocket.close();
        }
        System.out.println("================================================================================================");

        readReply(in);
    }

}
