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


}
