package Ejercicio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

/**
 * @author Rafa Narvaiza
 * EJ2. UDP Ejercicio3.Client3 side implementation. Here we'll work through incoming and outgoing datagrams to manage the differents answers that we'll receive from the server.
 */

public class Client {

    private byte[] sent;
    private byte[] received;
    public DatagramPacket outgoingDP = null;
   // public DatagramPacket dpOutcomming2 = null;
    public DatagramPacket dpIncomming = null;
    public DatagramSocket ds = null;
    private String author ="";
    private String incommingQuote = "";
    public InetAddress ia= null;
    public BufferedReader br = null;
    public InputStreamReader in = null;
    public Scanner sc= null;
    private SocketAddress sockAddr = null;


    /**
     * Initializating streams on isolation mode.
     */

    public void openStreams(){

        try {
            in = new InputStreamReader(System.in);
            br = new BufferedReader(in);
            ds = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
        initConn();
    }

    /**
     * Initialization connection.
     */


    public void initConn(){
        try {
            ia=InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get the input author and set its value.
     * @param stForPrint
     * @return
     */

    public String getStringFromConsole(String stForPrint){
        String s = "";
        sc = new Scanner(System.in);
        System.out.println(stForPrint);
        s=sc.nextLine();
        setAuthor(s);

        return s;
    }

    /**
     * Here we manage the outgoing string through the datagramPacket. It's necessary to transform the string on an byte's array.
     */

    public void outgoingChainToServer(){
        try {
            setSent(getStringFromConsole(Constants.AUTHOR_QUESTION).getBytes());
            outgoingDP = new DatagramPacket(getSent(), getSent().length, ia, Integer.parseInt(Constants.WHITELIST_PORT));
            ds.send(outgoingDP);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Managing the incoming datagram and transforming on a String to get shown.
     */

    public void incomminChainFromServer(){
        setReceived(new byte[1024]);
        try{
            dpIncomming = new DatagramPacket(getReceived(), getReceived().length);
            ds.receive(dpIncomming);
            setIncommingQuote(new String(dpIncomming.getData(), dpIncomming.getOffset(), dpIncomming.getLength()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Method to check if author exist in the server collection, otherwise we manage to put the new quotation for this new author.
     */

    public void checkFromHost(){
        if(!authorNotFound()){
            System.out.println("The phrase: " + getIncommingQuote() + " has been written by: " + getAuthor());
        }else{
            setSent(getStringFromConsole(Constants.INPUT_AUTHOR_PHRASE).getBytes());
            try {
                dpIncomming = new DatagramPacket(getReceived(), getReceived().length);
                ds.receive(dpIncomming);
                ds.send(new DatagramPacket(getSent(), getSent().length, ia, Integer.parseInt(Constants.WHITELIST_PORT)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Quotes has been added.");
        }
        closeSocket();

    }

    public boolean authorNotFound(){
        boolean error = false;
        if(getIncommingQuote().equals(Constants.ERROR_MSG)){
            error = true;
        }
        return error;
    }

    public void closeSocket(){
        ds.close();
    }

    public byte[] getSent() {
        return sent;
    }

    public void setSent(byte[] sent) {
        this.sent = sent;
    }

    public byte[] getReceived() {
        return received;
    }

    public void setReceived(byte[] received) {
        this.received = received;
    }

    public String getIncommingQuote() {
        return incommingQuote;
    }

    public void setIncommingQuote(String incommingQuote) {
        this.incommingQuote = incommingQuote;
    }

    public static void main(String[] args) {

        Client client = new Client();
        client.openStreams();
        client.outgoingChainToServer();
        client.incomminChainFromServer();
        client.checkFromHost();


    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
