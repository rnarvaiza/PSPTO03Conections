package Ejercicio1;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * @author Rafa Narvaiza
 * EJ1. Server has to take the conection from the client, receive a phrase and count the ammount of characters that contains.
 * The response of the server will contain that ammount of characters.
 * If the server receive an asterisk must close the conection.
 */

public class Host {

    private static final int PORT = 5000;
    private static final String END_CONNECTION = "*";
    private Socket sc;
    private ServerSocket server;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    Scanner scn = new Scanner(System.in);
    String incommingMessage = "";

    private String message = "";

    public void getConn(int port){
        try{
            server = new ServerSocket(PORT);
            System.out.println("Waiting for incomming peers on port: " + String.valueOf(PORT));
            sc = server.accept();
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }


    public void streams(){
        try{
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
/*
    public void startLoop(){
        while (true){

        }
    }

 */

    public void getInputData(){

        try{
            do{
                incommingMessage = (String) in.readUTF();
                setMessage(incommingMessage);
                System.out.println("Message from client: " + incommingMessage);
                sendResponse(ammountOfCharactersCounted());
            }while (!incommingMessage.equals(END_CONNECTION));
        } catch (IOException e) {
            System.out.println("Error reading incomming message from client: " + e.getMessage());
            finishConn();
        }
    }


    public void sendResponse(int response){
        if(response>0){
            try{
                out.writeUTF(String.valueOf(response));
                out.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

/*
    public void writeResponse(){
        while(true){
            sendResponse(ammountOfCharactersCounted());
        }
    }

 */


    private int ammountOfCharactersCounted() {
        int charactersCounted = 0;
        for(int i = 0; i < getMessage().length(); i++){
            if(getMessage().charAt(i) != ' '){
                charactersCounted ++;
            }
        }
        return charactersCounted;
    }


    public void finishConn(){
        try{
            in.close();
            out.close();
            sc.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }finally {
            System.out.println("Connection closed by client.");
            System.exit(0);
        }
    }


    public void initConn(int port){

            while (true){
                try {
                    getConn(port);
                    streams();
                    getInputData();
                } finally {
                    finishConn();
                }
            }
    }


    public static void main(String[] args) {
        Host h = new Host();
        h.initConn(PORT);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

/*
    public cHost() {

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

 */
}
