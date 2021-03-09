package TestUDP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {

        ServerSocket server ;
        Socket sc ;
        DataInputStream in;
        DataOutputStream out;
        final int PORT = 5000;


        try {
            server = new ServerSocket(PORT);
            System.out.println("server start");


            while(true){
                sc = server.accept();

                System.out.println("Cliente conectado");

                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());

                String message = in.readUTF();
                System.out.println(message);
                out.writeUTF("Hola desde el server");

                sc.close();
                System.out.println("Cliente desconectado");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
