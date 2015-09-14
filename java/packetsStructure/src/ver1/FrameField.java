package ver1;

/**
 * Created by gpietrus on 30.07.15.
 */
public class FrameField {

    private boolean bits[];
    private int size;

    public FrameField(int size) {
        bits = new boolean[size];
        this.size = size;
    }

    public FrameField(byte[] bytes) { //constructor from bytes array
        System.out.println("ver1.FrameField from bytes");
        System.out.println(bytes.length);
        int size = bytes.length*8;
        bits = new boolean[size];
        this.size = size;

        int j = 0;
        for (int i = 0; i < bytes.length; i++) {
            String bitString = String.format("%8s", Integer.toBinaryString(bytes[i])).replace(' ', '0');
            for (char c : bitString.toCharArray()) {
                bits[j] = c == '1' ? true : false;
                j++;
            }
        }
    }

    public void setBit(int index, boolean value) {
        bits[index] = value;
    }

    public void setBit(int indexStart, int indexStop, boolean value) {
        for (int i = indexStart; i <= indexStop; i++) {
            setBit(i,value);
        }
    }

    public boolean getBit(int index) {
        return bits[index];
    }

    public int size() {
        return size;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(bits[i] == true ? "1" : "0");
        }
        return stringBuilder.toString();
    }

}
