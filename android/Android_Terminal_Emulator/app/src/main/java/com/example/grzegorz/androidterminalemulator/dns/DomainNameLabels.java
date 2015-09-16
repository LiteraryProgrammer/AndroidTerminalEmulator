package com.example.grzegorz.androidterminalemulator.dns;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by grzegorz on 26.08.15.
 */
public class DomainNameLabels {

    //info
    //Note: Pointers, if used, terminate names. The name field may consist of a label (or sequence of labels) terminated with a zero length record OR a single pointer OR a label (or label sequence) terminated with a pointer.

    public String getLabelsString() {
        return labelsString;
    }

    public String labelsString = "";

    public int getPointerAddress() {
        return pointerAddress;
    }

    public int pointerAddress = 0;

    public DomainNameLabels(byte[] bytes) { //build from bytes
        int i = 0;
        int labelsLength = bytes.length;

        List labelsList = new ArrayList<String>();
        while(i < labelsLength) {

            //if not pointer
            if(!Utils.byteArraytoBinaryString(new byte[]{bytes[i]}).substring(0,2).equals("11")) {
                int labelLength = bytes[i];
                if(labelLength == 0) {
                    //end of name
                    break;
                }
                labelsList.add(new String(Arrays.copyOfRange(bytes, i + 1, i + 1 + labelLength)));
                i += labelLength + 1;
            }
            else {
                //if pointer
                //pointer ends the domainName
                //extract number of bytes
                //substract becaouse of first "11" bits
                //treat as unsigned
                pointerAddress = (Utils.byteArrayToShort(Arrays.copyOfRange(bytes,i,i+2)) & 0xFFFF)  - 0b1100000000000000;
                break; //end searching
            }
        labelsString = Joiner.on(".").join(labelsList);
        }
    }

    public int length() throws Exception { //returns length including 2 bytes for ending pointer

        //only pointer

        if(labelsString.length() == 0 && pointerAddress != 0) {
            return 2;
        }

        //label + pointer
        else if(labelsString.length() != 0 && pointerAddress !=0 ) {
            return labelsString.length() + 3;
        }

        //label + 0
        else if(labelsString.length() != 0 && pointerAddress == 0) {
            return labelsString.length() + 2;
        }

        else {
            throw new Exception();
        }

    }

    public String toString() {
        return labelsString + "\t" + "pointer " + pointerAddress;
    }


    public String toFullString(byte[] bytes) {
        if(pointerAddress != 0) {
            DomainNameLabels baseLabel = new DomainNameLabels(Arrays.copyOfRange(bytes, pointerAddress, bytes.length));
                return labelsString + ((labelsString.length() > 0) ? "." : "") + baseLabel.toFullString(bytes);
            }
        return labelsString;
        }
    }


