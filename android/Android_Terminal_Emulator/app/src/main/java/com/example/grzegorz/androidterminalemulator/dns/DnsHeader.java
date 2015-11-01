package com.example.grzegorz.androidterminalemulator.dns;

import java.util.ListIterator;
import java.util.Random;

/**
 * Created by gpietrus on 01.08.15.
 */
public class DnsHeader extends FrameHeader {

    public enum QR {
        query, response
    }

    private BitField id = new BitField(16);
    private BitField qr = new BitField(1);
    private BitField opcode = new BitField(4);
    private BitField aa = new BitField(1);
    private BitField tc = new BitField(1);
    private BitField rd = new BitField(1);
    private BitField ra = new BitField(1);
    private BitField z = new BitField(3);
    private BitField rcode = new BitField(4);
    private BitField qdcount = new BitField(16);
    private BitField ancount = new BitField(16);
    private BitField nscount = new BitField(16);
    private BitField arcount = new BitField(16);

    public BitField getQdcount() {
        return qdcount;
    }

    public BitField getAncount() {
        return ancount;
    }

    public BitField getNscount() {
        return nscount;
    }

    public BitField getArcount() {
        return arcount;
    }


    DnsHeader(QR qr) { //new frame
        //todo: implement default parameters?

        addFieldsToList();

        //id
        //random generated 16-bit value
        Random r = new Random();
        for (int i = 0; i < id.size(); i++) {
            id.setBit(i, r.nextBoolean());
        }

        //qr
        //query or response
        if (qr == QR.query) {
            this.qr.setBit(false);
        } else if (qr == QR.response) {
            this.qr.setBit(true);
        }

        //opcode
        //todo: only standart query?
        opcode.setBit(0, 3, false);

        //aa
        aa.setBit(false);

        //tc
        tc.setBit(false);

        //rd
        rd.setBit(true);

        //ra
        ra.setBit(false);

        //z
        z.setBit(0, 2, false);

        //rcode
        rcode.setBit(0, 3, false);

        //qdcount
        qdcount.setBit(15, true); //0000000000000001

        //ancount
        //leave all to false

        //nscount
        //leave all to false

        //arcount
        //leave all to false

    }

    DnsHeader(byte[] headerBytes) { //from received bytes

        addFieldsToList();
        String binaryString = Utils.byteArraytoBinaryString(headerBytes);
        ListIterator<BitField> listIterator = listOfFields.listIterator();

        int binaryStringIterator = 0;
        while (listIterator.hasNext()) {
            BitField next = listIterator.next();
            int fieldSize = next.size();
            String bitFieldBinaryString = binaryString.substring(binaryStringIterator, binaryStringIterator + fieldSize);
            next.loadBinaryString(bitFieldBinaryString);
            binaryStringIterator += fieldSize;
        }
    }

    //todo: refactoring?
    public void addFieldsToList() {
        listOfFields.add(id);
        listOfFields.add(qr);
        listOfFields.add(opcode);
        listOfFields.add(aa);
        listOfFields.add(tc);
        listOfFields.add(rd);
        listOfFields.add(ra);
        listOfFields.add(z);
        listOfFields.add(rcode);
        listOfFields.add(qdcount);
        listOfFields.add(ancount);
        listOfFields.add(nscount);
        listOfFields.add(arcount);
    }

    public BitField getRcode() {
        return rcode;
    }
}
