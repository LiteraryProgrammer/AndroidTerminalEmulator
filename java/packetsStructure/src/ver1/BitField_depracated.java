package ver1;

/**
 * Created by gpietrus on 26.07.15.
 */
public class BitField_depracated {
    private Boolean bits[];

    BitField_depracated(int length) {
        bits = new Boolean[length];
    }

    void set(int position, Boolean value) {
        bits[position] = value;
    }

    void set(int positionStart, int positionEnd, Boolean value) {
        for(int position = positionStart; position <= positionEnd; position++) {
            bits[position] = value;
        }
    }

    Boolean get(int position) {
        return bits[position];
    }

    int size() {
        return bits.length;
    }



    //todo: implement copying
}
