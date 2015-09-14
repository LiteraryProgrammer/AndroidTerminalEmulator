package ver2;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gpietrus on 01.08.15.
 */
public class FrameHeader {

    protected List<BitField> listOfFields = new LinkedList<BitField>();

    public byte[] getBytes() {

        byte[] headerBytes;
        String binaryString = "";
        for (BitField field : listOfFields) {
           binaryString += field.toString();
        }

        headerBytes = Utils.binaryStringToByteArray(binaryString);
        return headerBytes;
    }
}
