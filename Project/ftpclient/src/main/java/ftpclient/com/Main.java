package ftpclient.com;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 * Main class that serves as the entry point for the FTP client application. 
 * It handles user input for login type (anonymous or custom), establishes a connection to the FTP server, 
 * and processes user commands in a loop until the user decides to quit. 
 *  ദ്ദി ≽^⎚˕⎚^≼ .ᐟ
 * @author Anh_Nguyen
 */
public class Main {
    /**
     * Helper method to determine the login type (anonymous or custom) based on user input.
     * ฅ^•ﻌ•^ฅ
     * @param scanner
     * @return
     */
    private static boolean loginType(Scanner scanner){
        while(true){
            System.out.println("[System]> Login as anonymous? [y/n]");
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "y": return true;
                case "n": return false;
                default: System.out.println("[System]> Invalid choice. Please enter y or n.");
                    break;
            }
        }
    }
    /**
     * Main command loop that continuously prompts the user for commands until they choose to quit.
     * @param scanner
     * @param in
     * @param out
     * @throws Exception
     */
    private static void runCommand(Scanner scanner, BufferedReader in, BufferedWriter out) throws Exception{
        while(true){
            System.out.print("> ");
            String line = scanner.nextLine().trim();

            if(line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2); // Split into command and argument (if any)
            String command = parts[0].toLowerCase();
            String argument = parts.length > 1 ? helper.stripMatchingQuotes(parts[1].trim()) : "";

            if(!handleCommand(command, argument, in, out)) break;
        }
    }
    /***
     * Handles the execution of a given command with its argument.
     * @param command
     * @param argument
     * @param in
     * @param out
     * @return
     * @throws Exception
     */
    private static boolean handleCommand(String command, String argument, BufferedReader in, BufferedWriter out) throws Exception{
        switch (command) {
            case "quit":
                helper.sendCommand(out, "QUIT");
                helper.readReply(in);
                return false;
            
            case "pwd":
                helper.sendCommand(out, "PWD");
                helper.readReply(in);
                break;

            case "cd":
                helper.requireArgument(argument, "cd <directory>").ifPresent(dir -> helper.sendAndRead(out, in, "CWD " + dir));
                break;

            case "ls":
                helper.listDirectory(in, out);
                break;

            case "get":
                helper.requireArgument(argument, "get <fileName>").ifPresent(file -> helper.getFileSafe(in, out, file));
                break;

            case "put":
                helper.requireArgument(argument, "put <fileName>").ifPresent(file -> helper.uploadFileSafe(in, out, file));
                break;

            case "delete":
                helper.requireArgument(argument, "delete <directory>").ifPresent(file -> helper.sendAndRead(out, in, "DELE " + file));
                break;

            case "mkdir":
                helper.requireArgument(argument, "mkdir <dirName>").ifPresent(dir -> helper.sendAndRead(out, in, "MKD " + dir));
                break;

            case "rmdir":
                helper.requireArgument(argument, "rmdir <dirName>").ifPresent(dir -> helper.sendAndRead(out, in, "RMD " + dir));
                break;

            case "help":
                helper.printHelp();
                break;

            default:
                helper.sendCommand(out, command.toUpperCase() + (argument.isEmpty() ? "" : " " + argument ));
                helper.readReply(in);
                break;
        }
        return true;
    }
    /**
     * Main method that initializes the FTP client application. 
     * It prompts the user for login type, establishes a connection to the FTP server,
     * @param args
     */
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)) {
            boolean anonymous = loginType(scanner);
            String host = anonymous ? Constants.ANONYMOUS_HOST : Constants.CUSTOM_HOST;
            
            try(Socket socket = new Socket(host, Constants.FTP_PORT)) {
                BufferedWriter Network_out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader Network_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                if(anonymous){
                    helper.AnonymousLogin(Network_in, Network_out);
                }else{
                    helper.CustomLogin(Network_in, Network_out);
                }
                
                runCommand(scanner, Network_in, Network_out);
            } catch (Exception e) {
                System.err.println("[Error]> " + e.getMessage());
                e.printStackTrace();
            }

        }catch(Exception e){e.printStackTrace();}
    }
}