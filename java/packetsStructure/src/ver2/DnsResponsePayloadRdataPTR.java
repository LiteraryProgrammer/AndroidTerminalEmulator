package ver2;

import alpha.labels.DomainNameLabels;

import java.util.Arrays;

/**
 * Created by gpietrus on 15.08.15.
 */
public class DnsResponsePayloadRdataPTR extends DnsResponsePayloadRdata {

    DomainNameLabels name;

    public DnsResponsePayloadRdataPTR(byte[] bytes) throws Exception {
        name = new DomainNameLabels(bytes);
    }

    public String toConsoleString(byte[] bytes) {
        //todo: refactor exceptions
        try {
            return "Domain:\t" + name.toFullString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
