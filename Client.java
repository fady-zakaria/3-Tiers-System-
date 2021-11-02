package ds_assignment1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            InetAddress ip = InetAddress.getLocalHost();//local host
            Socket server = new Socket(ip, 22000); //It is the server that connect with the client
            //Sender
            DataInputStream ServerReadSource = new DataInputStream(server.getInputStream());
            DataOutputStream ServerWriteSource = new DataOutputStream(server.getOutputStream());

            while (true) {
                Scanner scan = new Scanner(System.in);
                String serverMsgs = ServerReadSource.readUTF();
                System.out.println(serverMsgs);

                String clientMsgs = scan.nextLine();
                if (clientMsgs.equalsIgnoreCase("no")) {
                    ServerWriteSource.writeUTF(clientMsgs);
                    String str = ServerReadSource.readUTF();
                    System.out.println(str);
                    break;
                }
                ServerWriteSource.writeUTF(clientMsgs);
            }
            ServerWriteSource.close();
            ServerReadSource.close();
            server.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
