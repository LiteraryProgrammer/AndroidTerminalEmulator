package com.example.grzegorz.androidterminalemulator.dns;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gpietrus on 02.08.15.
 */
public class DnsQueryPayload extends DnsPayload {

    private byte qnameBytes[];
    private BitField qtype = new BitField(16);
    private BitField qclass = new BitField(16);

    public DnsQueryPayload(byte[] payloadsBytes) {
        int qnameLength = payloadsBytes.length-4;
        qnameBytes = new byte[qnameLength];
        qnameBytes = Arrays.copyOfRange(payloadsBytes, 0, qnameLength);

        byte[] qtypeBytes = Arrays.copyOfRange(payloadsBytes,qnameLength,qnameLength+2);
        byte[] qclassBytes = Arrays.copyOfRange(payloadsBytes,qnameLength+2,qnameLength+4);

        qtype = Utils.byteArrayToBitField(qtypeBytes);
        qclass = Utils.byteArrayToBitField(qclassBytes);
    }

    private void addToList() {

        listOfFields.add(Utils.byteArrayToBitField(qnameBytes));
        listOfFields.add(qtype);
        listOfFields.add(qclass);
    }

    DnsQueryPayload(String domainName, RecordType recordType) {
        //qname

        String domainParts[] = domainName.split("\\."); //split by dot
        qnameBytes = new byte[Joiner.on("").join(domainParts).length() + domainParts.length + 1]; //length of all domainParts without dots + numer of length bytes + ending \0

        int qnameIterator = 0;

        for (String domainPart : domainParts) {
            byte domainPartLength = (byte) domainPart.length();
            byte label[] = new byte[domainPartLength + 1]; // label length + length byte
            label[0] = domainPartLength;

            int i = 1;
            for (char c : domainPart.toCharArray()) {
                label[i] = (byte) c;
                i++;
            }

            for (byte one : label) {
                qnameBytes[qnameIterator] = one;
                qnameIterator++;
            }
        }
        qnameBytes[qnameIterator] = 0; //last byte to 0x00

        //qtype
        qtype.loadBinaryString(Utils.intToBinaryString(recordType.value, qtype.size()));

        //qclass
        //todo: verify?
        qclass.setBit(15,true);

        //adding to list
        addToList();
    }

    public String getDomainName() {

        List<String> domainNameParts = new ArrayList<String>();
        int labelPartLength = qnameBytes[0];
        int labelIter = 1;
        while(labelPartLength != 0) {
            String domainNamePart = "";
            int i;
            for (i = labelIter; i < labelIter + labelPartLength; i++) {
                domainNamePart += (char) (qnameBytes[i] & 0xFF);
            }
            labelPartLength = qnameBytes[i];
            labelIter = i + 1;
            domainNameParts.add(domainNamePart);
        }

        return Joiner.on(".").join(domainNameParts);
    }

}
