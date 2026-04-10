package ftpclient.com;
import ftpclient.com.helper;
import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        try{
            Scanner sc = new Scanner(System.in);
            System.out.println("[System]> Before we start, do you want to login as anoymous? (y/n)");
            System.out.print("> ");
            String choice = sc.nextLine();

            Socket socket;
            switch(choice){
                case "y":
                    socket = new Socket("ftp.gnu.org", 21);
                    break;
                case "n":
                    socket = new Socket("ftp.dlptest.com", 21);
                    break;
                default:
                    System.out.println("[System]> Invalid choice. Please enter y or n.");
                    return;
            }

            BufferedWriter Network_out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader Network_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            

            switch(choice){
                case "y": helper.AnonymousLogin(Network_in, Network_out); break;
                case "n": helper.CustomLogin(Network_in, Network_out); break;
            }

            while(true){
                System.out.print("> ");
                String text = sc.nextLine().trim();
                if (text.isEmpty()) {
                    continue;
                }

                String[] inputParts = text.split("\\s+");
                String command = inputParts[0].toLowerCase();

                if(command.equalsIgnoreCase("quit")){
                    helper.sendCommand(Network_out, command.toUpperCase());
                    helper.readReply(Network_in);
                    break;
                }else if(command.equalsIgnoreCase("pwd")){
                    helper.sendCommand(Network_out, command.toUpperCase());
                    helper.readReply(Network_in);
                }else if(command.equalsIgnoreCase("cd")){
                    if(inputParts.length > 1){
                        String folderName = inputParts[1];
                        helper.sendCommand(Network_out, "CWD " + folderName);
                        helper.readReply(Network_in);
                    }else{
                        System.out.println("[System]> Error: Please specify a directory [command: cd <folderName>]");
                    }
                }else if(command.equalsIgnoreCase("ls")){
                    helper.listDirectory(Network_in, Network_out);
                }else{
                    helper.sendCommand(Network_out, text);
                    helper.readReply(Network_in);
                }
            }
            socket.close();
        }catch(Exception e){e.printStackTrace();}
    }
}