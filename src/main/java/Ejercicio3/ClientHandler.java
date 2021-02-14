package Ejercicio3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;

/**
 * @author Rafa Narvaiza
 *
 */

public class ClientHandler extends Thread{


    private DataInputStream in ;
    private DataOutputStream out ;
    private Socket client;
    String incommingMessage = "";
    private String message = "";
    private static final String END_CONNECTION = "*";


    /**
     * Constructor to initialize the new thread of ClientHandler with those given parameters.
     * @param in
     * @param out
     * @param client
     */

    public ClientHandler(DataInputStream in, DataOutputStream out, Socket client) {
        this.in = in;
        this.out = out;
        this.setClient(client);
    }

    /**
     * Loop to start the atempt of reading until we get the END_CONNECTION parameter (*).
     * Each time the loop get an incomming message different than * it will be setted on a private variable, then the method will manage the string
     * to convert it on an only upper case string and give it as the answer to the client.
     */

    public void getInputData(){

        try{
            do{
                incommingMessage = in.readUTF();
                setMessage(incommingMessage);
                System.out.println("Message from client: " + incommingMessage);
                sendResponse(getMessage().toUpperCase(Locale.ROOT));
            }while (!incommingMessage.equals(END_CONNECTION));
        } catch (IOException e) {
            System.out.println("Error reading incomming message from client: " + e.getMessage());
            finishConn();
        }
    }

    /**
     * Method that will write on the outgoing stream the managed response.
     * @param response
     */

    public void sendResponse(String response){
        if(!response.isEmpty()
        ){
            try{
                out.writeUTF(response);
                out.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method designed to close streams and sockets.
     */

    public void finishConn(){
        try{
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }finally {
            System.out.println("Connection closed by client.");
            System.exit(0);
        }
    }

    /**
     * Start to try to get the input streams.
     */

    public void loopStart(){
        while (true){
            try {
                getInputData();
            } finally {
                finishConn();
            }
        }
    }


     @Override
    public void run(){
        loopStart();
     }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }
}
