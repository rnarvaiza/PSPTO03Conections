package Ejercicio3;

import java.io.*;
import java.net.*;

/**
 * @author Rafa Narvaiza
 * Ejercicio3.Host3 will be executing repeatedly, each time a client attempts to connect, host will instantiate a new thread to attend the client request.
 */

public class Host3 {

    private static final int PORT = 5000;
    private DataInputStream in = null;
    private  DataOutputStream out = null;


    public static void main(String[] args) throws IOException {
        DataInputStream in;
        DataOutputStream out;
        ServerSocket server = new ServerSocket(PORT);;


        /**
         * Inside the next loop, we initialize an empty socket. Then, each atempt of conection will try to instantiate a new thread of the clientHandler givin through
         * parameters the socket and the streams that will need.
         */

        while(true){
            Socket sc = null;
            try{
                System.out.println("Waiting for incomming peers on port: " + String.valueOf(PORT));
                sc=server.accept();
                in = new DataInputStream(sc.getInputStream());
                out=new DataOutputStream(sc.getOutputStream());
                System.out.println("New peer connected: " + sc);
                Thread t = new ClientHandler(in,out,sc);
                t.start();
            }catch (IOException e){
                System.out.println(e.getMessage());
                sc.close();
            }

        }
    }

}
