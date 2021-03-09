package test2UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    public static void main(String[] args) {
        final int PUERTO = 5000;
        byte[] buffer = new byte[1024];
        int clientNumber = 1;
        try{
            System.out.println("UDP server start.");
            DatagramSocket socketUDP = new DatagramSocket(PUERTO);

            while (true){

                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                socketUDP.receive(peticion);
                System.out.println("Recibi info cliente"+clientNumber);
                String message = new String(peticion.getData());
                System.out.println(message);

                int ClientPort = peticion.getPort();
                InetAddress address = peticion.getAddress();

                message = "Hola desde el server";
                buffer = message.getBytes();

                DatagramPacket out = new DatagramPacket(buffer, buffer.length, address, ClientPort);
                System.out.println("Envio info al cliente" + clientNumber);
                socketUDP.send(out);

                clientNumber++;

            }

        }catch (SocketException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
