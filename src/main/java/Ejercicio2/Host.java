package Ejercicio2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafa Narvaiza
 * EJ2. Implementations of host side UDP app. Here we initialize a collection of 5 quote class, which must contain the quotation and his author.
 * Server will receibe and author from the client, check if author is inside the collection and then return the quotation. If the collection doesn't contains the
 * incomming author, it would be necessary to add the new author and ask the client for his quotation.
 * For this porpouse, Answer class is designed with a bolean which will be true if the quotation is already on the collection or false if it isn't. It also has an String parameter,
 * which will contain the quotation.
 */

public class Host {

    private byte[] sent;
    private byte[] received = new byte[1024];
    private String quote;
    private String author;
    private List<Quotes> quotes;
    private static final String WHITELIST_PORT = "1501";
    Answer answer = new Answer();
    DatagramPacket outcomingDP;
    private InetAddress origin;
    private int portOfIncommingDP;
    private DatagramPacket incomingDP;
    DatagramSocket ds;


    public void fillTheArray(){
        quotes = new ArrayList<>();
        quotes.add (new Quotes("Start writing, no matter what. The water does not flow until the faucet is turned on.","Louis L’Amour"));
        quotes.add (new Quotes("Get it down. Take chances. It may be bad, but it's the only way you can do anything really good.","William Faulkner"));
        quotes.add (new Quotes("You can’t wait for inspiration. You have to go after it with a club","Jack London"));
        quotes.add (new Quotes("If you want to be a writer, you must do two things above all others: read a lot and write a lot.","Stephen King"));
        quotes.add (new Quotes("Half my life is an act of revision","John Irving"));

    }

    public void initialize(){
        try {
            ds = new DatagramSocket(Integer.valueOf(Constants.WHITELIST_PORT));
            System.out.println("Waiting for incomming peers...");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void hostIterator(){
        while(true){
            incommingChainFromClient();
            saveIncommingAddress();
            findAuthor();
            manageAnswer();
        }
    }

    public void incommingChainFromClient(){
        setIncomingDP(new DatagramPacket(getReceived(), getReceived().length));
        try {
            ds.receive(getIncomingDP());
            setAuthor(new String(getIncomingDP().getData(), getIncomingDP().getOffset(), getIncomingDP().getLength()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Author: " + getAuthor() + " has been required.");

    }

    public void saveIncommingAddress(){
        setOrigin(getIncomingDP().getAddress());
        setPortOfIncommingDP(getIncomingDP().getPort());
    }

    public void findAuthor(){
        boolean found = false;
        try{
            for(Quotes q: quotes){
                if(getAuthor().contains(q.getAuthor())){
                    answer.setExist(true);
                    answer.setText(q.getText());
                    found=true;
                }
            }
        }catch (ArrayIndexOutOfBoundsException e){
            e.getMessage();
        }
        if(!found){
            System.out.println("Nothing found.");
            answer.setExist(false);
            answer.setText(Constants.ERROR_MSG);
        }
    }

    public void outcommingChainToClient(){
        try {
            setSent(answer.getText().getBytes());
            outcomingDP = new DatagramPacket(getSent(), getSent().length,getOrigin(),getPortOfIncommingDP());
            ds.send(outcomingDP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Answer: " + answer.getText() + " sent.");
    }

    public void manageAnswer(){
        outcommingChainToClient();
        if(!answer.exist()){
            try {
                setIncomingDP(new DatagramPacket(getReceived(), getReceived().length));
                ds.receive(getIncomingDP());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            setQuote(new String(getIncomingDP().getData(), getIncomingDP().getOffset(), getIncomingDP().getOffset()));
            quotes.add(new Quotes(getQuote(),getAuthor()));
        }
    }


    public void closeConn(){
        ds.close();
        System.out.println("Connection closed");
        System.exit(0);
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

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public DatagramPacket getIncomingDP() {
        return incomingDP;
    }

    public void setIncomingDP(DatagramPacket incomingDP) {
        this.incomingDP = incomingDP;
    }

    public InetAddress getOrigin() {
        return origin;
    }

    public void setOrigin(InetAddress origin) {
        this.origin = origin;
    }

    public int getPortOfIncommingDP() {
        return portOfIncommingDP;
    }

    public void setPortOfIncommingDP(int portOfIncommingDP) {
        this.portOfIncommingDP = portOfIncommingDP;
    }

    public static void main(String[] args) {
        Host h = new Host();
        try{
            h.fillTheArray();
            h.initialize();
            h.hostIterator();
        }finally {
            //h.closeConn();
        }

    }
}
