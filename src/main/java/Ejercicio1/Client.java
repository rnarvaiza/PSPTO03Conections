package Ejercicio1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author Rafa Narvaiza
 * EJ1. Ejercicio1.Client has to request the server IP:PORT. Once it has been done, client will request a phrase through console and will sent it to the server.
 * The client will order to close the connection when an asterisk will be typed.
 */

public class Client {

    private static final String WHITELIST_IP = "127.0.0.1";
    private static final String WHITELIST_PORT = "5000";
    private static final String CONNECTION_CLOSING_MSG = "Shutting down connection.";
    private static final String STRING_FOR_SERVER = "Please type the phrase for the server:";
    private static final String END_CONNECTION = "*";
    private Socket sc;
    public DataInputStream in;
    public DataOutputStream out;
    Scanner scString = new Scanner(System.in);
    private String outcommingMessage;
    private String incommingMessage;
    private static String ip;
    private static int port;


    public String getIncommingMessage() {
        return incommingMessage;
    }

    public void setIncommingMessage(String incommingMessage) {
        this.incommingMessage = incommingMessage;
    }

    public String getOutcommingMessage() {
        return outcommingMessage;
    }

    public void setOutcommingMessage(String outcommingMessage) {
        this.outcommingMessage = outcommingMessage;
    }


    public static String getWhitelistIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public static int getWhitelistPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Initialization socket on isolation mode.
     */


    public void initConn(){
        inputServerInfo();
            try{
                sc = new Socket(getWhitelistIp(), getWhitelistPort());
            } catch (UnknownHostException ue) {
                System.out.println(ue.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

    }

    /**
     * Initializating streams on isolation mode.
     */


    public void openStreams(){
        try{
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method with an infinite loop designed to get and isolate the input string. Once we get it isolated, we proceed to send throw the implemented methow below.
     * After that, we keep listening to get our comeback message.
     * If an * is typed on, connection will be closed.
     */

    public void startLoop(){
        String s;
        while(true){
            do{
                System.out.println(STRING_FOR_SERVER);
                s= scString.nextLine();
            }while(s.isEmpty());
            setOutcommingMessage(s);
            sendMessage(getOutcommingMessage());
            if(getOutcommingMessage().equals(END_CONNECTION)){
                System.out.println(CONNECTION_CLOSING_MSG);
                closeConn();
            }
            getReceivedMessage();
            System.out.println("The phrase: " + getOutcommingMessage() + " has " + getIncommingMessage() + " characters.");

        }
    }

    /**
     * On this method we'll be listening on an instance of DataInputStream to get the message from the host.
     */

    public void getReceivedMessage(){
        String s = "";
        try{
            s = in.readUTF();
            setIncommingMessage(s);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method to send through DataOutputStream a choosen string.
     * @param s
     */


    public void sendMessage(String s){
        try{
            out.writeUTF(s);
            out.flush();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method for closing streams and sockets.
     */


    public void closeConn(){
        try {
            in.close();
            out.close();
            sc.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.exit(0);
        }
    }



    public void executeConn(){
            try{
                initConn();
                openStreams();
                startLoop();
            }finally {
                closeConn();
            }
    }

    /**
     * Method designed for gathering connection info.
     */

    public void inputServerInfo(){
        String ip;
        int port;
        Scanner scMain = new Scanner(System.in);
        Scanner scMainForInt = new Scanner(System.in);

        //To avoid misunderstanding and connection timeouts or pointing to nowhere, whe restricted the odds to a ip&port whitelist. As we are just
        //testing, its saved on an String variable. But we can implement an array of Strings which contain the whitelisted IP's and check before connect.

        System.out.println("Please input IP from server: ");
        ip = (scMain.nextLine());
        if(!ip.equalsIgnoreCase(WHITELIST_IP)){
            setIp(WHITELIST_IP);
            System.out.println("Input IP is not on whitelist. We suggest to add new IP's to whitelist. Default whitelist IP is setted: " + WHITELIST_IP);
        }else{
            setIp(ip);
        }


        System.out.println("Please input PORT from server: ");
        port = (Integer.valueOf(scMainForInt.nextLine()));
        if(!String.valueOf(port).equalsIgnoreCase(WHITELIST_PORT)){
            setPort(Integer.valueOf(WHITELIST_PORT));
            System.out.println("Input PORT is not on whitelist. We suggest to add new PORT's to whitelist. Default whitelist PORT is setted: " + WHITELIST_PORT);
        }else{
            setPort(port);
        }


    }


    public static void main(String[] args) {
        Client client = new Client();
        client.executeConn();
    }

}
