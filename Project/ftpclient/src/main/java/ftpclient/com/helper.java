package ftpclient.com;

import java.io.*;
import java.net.*;
import java.util.Optional;

/**
 * Helper class that contains various static methods to assist with FTP operations such as sending commands, 
 * reading server replies, handling file uploads/downloads, and managing progress display.
 * This class is designed to keep the main logic in the Main class clean and organized by abstracting away common tasks 
 * and utilities related to FTP communication and file handling.
 * /ᐠ - ˕ -マ - ᶻ 𝗓 𐰁
 * @author Anh_Nguyen
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
            if(serverText == null) break;

            System.out.println("[Server]> " + serverText);
            
            if(serverText.length() >= 4 && Character.isDigit(serverText.charAt(0)) && serverText.charAt(3) == ' '){
                break;
            }
        }
        return serverText;
    }

    /**
     * method to automatic login as Custom user
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

    /**
     * method to automatic login as Custom userAnonymous
     * @param in
     * @param out
     * @throws IOException
     */
    static public void AnonymousLogin(BufferedReader in, BufferedWriter out) throws IOException{
        helper.readReply(in);
        sendCommand(out, "USER anonymous");
        readReply(in);
            
        sendCommand(out, "PASS guest");
        readReply(in);
    }

    /**
     * Helper to create data socket, send PASV command and parse the respond to get ip and port, then create a new socket for data transfer
     * @param in
     * @param out
     * @return
     * @throws Exception
     */
    static public Socket createDataSocket(BufferedReader in, BufferedWriter out) throws Exception{
        sendCommand(out, "PASV");
        String serverRespond = readReply(in);
        int startIndex = serverRespond.indexOf('(');
        int endIndex = serverRespond.indexOf(')');

        if(startIndex == -1 || endIndex == -1){
            throw new Exception("Server did not return a valid PASV address. The respond was: " + serverRespond);
        }
        
        String[] parts = serverRespond.substring(startIndex + 1, endIndex).split(",");

        String ip = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
        
        int FifthNum = Integer.parseInt(parts[4]);
        int SixthNum = Integer.parseInt(parts[5]);
        int port = FifthNum * 256 + SixthNum;

        System.out.println("[System]> PASV: " + ip + ":" + port);
        return new Socket(ip, port);
    }

    /**
     * Builds a progress bar string based on the given percentage. 
     * The progress bar is 50 characters wide, with '=' representing completed progress and '>' indicating the current position. 
     * If the percentage is 100% or more, the bar will be fully filled with '='.
     * @param percent The percentage of completion.
     * @return The progress bar string.
     */
    private static String buildProgressBar(int percent) {
        int barWidth = 50;
        int filledLength = Math.min(barWidth, Math.max(0, percent * barWidth / 100)); // Ensure filledLength is between 0 and barWidth
        StringBuilder bar = new StringBuilder();

        for(int i = 0; i < barWidth; i++){
            if(i < filledLength) bar.append("=");
            else if(i == filledLength && percent < 100) bar.append(">");
            else bar.append(" ");
        }

        return bar.toString();
    }

    /**
     * Prints the download/upload progress to the console. If the file size is known, it shows a progress bar and percentage. 
     * If the file size is unknown, it simply shows the total bytes downloaded/uploaded.
     * @param totalBytesRead
     * @param fileSize
     * @param lastPercent
     */
    private static void printProgress(long totalBytesRead, long fileSize, int lastPercent) {
        if(fileSize <= 0) {
            System.out.print("\r[System]> Progress: " + totalBytesRead + " bytes downloaded...");
            return;
        }

        int percent = (int) ((totalBytesRead * 100) / fileSize);
        if(percent != lastPercent) {
            System.out.print("\r[System]> Progress: [" + buildProgressBar(percent) + "]" + percent + "% (" + totalBytesRead + "/" + fileSize + " bytes)");
        }
    }

    /**
     * Helper to list files in current directory, it will create a data socket, 
     * send LIST command and print out the respond from data socket until it is closed by server, 
     * then read the final respond from control socket
     * @param in
     * @param out
     * @throws Exception
     */
    static public void listDirectory(BufferedReader in, BufferedWriter out) throws Exception {
        try {
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
        } catch (Exception e) {
            System.out.println("[System]> Could not list files because something went wrong. " + e.getMessage());
        }
        
    }

    /**
     * Helper to download file, it will first check if there is a Download folder and create one if not, 
     * then it will get the file size, create a data socket, 
     * send RETR command and read from data socket and write to local file until server close the connection, 
     * at the same time it will print out the download progress. Finally it will read the final respond from control socket
     * @param in
     * @param out
     * @param fileName
     * @throws IOException
     */
    static public void getFile(BufferedReader in, BufferedWriter out, String fileName) throws IOException{
        File downloadFolder = new File("Download");
        if(!downloadFolder.exists()){
            downloadFolder.mkdir();
        }

        File locaFile = new File(downloadFolder, fileName);
        
        try {
            long fileSize = getSize(in, out, fileName);

            sendCommand(out, "TYPE I");
            readReply(in);

            Socket dataSocket = createDataSocket(in, out);
            sendCommand(out, "RETR " + fileName);
            String serverRespon = readReply(in);
            if(serverRespon.startsWith("1") || serverRespon.startsWith("2")){
                InputStream dataIn = dataSocket.getInputStream();
                FileOutputStream FileOut = new FileOutputStream(locaFile);

                byte[] buffer = new byte[8192];
                int bytesRead;
                long totalBytesRead = 0;
                int lastPercent = -1;
                
                System.out.println("[System]> Downloading " + fileName + " from server...");
                
                /* Print download progress */
                while((bytesRead = dataIn.read(buffer)) != -1){
                    FileOut.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    int percent = fileSize > 0 ? (int) ((totalBytesRead * 100) / fileSize) : -1;
                    if(fileSize <= 0 || percent != lastPercent){
                        printProgress(totalBytesRead, fileSize, lastPercent);
                        lastPercent = percent;
                    }
                }

                if(fileSize > 0){
                    System.out.print("\r[System]> Progress: [" + buildProgressBar(100) + "]100% (" + totalBytesRead + "/" + fileSize + " bytes)\n");
                }else{
                    System.out.println();
                }

                FileOut.close();
                dataSocket.close();
                readReply(in);
                System.out.println("[System]> " + fileName + " should be in " + locaFile.getAbsolutePath());
                
            }else{
                dataSocket.close();
            }
        } catch (Exception e) {
            System.out.println("[System]> Could no  t download file, either because file does not exist or because something went wrong. " + e.getMessage());
        }
    }

    /***
     * Helper to upload file, it will first check if the file exist in current directory or Upload folder,
     * @param in
     * @param out
     * @param fileName
     * @throws IOException
     */
    static public void uploadFile(BufferedReader in, BufferedWriter out, String fileName) throws IOException{
        File localFile = new File(fileName);
        if(!localFile.exists()){
            File uploadFolder = new File("Upload");
            localFile = new File(uploadFolder, fileName);
        }

        if(!localFile.exists()){
            System.out.println("[System]> " + fileName + " not found. Try placing it in the Upload folder or provide a full path.");
        }else{
            try {
                long fileSize = getSize(in, out, fileName);

                String remoteFileName = localFile.getName();
                sendCommand(out, "TYPE I");
                readReply(in);

                Socket dataSocket = createDataSocket(in, out);
                sendCommand(out, "STOR " + remoteFileName);
                String serverRespon = readReply(in);

                if(serverRespon.startsWith("1") || serverRespon.startsWith("2")){
                    try (FileInputStream FileIn = new FileInputStream(localFile)) {
                        OutputStream dataOut = dataSocket.getOutputStream();

                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        long totalBytesRead = 0;
                        int lastPercent = -1;

                        System.out.println("[System]> Uploading " + localFile.getAbsolutePath() + " to server as " + remoteFileName + "...");
                        while((bytesRead = FileIn.read(buffer)) != -1){
                            dataOut.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;
                            int percent = fileSize > 0 ? (int) ((totalBytesRead * 100) / fileSize) : -1;
                            if(fileSize <= 0 || percent != lastPercent){
                                printProgress(totalBytesRead, fileSize, lastPercent);
                                lastPercent = percent;
                            }
                        }

                        if(fileSize > 0){
                            System.out.print("\r[System]> Progress: [" + buildProgressBar(100) + "]100% (" + totalBytesRead + "/" + fileSize + " bytes)\n");
                        }else{
                            System.out.println();
                        }

                        dataOut.close();
                    }
                    dataSocket.close();
                    readReply(in);
                }else{
                dataSocket.close();
                }
            } catch (Exception e) {
                System.out.println("[System]> Could not upload file, because something went wrong. " + e.getMessage());
            }
        }
    }
    /**
     * Helper to get file size, it will send SIZE command and parse the respond to get file size, 
     * if server does not support SIZE command or return invalid respond, it will return -1
     * @param in
     * @param out
     * @param fileName
     * @return file size in bytes, or -1 if file size is unknown
     */
    static public long getSize(BufferedReader in, BufferedWriter out, String fileName){
        long fileSize = -1;
        try {
            sendCommand(out, "SIZE " + fileName);
            String sizeResponse = readReply(in);
            
            if(sizeResponse != null && sizeResponse.startsWith("213 ")){
                try{
                    fileSize = Long.parseLong(sizeResponse.substring(4).trim());
                }catch(NumberFormatException e){}
            }
        } catch (Exception e) {
            System.out.println("[System]> Could not get file size, because something went wrong. " + e.getMessage());
        }
        return fileSize;
    }

    /**
     * Helper to strip matching quotes from a string. 
     * If the string starts and ends with the same type of quote (either single or double), 
     * those quotes will be removed.
     * @param value
     * @return
     */
    public static String stripMatchingQuotes(String value) {
        if (value == null || value.length() < 2) {
            return value;
        }

        char first = value.charAt(0);
        char last = value.charAt(value.length() - 1);
        if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    /**
     * Helper to check if an argument is provided for a command that requires one. 
     * If the argument is empty, it prints an error message with the correct usage and returns an empty Optional. 
     * If the argument is provided, it returns an Optional containing the argument.
     * @param argument
     * @param usage
     * @return
     */
    public static Optional<String> requireArgument(String argument, String usage) {
        if (argument.isEmpty()) {
            System.out.println("[System]> Error: Usage — " + usage);
            return Optional.empty();
        }
        return Optional.of(argument);
    }
    
    /**
     * Helper to send a command and read the response. 
     * It combines sendCommand and readReply into one method for convenience.
     * @param out
     * @param in
     * @param cmd
     */
    public static void sendAndRead(BufferedWriter out, BufferedReader in, String cmd) {
        try {
            helper.sendCommand(out, cmd);
            helper.readReply(in);
        } catch (IOException e) {
            System.err.println("[Error]> " + e.getMessage());
        }
    }

    /**
     * Helper to print out the list of available commands and their usage instructions to the console. 
     * This method is called when the user types "help" to provide guidance on how to use the FTP client.
     */
    public static void printHelp() {
        System.out.println("[System]> Available commands:");
        System.out.println("[System]>   help                 - Show this help message");
        System.out.println("[System]>   pwd                  - Print the current remote directory");
        System.out.println("[System]>   ls                   - List files in the current remote directory");
        System.out.println("[System]>   cd <dirName>         - Change the current remote directory");
        System.out.println("[System]>   get <fileName>       - Download a file from the server");
        System.out.println("[System]>   put <fileName>       - Upload a local file to the server");
        System.out.println("[System]>   delete <fileName>    - Delete a file on the server");
        System.out.println("[System]>   mkdir <dirName>      - Create a directory on the server");
        System.out.println("[System]>   rmdir <dirName>      - Remove a directory on the server");
        System.out.println("[System]>   quit                 - Close the FTP connection and exit");
    }

    /**
     * Safe wrapper for getFile method. 
     * It catches any IOException that may occur during the file download process and prints an error message 
     * instead of throwing the exception.
     * @param in
     * @param out
     * @param file
     */
    public static void getFileSafe(BufferedReader in, BufferedWriter out, String file) {
        try {getFile(in, out, file); }
        catch (IOException e) { System.err.println("[Error]> " + e.getMessage()); }
    }

    /**
     * Safe wrapper for uploadFile method. 
     * It catches any IOException that may occur during the file upload process and prints an error message
     * @param in
     * @param out
     * @param file
     */
    public static void uploadFileSafe(BufferedReader in, BufferedWriter out, String file) {
        try { helper.uploadFile(in, out, file); }
        catch (IOException e) { System.err.println("[Error]> " + e.getMessage()); }
    }
}
