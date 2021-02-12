package Ejercicio1;

import Sandbox.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Rafa Narvaiza
 * EJ1. Client has to request the server IP:PORT. Once it has been done, client will request a phrase through console and will sent it to the server.
 * The client will order to close the connection when an asterisk will be typed.
 */

public class Client {

    public static void main(String[] args) {

        DataInputStream in;
        DataOutputStream out;

        try{
            Socket sc = new Socket(Constants.IP, Constants.PORT);

            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            String outcommingMessage = "message sent from client";
            out.writeUTF(outcommingMessage);

            String incommingMessage = in.readUTF();

            sc.close();


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
