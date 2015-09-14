package ver1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by gpietrus on 25.07.15.
 */

//todo: rozbic na klasy response i request oraz header i payload
public class DNSFrame extends ProtocolFrame {

    //dns frame specification: https://pl.wikipedia.org/wiki/Domain_Name_System#Odpowiedzi_na_zapytania
    //todo: zrobic settery i getery i zmienic na prywatne
    int frameSizeBits = 96; //todo: spr, in bits
    int frameSizeBytes = 12;

    //todo: spakowac to w header
    FrameField id = new FrameField(16);
    FrameField qr = new FrameField(1);
    FrameField opcode = new FrameField(4);
    FrameField aa = new FrameField(1);
    FrameField tc = new FrameField(1);
    FrameField rd = new FrameField(1);
    FrameField ra = new FrameField(1);
    FrameField z = new FrameField(3);
    FrameField rcode = new FrameField(4);
    FrameField qdcount = new FrameField(16);
    FrameField ancount = new FrameField(16);
    FrameField nscount = new FrameField(16);
    FrameField arcount = new FrameField(16);

    //todo: spakowac to w query
    //question section
    byte qnameBytes[]; //declared in constructor
    FrameField qtype = new FrameField(16);
    FrameField qclass = new FrameField(16);


    //todo: i potem jest queries z inna strutkura jeszce


    //response
    FrameField name; //to be initailized
    FrameField type = new FrameField(16);
    FrameField classCode = new FrameField(16);
    FrameField ttl = new FrameField(32);
    FrameField rdlength = new FrameField(16);
    FrameField rdata; //to be inizialized


    public void send(byte[] bytes) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress address = null;

        address = InetAddress.getByName("62.179.1.62");

        DatagramPacket packet = new DatagramPacket(
                bytes, bytes.length, address, 53);

