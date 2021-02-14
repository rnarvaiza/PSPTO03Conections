package Ejercicio4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * @author Rafa Narvaiza
 * MasterMind host.
 */
public class Host4 extends Thread{

    private static final String CODE_WRONG = "Wrong code. \n Need help? Here you have: ";
    private static final String exists = "\u2592";
    private static final String notExists ="\u2588";
    private static final String QUESTION = "Can you guess the code?";
    private static final String GAME_RULES = "Hi! This game is about guessing the 4 digit password.\n We will help you with this two keys: If this appears: " + notExists + " the number doesnt appear in the password.\n But if this appears: " + exists + " seems that this number is placed in other position in the password. GL&HF";
    private static final int WHITELIST_PORT = 5050;
    private Socket sc;
    private ServerSocket server;

    private int counter;
    private int attempts;
    private String identifier;
    private String answer;
    private String code;
    private boolean winner = false;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String playerWin;
    private String incommingMessage;


    public Host4(){
    }

    public static void main(String[] args) {
        Host4 h = new Host4();
        h.initConn();
        h.setCode(h.generateCode());
        h.printMessage("Generated code to guess: " + h.getCode());
        h.setCounter(1);
        while(true){
            h.setSc(null);
            try{
                h.setSc(h.getServer().accept());
                h.streams();
                h.setIdentifier(h.getSc() + " New player connected: " + "Player"+ h.getCounter());
                h.printMessage(h.getIdentifier());
                Host4 host4 = new Host4(h.getCode(), h.getSc());
                host4.start();
                h.setCounter(h.getCounter()+1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void run(){
        sendMessage(playerCounter());
        setIncommingMessage(getReceivedMessage());
        printMessage( getIncommingMessage() + " is playing.");
        sendMessage(GAME_RULES);
        initGame();
    }

    public void initGame(){
        setAttempts(1);
        while (true){
            sendMessage(QUESTION);
            setAnswer(getReceivedMessage());
            try {
                checkPlayerAttempt();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            setAttempts(getAttempts()+1);
        }
    }


    public void checkPlayerAttempt() throws IOException {
        String exceededAttempts;
        while(getAttempts()<20){
         if(!checkAnswer()){
             String clue;
             clue = getHelp(getAnswer(),getCode());
             printMessage(CODE_WRONG + clue);
         }
         else{
             setWinner(true);
             setPlayerWin(getIdentifier() + " tooks " + getAttempts() + " to win !");
             printMessage(getPlayerWin());
             sendMessage(getPlayerWin());
             sendMessage(getIdentifier() + " has won, better luck next time.");
             getOut().close();
             getIn().close();
             sc.close();
             System.exit(0);
         }
        }
        exceededAttempts = (getIdentifier() + " attempts exceeded");
        printMessage(exceededAttempts);
        sendMessage(exceededAttempts);
        getOut().close();
        getIn().close();
        sc.close();
        System.exit(0);
    }

    public boolean checkAnswer(){
        boolean answer = false;
        if(getCode().equals(getAnswer())){
            answer=true;
        }
        return answer;
    }

    public String playerCounter(){
        String s;
        s = "Player number: " + getCounter();
        return s;
    }

    public void sendMessage(String s){
        try{
            out.writeUTF(s);
            out.flush();
        }catch (IOException e){
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

    public void initConn(){
        try {
            setServer(new ServerSocket(WHITELIST_PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void streams(){
        try{
            setIn(new DataInputStream(sc.getInputStream()));
            setOut(new DataOutputStream(sc.getOutputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printMessage(String s){
        System.out.println(s);
    }


    public String generateCode(){
        String digits= "1234567890";
        Random random = new Random();
        String s = "";
        for (int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(digits.length());
            digits+=String.valueOf(randomIndex);
            char randomChar = digits.charAt(randomIndex);

            while (s.indexOf(randomChar) != -1) {
                randomIndex =  random.nextInt(digits.length());
                digits+=String.valueOf(randomIndex);
                randomChar = digits.charAt(randomIndex);
            }
            s += randomChar;
        }
        return s;
    }

    public String getHelp(String s, String code) {
        String help = "";
        for (int x = 0; x < s.length(); x++) {
            if (s.charAt(x) == code.charAt(x)) {
                help += notExists +" ";
                continue;
            }
            int indiceCaracter = code.indexOf(s.charAt(x));
            if (indiceCaracter != -1) {
                help += exists+" ";
                continue;
            }
            help += "_";
        }
        return help;
    }


    public Socket getSc() {
        return sc;
    }

    public void setSc(Socket sc) {
        this.sc = sc;
    }


    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Host4(String answer, Socket sc){
        this.answer=answer;
        this.sc=sc;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }


    public String getIncommingMessage() {
        return incommingMessage;
    }

    public void setIncommingMessage(String incommingMessage) {
        this.incommingMessage = incommingMessage;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getPlayerWin() {
        return playerWin;
    }

    public void setPlayerWin(String playerWin) {
        this.playerWin = playerWin;
    }
}
