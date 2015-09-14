package ver2;

/**
 * Created by gpietrus on 01.08.15.
 */
public class BitField {

    private boolean[] bits;

    BitField(int size) {
        bits = new boolean[size];
        setBit(0,size-1,false); //initialize all bits to false
    }

    public int size() {
        return bits.length;
    }

    public void setBit(int index, boolean value) {
        bits[index] = value;
    }

    public void setBit(boolean value) { //set bit at 0 index
        bits[0] = value;
    }

    public void setBit(int indexStart, int indexStop, boolean value) {
        for (int i = indexStart; i <= indexStop; i++) {
            bits[i] = value;
        }
    }

    public void loadBinaryString(String binaryString) {
        for (int i = 0; i < bits.length; i++) {
            bits[i] = binaryString.charAt(i) == '1';
        }
    }

    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bits.length; i++) {
            stringBuilder.append(bits[i] == true ? "1" : 0);
        }

        return stringBuilder.toString();
    }

    public int toInt() {
        return Integer.parseInt(toString(),2);
    }
}