        datagramSocket.send(packet);
        System.out.println("wyslano");
        //todo: nasluchiwanie na powrot
    }

    @Override
    public void compileFrame() {

        //dynamics
        FrameField qname = new FrameField(qnameBytes);


        List<FrameField> frameFieldList = Arrays.asList(id, qr, opcode, aa, tc, rd, ra, z, rcode, qdcount, ancount, nscount, arcount, qname, qtype, qclass);
        //List<ver1.FrameField> frameFieldList = Arrays.asList(id,qr,opcode,aa,tc,rd,ra,z,rcode,qdcount,ancount,nscount,arcount,qname);

        Frame frame = new Frame(frameFieldList);

        //boolean bits[] = frame.getBits();
        //byte bytes[] = frame.getBytes();
        //i to wyslac
        //todo: tylko ze zmienic na inne miejsce

        /*try {
            send(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //todo: wysylanie dziala, porownac czym sie rozni
    }

    public byte[] getBytes() {
        FrameField qname = new FrameField(qnameBytes);
        List<FrameField> frameFieldList = Arrays.asList(id, qr, opcode, aa, tc, rd, ra, z, rcode, qdcount, ancount, nscount, arcount, qname, qtype, qclass);
        Frame frame = new Frame(frameFieldList);
        return frame.getBytes();
    }

    private boolean[] binaryStringToBooleanArray(String binaryString) { //todo: move to another class
        boolean booleanArray[] = new boolean[binaryString.length()];
        for (int i = 0; i < binaryString.length(); i++) {
            booleanArray[i] = binaryString.charAt(i) == '1' ? true : false;
        }
        return booleanArray;
    }

    public DNSFrame(byte[] bytes) { //response frame
        System.out.println("\n\n\nresponse bits");
        String binaryString = toBinary(bytes);
        boolean[] booleanArray = binaryStringToBooleanArray(binaryString);

        //loading data into structure
        int iter = 0;
        //id
        for (int i = 0; i < id.size(); i++) {
            id.setBit(i, booleanArray[iter]);
            iter++;
        }

        //qr
        qr.setBit(0, booleanArray[iter]);
        iter++;

        //opcode
        for (int i = 0; i < opcode.size(); i++) {
            opcode.setBit(i, booleanArray[iter]);
            iter++;
        }

        //aa
        aa.setBit(0, booleanArray[iter]);
        iter++;

        //tc
        tc.setBit(0, booleanArray[iter]);
        iter++;

        //rd
        rd.setBit(0, booleanArray[iter]);
        iter++;

        //ra
        ra.setBit(0, booleanArray[iter]);
        iter++;

        //z
        for (int i = 0; i < z.size(); i++) {
            z.setBit(i, booleanArray[iter]);
            iter++;
        }

        //rcode
        for (int i = 0; i < rcode.size(); i++) {
            rcode.setBit(i, booleanArray[iter]);
            iter++;
        }

        //qdcode
        for (int i = 0; i < 16; i++) {
            qdcount.setBit(i, booleanArray[iter]);
            iter++;

        }

        //ancount
        //all to zero
        for (int i = 0; i < 16; i++) {
            ancount.setBit(i, booleanArray[iter]);
            iter++;
        }

        //ancount
        //all to zero
        for (int i = 0; i < 16; i++) {
            nscount.setBit(i, booleanArray[iter]);
            iter++;
        }


        //ancount
        //all to zero
        for (int i = 0; i < 16; i++) {
            arcount.setBit(i, booleanArray[iter]);
            iter++;
        }


        //qname
        //todo: implement more domains, using numer of queries

        //count number of bytes in qname
        //reading labels length from packet

        //find 0x00 byte which ends labels
        int zeroByte = iter / 8;
        int iterBytes = iter / 8;
        while (bytes[zeroByte] != 0) {
            System.out.println(bytes[zeroByte]);
            zeroByte++;
        }
        zeroByte++;


        int endOfQname = zeroByte;

        qnameBytes = new byte[endOfQname - iterBytes];
        qnameBytes = Arrays.copyOfRange(bytes, iterBytes, endOfQname);

        iterBytes = endOfQname;
        iter = iterBytes * 8;

        //qtype
        for (int i = 0; i < qtype.size(); i++) {
            qtype.setBit(i, booleanArray[iter]);
            iter++;
        }

        //qclass
        for (int i = 0; i < qclass.size(); i++) {
            qclass.setBit(i, booleanArray[iter]);
            iter++;
        }

        //tu startuje response
        //todo WAZNE: The Resource Record Name field is encoded in the same way as the Question Name field unless the name is already present elsewhere in the DNS message, in which case a 2-byte field is used in place of a length-value encoded name and acts as a pointer to the name that is already present.

        //todo: c0 0c sprwadzic 0xc0 = 192 / 8 = 12, 12 bajt od poczatku wskazuje na label
        //todo: sprawdzic czy tak jest zawsze
        //todo: 0x0c = 12 po co to?

        //2 bajty

        //name
        name = new FrameField(16); //2 bajty
        for (int i = 0; i < name.size(); i++) {
            name.setBit(i, booleanArray[iter]);
            iter++;
        }

        //type
        for (int i = 0; i < type.size(); i++) {
            type.setBit(i, booleanArray[iter]);
            iter++;
        }

        //classCode
        for (int i = 0; i < classCode.size(); i++) {
            classCode.setBit(i, booleanArray[iter]);
            iter++;
        }

        //ttl
        for (int i = 0; i < ttl.size(); i++) {
            ttl.setBit(i, booleanArray[iter]);
            iter++;
        }

        //rdlength
        for (int i = 0; i < rdlength.size(); i++) {
            rdlength.setBit(i, booleanArray[iter]);
            iter++;
        }

        //rdata
        rdata = new FrameField(32); //todo: pilne! nie wiem czy 32
        for (int i = 0; i < rdata.size(); i++) {
            rdata.setBit(i, booleanArray[iter]);
            iter++;
        }

        System.out.println(true);
        //http://stackoverflow.com/questions/9865084/dns-response-answer-authoritative-section
    }


    private byte[] fromBinary(String s) //todo: use this function and move to another class
    {
        int sLen = s.length();
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
        char c;
        for (int i = 0; i < sLen; i++)
            if ((c = s.charAt(i)) == '1')
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
            else if (c != '0')
                throw new IllegalArgumentException();
        return toReturn;
    }

    private String toBinary(byte[] bytes) //todo: move to another class
    {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    public DNSFrame(String domain) { //request frame

        //todo: czy musze ustawiac pola ktore nie maja znaczenia?
        //http://www.tcpipguide.com/free/t_DNSMessageHeaderandQuestionSectionFormat.htm

        //id
        //todo: can it be random?


        Random r = new Random();
        //todo: first and 8th bits to be 0 to not end java byte 128 value

        for (int i = 0; i < id.size(); i++) {
            id.setBit(i, r.nextBoolean()); //boolean by byc niezaleznym od architektury
        }
        id.setBit(0, false);
        id.setBit(8, false);

        //qr
        //query, not response
        qr.setBit(0, false);

        //opcode
        //todo: narazie tylko standart query, czy chce inne?
        opcode.setBit(0, 3, false); //standart query

        //aa
        //todo: po co to
        aa.setBit(0, false);

        //tc
        tc.setBit(0, false);

        //rd
        rd.setBit(0, true);

        //ra
        ra.setBit(0, false);

        //z
        z.setBit(0, 2, false);

        //rcode
        rcode.setBit(0, 3, false);


        //qdcode //Specifies the number of questions in the Question section of the message.
        //todo: przeniesc ta funkcje z fora gdzies zeby byla dostepna razem z kowersja na string
        Integer numerOfQuestions = 1;
        String numberOfQuestionsBinaryString = String.format("%16s", Integer.toBinaryString(numerOfQuestions)).replace(' ', '0'); //16 digits
        System.out.println(numberOfQuestionsBinaryString);
        for (int i = 0; i < 16; i++) {
            if (numberOfQuestionsBinaryString.charAt(i) == '1') {
                qdcount.setBit(i, true);
            }
        }

        //ancount
        //all to zero
        for (int i = 0; i < 16; i++) {
            ancount.setBit(i, false);
        }

        //ancount
        //all to zero
        for (int i = 0; i < 16; i++) {
            nscount.setBit(i, false);
        }


        //ancount
        //all to zero
        for (int i = 0; i < 16; i++) {
            arcount.setBit(i, false);
        }

        //todo: impleemtn more domains possible
        String domainParts[] = domain.split("\\."); //split by dot
        qnameBytes = new byte[String.join("", domainParts).length() + domainParts.length + 1]; //length of all domainParts without dots + numer of length bytes + ending \0

        int qnameIterator = 0; //todo: better copying

        //qnameBytes = ver2 byte[domain.length() + domainParts];
        for (String domainPart : domainParts) {
            byte domainPartLength = (byte) domainPart.length();
            //System.out.println(String.format("%16s", Integer.toBinaryString(length)) + "\t" + length + "\t" + sen);
            byte label[] = new byte[domainPartLength + 1]; // label length + length byte
            label[0] = domainPartLength;

            int i = 1;
            for (char c : domainPart.toCharArray()) {
                label[i] = (byte) c;
                i++;
            }

            for (byte one : label) {
                System.out.println(one);
                qnameBytes[qnameIterator] = one;
                qnameIterator++;
            }
            System.out.println("---");


        }

        qnameBytes[qnameIterator] = 0;

        System.out.println("\n\nqnameBytes\n\n");
        for (byte b : qnameBytes) {
            System.out.println(b + "\t" + Integer.toBinaryString(b) + "\t" + Integer.toHexString(b));
        }

        //qtype
        //todo: temporarly only A record
        qtype.setBit(15, true);

        //qclass
        //todo: check po co
        qclass.setBit(15, true);

    }

    public String toString() {
        String header =
                "DNS ver1.Frame" +
                "\nMessage ID\t" + id.toString() +
                "\nQuery - Response\t" + qr.toString() +
                "\nOPCODE\t" + opcode.toString() +
                "\nAuthoritative Answer\t" + aa.toString() +
                "\nTrunCation\t" + tc.toString() +
                "\nRecursion Desired\t" + rd.toString() +
                "\nRecursion Available\t" + ra.toString() +
                "\nRCODE\t" + rcode.toString() +
                "\nQDCOUNT\t" + qdcount.toString() +
                "\nANCOUNT\t" + ancount.toString() +
                "\nNSCOUNT\t" + nscount.toString() +
                "\nARCOUNT\t" + arcount.toString();

        String request =
                "QNAME\t" + toBinary(qnameBytes) +
                "\nQTYPE\t" + qtype.toString() +
                "\nQCLASS\t" + qclass.toString();

        String response; //todo: rozpoznawanie co jest
        return header + "\n" + request;
    }

    public String toTerminalString() {
        return "todo: implement"; //todo: implement
    }


}


//todo: dalsze czesci authority z dns packietu