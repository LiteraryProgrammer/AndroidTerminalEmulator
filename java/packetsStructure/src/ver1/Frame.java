package ver1;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gpietrus on 30.07.15.
 */
public class Frame {

    private boolean[] bits;
    private int size; //size in bits

    public boolean[] getBits() {
        return bits;
    }

    public byte[] getBytes() {
        //convering bits to bytes
        //todo: czy moze sie zdazyc ze ilosc bitow nie jest wielokrotnoscia 8?

        byte[] bytes = new byte[size/8];

        int i = 0;
        List<String> listOfBinaryStrings = getListOfBinaryStrings();
        for (String binaryString : listOfBinaryStrings) {
            bytes[i] = Byte.parseByte(binaryString, 2);
            i++;
        }
        return bytes;
    }

    private List<String> getListOfBinaryStrings() {
        int i = 0;
        List<String> listOfBinaryStrings = new LinkedList<String>();
        while (i < size) {
            String string = "";
            for (int j = i; j < i + 8; j++) {
                string += bits[j] == true ? "1" : "0";
            }
            listOfBinaryStrings.add(string);
            i += 8;
        }
        return listOfBinaryStrings;
    }

    public int size() {
        return size;
    }

    public Frame(List<FrameField> listOfFrameFields) {

        //size:
        //dlugosc laczna wszystkich bitow na liscie

        int size = 0;
        System.out.println("rozmiary pol");
        for (FrameField frameField : listOfFrameFields) {
            size += frameField.size();
            System.out.println(frameField.size());
        }

        System.out.println("rozmiar ramki\t" + size);

        this.size = size;


        //
        //deklarajca tablicy
        bits = new boolean[size];


        //laczenie

        int iter = 0;
        for (FrameField frameField : listOfFrameFields) {
            System.out.println("pole o dlugosci\t" + frameField.size());
            for (int i = 0; i < frameField.size(); i++) {
                bits[iter] = frameField.getBit(i);
                System.out.println(iter + "\t" + bits[iter]);
                iter++;
            }
        }


    }

}
