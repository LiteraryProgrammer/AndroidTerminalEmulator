package ver1;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * Created by gpietrus on 01.08.15.
 */
public class Nslookup {

    private DatagramSocket datagramSocket;
    private String address = "62.179.1.62";
    private InetAddress inetAddress;
    private DNSFrame requestDnsFrame;
    private DNSFrame responseDnsFrame;

    public void run() throws IOException {
        DatagramPacket requestDatagramPacket;
        DatagramPacket responseDatagramPacket;
        datagramSocket = new DatagramSocket();
        inetAddress = InetAddress.getByName(address);

        //sending
        requestDnsFrame = new DNSFrame("poczta.onet.pl");
        System.out.println(requestDnsFrame.toString());
        byte[] requestBytes = requestDnsFrame.getBytes();
        requestDatagramPacket = new DatagramPacket(requestBytes,requestBytes.length, inetAddress, 53); //todo: port
        datagramSocket.send(requestDatagramPacket);



        //receiving
        byte[] responseBuf = new byte[64]; //todo: specify max buffer to max udp size?
        responseDatagramPacket = new DatagramPacket(responseBuf,responseBuf.length);
        datagramSocket.receive(responseDatagramPacket);
        int receivedLength = responseDatagramPacket.getLength();
        responseDnsFrame = new DNSFrame(Arrays.copyOfRange(responseBuf,0,receivedLength));
        System.out.println(responseDnsFrame.toString());

        datagramSocket.close(); //todo: move to close datagrma


    }

}
