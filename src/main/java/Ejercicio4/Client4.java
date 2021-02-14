package Ejercicio4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author Rafa Narvaiza
 * MasterMind client.
 */
public class Client4 {

    private static final int WHITELIST_PORT = 5050;
    private static final String WHITELIST_IP = "127.0.0.1";
    private Socket sc;
    private static String ip;
    private static int port;
    public DataInputStream in;
    public DataOutputStream out;
    private String gameIntro;
    private String answer;
    private String question;
    private String solution;
    private String outcommingMessage;
    private String incommingMessage;
    private String ID;
    private String gameRules;
    private int lives;



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
        if(port!=(WHITELIST_PORT)){
            setPort(Integer.valueOf(WHITELIST_PORT));
            System.out.println("Input PORT is not on whitelist. We suggest to add new PORT's to whitelist. Default whitelist PORT is setted: " + WHITELIST_PORT);
        }else{
            setPort(port);
        }
    }

    public void initConn(){
        inputServerInfo();
        try{
            setSc(new Socket(getWhitelistIp(), getWhitelistPort()));

        } catch (UnknownHostException ue) {
            System.out.println(ue.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void openStreams(){
        try{
            in = new DataInputStream(getSc().getInputStream());
            out = new DataOutputStream(getSc().getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getReceivedMessage(){
        String s = "";
        try{
            s = in.readUTF();
            setIncommingMessage(s);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return s;
    }

    public void sendID(){

        try {
            sendMessage(getID() + (InetAddress.getLocalHost().getHostAddress()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String s){
        try{
            out.writeUTF(s);
            out.flush();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void printMessages(String s){
        System.out.println(s);
    }

    public String getMessagesFromConsole(){
        String s;
        Scanner scn= new Scanner(System.in);
        s=scn.nextLine();
        return s;
    }

    public void play(){
        setLives(1);
        while(true){
            setQuestion(getIncommingMessage());
            printMessages(getQuestion());
            checkMatch(getQuestion());

            setAnswer(getMessagesFromConsole());
            sendMessage(getAnswer());

            setSolution(getIncommingMessage());
            printMessages(getSolution());
            checkMatch(getSolution());
            setLives(getLives()+1);
            checkLives();

        }
    }

    public void checkLives(){
        while(getLives()<20){
            printMessages("Still alive," + (getLives()-20) + " lives rest.");
        }
        try {
            printMessages("Game over.");
            getSc().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkMatch(String s){
        printMessages(s);
        if(s.contains("win")){
            printMessages("You win!");
            try {
                getSc().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        Client4 client4 = new Client4();
        client4.initConn();
        client4.openStreams();
        client4.setIncommingMessage(client4.getReceivedMessage());
        client4.setID(client4.getIncommingMessage());
        client4.sendID();
        client4.setGameRules(client4.getReceivedMessage());
        client4.printMessages(client4.getGameRules());
        client4.printMessages("Lets play.");
        client4.play();

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

    public String getOutcommingMessage() {
        return outcommingMessage;
    }

    public void setOutcommingMessage(String outcommingMessage) {
        this.outcommingMessage = outcommingMessage;
    }

    public String getIncommingMessage() {
        return incommingMessage;
    }

    public void setIncommingMessage(String incommingMessage) {
        this.incommingMessage = incommingMessage;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getGameRules() {
        return gameRules;
    }

    public void setGameRules(String gameRules) {
        this.gameRules = gameRules;
    }

    public String getGameIntro() {
        return gameIntro;
    }

    public void setGameIntro(String gameIntro) {
        this.gameIntro = gameIntro;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Socket getSc() {
        return sc;
    }

    public void setSc(Socket sc) {
        this.sc = sc;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
