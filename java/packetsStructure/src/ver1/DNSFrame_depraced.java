package ver1;

import java.util.Random;

/**
 * Created by gpietrus on 25.07.15.
 */


public class DNSFrame_depraced extends ProtocolFrame {

    //dns frame specification: https://pl.wikipedia.org/wiki/Domain_Name_System#Odpowiedzi_na_zapytania
    //todo: zrobic settery i getery i zmienic na prywatne
    int frameSizeBits = 96; //todo: spr, in bits
    int frameSizeBytes = 12;

    //todo: spakowac to w header
    BitField_depracated id = new BitField_depracated(16);
    BitField_depracated qr = new BitField_depracated(1);
    BitField_depracated qpcode = new BitField_depracated(4);
    BitField_depracated aa = new BitField_depracated(1);
    BitField_depracated tc = new BitField_depracated(1);
    BitField_depracated rd = new BitField_depracated(1);
    BitField_depracated ra = new BitField_depracated(1);
    BitField_depracated z = new BitField_depracated(3);
    BitField_depracated rcode = new BitField_depracated(4);
    BitField_depracated qdcount = new BitField_depracated(16);
    BitField_depracated ancount = new BitField_depracated(16);
    BitField_depracated nscount = new BitField_depracated(16);
    BitField_depracated arcount = new BitField_depracated(16);

    //todo: spakowac to w query
    //question section
    byte qname[]; //declared in constructor
    BitField_depracated qtype = new BitField_depracated(16);
    BitField_depracated qclass = new BitField_depracated(16);


    //i potem jest queries z inna strutkura jeszce


    public void send() {

    }

    @Override
    public void compileFrame() {
        //todo: do zrobienia najszynciej, kompilacja calej ramki i sprawdzic z wiresharkiem czy wyszla taka sama?
        //ver1.BitField_depracated frameBits = ver2 ver1.BitField_depracated(frameSizeBits);
        BitField_depracated frameBits = new BitField_depracated(96);

        //todo: better way to rewrite
/*
        int frameBitsIterator = 0;

        for (int i = 0; i < 16; i++) {
            frameBits.set(frameBitsIterator, id.get(i));
            frameBitsIterator++;
        }

        frameBits.set(frameBitsIterator, qr.get(0));
        frameBitsIterator++;

        for (int i = 0; i < 4; i++) {
            frameBits.set(frameBitsIterator, opcode.get(i));
            frameBitsIterator++;
        }

        frameBits.set(frameBitsIterator, aa.get(0));
        frameBitsIterator++;

        frameBits.set(frameBitsIterator, tc.get(0));
        frameBitsIterator++;

        frameBits.set(frameBitsIterator, rd.get(0));
        frameBitsIterator++;

        frameBits.set(frameBitsIterator, aa.get(0));
        frameBitsIterator++;

        for (int i = 0; i < 3; i++) {
            frameBits.set(frameBitsIterator, z.get(i));
            frameBitsIterator++;
        }

        for (int i = 0; i < 4; i++) {
            frameBits.set(frameBitsIterator, rcode.get(i));
            frameBitsIterator++;
        }

        for (int i = 0; i < 16; i++) {
            frameBits.set(frameBitsIterator, qdcount.get(i));
            frameBitsIterator++;
        }

        for (int i = 0; i < 16; i++) {
            frameBits.set(frameBitsIterator, ancount.get(i));
            frameBitsIterator++;
        }

        for (int i = 0; i < 16; i++) {
            frameBits.set(frameBitsIterator, nscount.get(i));
            frameBitsIterator++;
        }

        for (int i = 0; i < 16; i++) {
            frameBits.set(frameBitsIterator, arcount.get(i));
            frameBitsIterator++;
        }

        System.out.println("po kompilacji\t" + frameBitsIterator);
*/
        System.out.println("size"  + frameBits.size());

        for (int i = 0; i < 96; i++) {
            if (i % 8 == 0) System.out.print("\t");
            //System.out.print(frameBits.get(i) ? 1 : 0);
            System.out.print("bit" + i + "\t" + (frameBits.get(i) ? 1 : 0) + "\n");
        }

        System.out.println("\n\n\n");

        //to bytes
        /*byte frameBytes[] = frameBits.toByteArray(); //todo: to zle bo obcina od zer
        System.out.println(frameBytes.length);
        System.out.println(frameBytes.toString());
*/
        //todo: zrezygnowac z bitset i zrobic wlasna klase operujaca na booleanach?

        //ramke skonstruowac na bitach a potem przekonrertowac na bite array
    }


