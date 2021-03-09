package test2UDP;

import java.io.IOException;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        final int PUERTO = 5000;
        byte[] buffer;
        InetAddress address;
        try {
            address = InetAddress.getByName("127.0.0.1");
            DatagramSocket socketUDP = new DatagramSocket();
            String message = "Hola desde el client";
            buffer = message.getBytes();
            DatagramPacket envio = new DatagramPacket(buffer, buffer.length, address, PUERTO );
            socketUDP.send(envio);

            DatagramPacket recibo = new DatagramPacket(buffer, buffer.length );
            socketUDP.receive(recibo);

            message = new String(recibo.getData());
            System.out.println(message);

            socketUDP.close();

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
