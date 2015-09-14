package ver1;

/**
 * Created by gpietrus on 26.07.15.
 */
public class Alpha {
    public static void main(String[] args) {
        String domain = "poczta.onet.pl";
        String labels[] = domain.split("\\."); //split by dot

        for(String label : labels) {
            byte length = (byte) label.length();
            //System.out.println(String.format("%16s", Integer.toBinaryString(length)) + "\t" + length + "\t" + label);
            byte b[] = new byte[length + 1]; // label length + length byte
            b[0] = length;

            int i = 1;
            for (char c : label.toCharArray()) {
                b[i] = (byte) c;
                i++;
            }

            for(byte one : b) {
                System.out.println(one);
            }
            System.out.println("---");

        }





    }
}
