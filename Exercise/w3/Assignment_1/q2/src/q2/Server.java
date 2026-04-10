package q2;

import java.net.*;
import java.util.*;

public class Server {

    private static final int PORT    = 9999;
    private static final int BUF_LEN = 1024;

    public static void main(String[] args) throws Exception {

        DatagramSocket socket = new DatagramSocket(PORT);
        System.out.println("Server started on port " + PORT);
        System.out.println("Waiting for sensor readings...\n");
        ArrayList<int[]>    seqList  = new ArrayList<>();
        ArrayList<String[]> dataList = new ArrayList<>();

        byte[] buffer = new byte[BUF_LEN];

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String message = new String(packet.getData(), 0, packet.getLength()).trim();
            System.out.println("[RECEIVED] Raw packet: \"" + message + "\"");

            String[] parts = message.split(" ", 3);
            if (parts.length < 3) {
                System.out.println("[WARNING] Malformed packet ignored: " + message);
                continue;
            }

            int    seq   = Integer.parseInt(parts[0].trim());
            String type  = parts[1].trim();
            String value = parts[2].trim();

            if (type.equals("END")) {
                System.out.println("[INFO] END signal received. Processing collected readings...\n");
                break;
            }
            seqList.add(new int[]{seq});
            dataList.add(new String[]{type, value});
        }

        socket.close();
        int n = seqList.size();
        for (int i = 1; i < n; i++) {
            int[]    keySeq  = seqList.get(i);
            String[] keyData = dataList.get(i);
            int j = i - 1;
            while (j >= 0 && seqList.get(j)[0] > keySeq[0]) {
                seqList.set(j + 1, seqList.get(j));
                dataList.set(j + 1, dataList.get(j));
                j--;
            }
            seqList.set(j + 1, keySeq);
            dataList.set(j + 1, keyData);
        }

        System.out.println("Sensor Readings (in original order)    ");
        if (dataList.isEmpty()) {
            System.out.println("  (no readings received)");
        } else {
            for (int i = 0; i < dataList.size(); i++) {
                String type  = dataList.get(i)[0];
                String value = dataList.get(i)[1];

                if (type.equals("temp")) {
                    System.out.printf("  room temperature : %s%n", value);
                } else if (type.equals("hum")) {
                    System.out.printf("  humidity         : %s%n", value);
                } else if (type.equals("door")) {
                    System.out.printf("  door status      : %s%n", value);
                } else {
                    System.out.printf("  unknown (%s)     : %s%n", type, value);
                }
            }
        }
        System.out.println("Server finished.");
    }
}