    public DNSFrame_depraced(String domain) {

        //todo: czy musze ustawiac pola ktore nie maja znaczenia?
        //http://www.tcpipguide.com/free/t_DNSMessageHeaderandQuestionSectionFormat.htm

        //id
        //todo: can it be random?


        Random r = new Random();
        //todo: first bit to be 0 to not end java byte 128 value
        id.set(0,false);
        for (int i = 1; i < id.size(); i++) {
            id.set(i, r.nextBoolean()); //boolean by byc niezaleznym od architektury
        }

        //qr
        //query, not response
        qr.set(0, false);

        //opcode
        //todo: narazie tylko standart query, czy chce inne?
        qpcode.set(0, 3, false); //standart query

        //aa
        //todo: po co to
        aa.set(0, false);

        //tc
        tc.set(0, false);

        //rd
        rd.set(0, true);

        //ra
        ra.set(0, false);

        //z
        z.set(0, 2, false);

        //rcode
        rcode.set(0, 3, false);


        //qdcode //Specifies the number of questions in the Question section of the message.
        //todo: przeniesc ta funkcje z fora gdzies zeby byla dostepna razem z kowersja na string
        Integer numerOfQuestions = 1;
        String numberOfQuestionsBinaryString = String.format("%16s", Integer.toBinaryString(numerOfQuestions)).replace(' ', '0'); //16 digits
        System.out.println(numberOfQuestionsBinaryString);
        for (int i = 0; i < 16; i++) {
            if (numberOfQuestionsBinaryString.charAt(i) == '1') {
                qdcount.set(i,true);
            }
        }

        //ancount
        //all to zero
        for (int i = 0; i < 16; i++) {
            ancount.set(i, false);
        }

        //ancount
        //all to zero
        for (int i = 0; i < 16; i++) {
            nscount.set(i, false);
        }


        //ancount
        //all to zero
        for (int i = 0; i < 16; i++) {
            arcount.set(i, false);
        }

        //todo: impleemtn more domains possible
        String domainParts[] = domain.split("\\."); //split by dot
        qname = new byte[String.join("", domainParts).length() + domainParts.length + 1]; //length of all domainParts without dots + numer of length bytes + ending \0

        int qnameIterator = 0; //todo: better copying

        //qnameBytes = ver2 byte[domain.length() + domainParts];
        for (String domainPart : domainParts) {
            byte domainPartLength = (byte) domainPart.length();
            //System.out.println(String.format("%16s", Integer.toBinaryString(length)) + "\t" + length + "\t" + label);
            byte label[] = new byte[domainPartLength + 1]; // label length + length byte
            label[0] = domainPartLength;

            int i = 1;
            for (char c : domainPart.toCharArray()) {
                label[i] = (byte) c;
                i++;
            }

            for (byte one : label) {
                System.out.println(one);
                qname[qnameIterator] = one;
                qnameIterator++;
            }
            System.out.println("---");


        }

        qname[qnameIterator] = 0;

        System.out.println("\n\nqnameBytes\n\n");
        for (byte b : qname) {
            System.out.println(b + "\t" + Integer.toBinaryString(b) + "\t" + Integer.toHexString(b));
        }

        //qtype
        //todo: temporarly only A record
        qtype.set(3, true);

        //qclass
        //todo: check po co
        qclass.set(3, true);

    }


}
