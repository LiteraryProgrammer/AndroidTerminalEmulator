package com.example.grzegorz.androidterminalemulator.dns;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gpietrus on 02.08.15.
 */
public class DnsPayload extends FramePayload {

    public enum RecordType {
        A(1), NS(2), CNAME(5), SOA(6), WKS(11), PTR(12), MX(15), TXT(16), RP(17), AAAA(28), SRV(33), A6(38), ANY(255);
        //tu skonczylem zaimplementowac RP 17
        //add unsupported
        public int value;

        RecordType(int value) {
            this.value = value;
        }

        public static RecordType fromValue(short value) throws Exception {
            for(RecordType recordType : values()) {
                if(recordType.value == value) {
                    return recordType;
                }
            }
            throw new Exception(); //todo: change to illegal argument exception
        }


    }

    public enum NameType {
        SEQ_OF_LABELS, POINTER
    }

    public static NameType dnsNameType(byte[] bytes) { //determine if name is a sequence of labels or a pointer
        //check first byte, if starts with 11... - pointer
        if(Utils.byteArraytoBinaryString(new byte[]{bytes[0]}).substring(0,2).equals("11")) {
            return NameType.POINTER;
        };
        return NameType.SEQ_OF_LABELS;
    }

    public static String decodeDnsLabels(byte[] bytes) {
        //todo: depracated - to remove or refactor, replaced by DomainNameLabels
        //find zero byte - terminating
        int zeroByteIndex = Utils.findFirstZeroByte(bytes); //todo: czy to sie musi konczyc \0? chyba nie
        byte[] labelsBytes = Arrays.copyOfRange(bytes, 0, zeroByteIndex);

        int labelsLength = labelsBytes.length;
        int i = 0;
        List labelsList = new ArrayList<String>();
        while(i < labelsLength) {
            //first byte in each label is length
            int labelLength = labelsBytes[i]; //tu zwraca -64 nie wiem czemu?
            int endOfLabel = i + labelLength + 1;
            labelsList.add(new String(Arrays.copyOfRange(labelsBytes, i + 1, endOfLabel)));
            //todo :tu skonczylem blad endOfLabel = -58 skad?
            i = endOfLabel;
        }

        return Joiner.on(".").join(labelsList);
    }

}
