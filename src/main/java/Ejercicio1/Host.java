package Ejercicio1;

import java.io.*;
import java.net.*;

/**
 * @author Rafa Narvaiza
 * EJ1. Server has to take the conection from the client, receive a phrase and count the ammount of characters that contains.
 * The response of the server will contain that ammount of characters.
 * If the server receive an asterisk must close the conection.
 */

public class Host {

    public static final int PORT = 5000;
    public static void main(String[] args) {

        ServerSocket server = null;
        Socket sc = null;
        DataInputStream in;
        DataOutputStream out;
        try{
            server = new ServerSocket(PORT);

            while (true){
                sc = server.accept();
                System.out.println("Connection started.");

                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());

                String incommingMessage = in.readUTF();
                System.out.println(incommingMessage);

                String outcomminMessage = "some message to return to the client";
                out.writeUTF(outcomminMessage);

                sc.close();
                System.out.println("Connection finished.");

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
