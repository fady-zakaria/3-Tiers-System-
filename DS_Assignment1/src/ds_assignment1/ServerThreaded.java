package ds_assignment1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class ClientHandler extends Thread {

    Socket clientSocket;
    DataInputStream sensorReadSource;
    DataOutputStream sensorWriteSource;
    
    public ClientHandler(Socket clientSocket, DataInputStream sensorReadSource, DataOutputStream sensorWriteSource) {
        this.clientSocket = clientSocket;
        this.sensorReadSource = sensorReadSource;
        this.sensorWriteSource = sensorWriteSource;
    }

    @Override
    public void run() {
        try {
            //Act as Server for client
            DataInputStream clientReadSource = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream clientWriteSource = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                //Server talks client 
                clientWriteSource.writeUTF("[Server]:Hi! Welcome to our System\n"
                        + "If you want any recommendations about your traffic, Please Enter your Current Location");

                String currentLocation = clientReadSource.readUTF();
                System.out.println("Client: Current Loction ==> " + currentLocation);

                clientWriteSource.writeUTF("Server: Please Enter your Destination that you want to go ");
                clientWriteSource.flush();

                String finalDestination = clientReadSource.readUTF();
                System.out.println("Client: Wanted Destination ==> " + finalDestination);

                //Sensor part
                String str = "Server: The current Location is " + currentLocation + " & The final distination is " + finalDestination;
                sensorWriteSource.writeUTF(str);
                sensorWriteSource.flush();
                String sensorResult = sensorReadSource.readUTF();

                clientWriteSource.writeUTF(sensorResult + "Server: Do you want another recommendation? [Yes/No]");
                clientWriteSource.flush();

                String client_Choice = clientReadSource.readUTF();
                sensorWriteSource.writeUTF(client_Choice);
                sensorWriteSource.flush();
                System.out.println("Client: " + client_Choice);

                if (client_Choice.equalsIgnoreCase("no")) {
                    clientWriteSource.writeUTF("Server: Thanks for using our System");
                    break;
                }
            }
            clientWriteSource.close();
            clientReadSource.close();
            clientSocket.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}

public class ServerThreaded{
     public static void main(String[] args){
         try {
            //To connect to the client as a server 
            ServerSocket server = new ServerSocket(22000);
            System.out.println("Server Running...");

            //Act as client for sensor
            InetAddress ip = InetAddress.getLocalHost();
            
            //Server act as client to the sensor(server)
            Socket sensorClient = new Socket(ip, 1234);
            DataInputStream sensorReadSource = new DataInputStream(sensorClient.getInputStream());
            DataOutputStream sensorWriteSource = new DataOutputStream(sensorClient.getOutputStream());

            while (true) {
                //Act as server
                Socket clientSocket = server.accept(); //return socket
                System.out.println("New Client Accepted...");

                ClientHandler thr = new ClientHandler(clientSocket, sensorReadSource, sensorWriteSource);
                thr.start();

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
     }
}
