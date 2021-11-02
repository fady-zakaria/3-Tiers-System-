package ds_assignment1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Sensor {

    public static void main(String[] args) {
        try {
            ServerSocket sensorServer = new ServerSocket(1234);
            System.out.println("Sensor Running...");

            while (true) {
                Socket serverSocket = sensorServer.accept();
                System.out.println("Server Connected to the Sensor...");

                DataInputStream serverReadSource = new DataInputStream(serverSocket.getInputStream());
                DataOutputStream serverWriteSource = new DataOutputStream(serverSocket.getOutputStream());

                while (true) {
                    String ServerData = serverReadSource.readUTF();
                    System.out.println("New client connected to the server");
                    System.out.println(ServerData);

                    serverWriteSource.writeUTF("Sensor: The best route is to turn left and after 1 km turn right\n");

                    String clientAnswer = serverReadSource.readUTF();
                    if (clientAnswer.equalsIgnoreCase("no")) {
                        System.out.println("Client: He doesn't want another recommendation");
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
