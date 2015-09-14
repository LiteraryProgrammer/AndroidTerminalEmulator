package ver2;

import alpha.labels.DomainNameLabels;

import java.util.Arrays;

/**
 * Created by gpietrus on 15.08.15.
 */
public class DnsResponsePayloadRdataTXT extends DnsResponsePayloadRdata {

    private long txtLength;
    private String txt;

    public DnsResponsePayloadRdataTXT(byte[] bytes) throws Exception {

        txtLength = bytes[0];
        txt = new String(Arrays.copyOfRange(bytes,1,bytes.length));


    }

    public String toConsoleString(byte[] bytes) {
        return "TXT length:\t" + txtLength + "\n" + "TXT:\t" + txt + "\n";
    }
}
