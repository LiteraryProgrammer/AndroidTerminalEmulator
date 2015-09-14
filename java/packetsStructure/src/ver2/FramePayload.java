package ver2;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gpietrus on 01.08.15.
 */
public class FramePayload {
    protected List<BitField> listOfFields = new LinkedList<BitField>();

    public byte[] getBytes() {
        byte [] payloadBytes;
        String binaryString = "";
        for (BitField field : listOfFields) {
            binaryString += field.toString();
        }

        payloadBytes = Utils.binaryStringToByteArray(binaryString);
        return payloadBytes;
    }
}
