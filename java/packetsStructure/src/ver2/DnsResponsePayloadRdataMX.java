package ver2;

import alpha.labels.DomainNameLabels;

import java.util.Arrays;

/**
 * Created by gpietrus on 15.08.15.
 */
public class DnsResponsePayloadRdataMX extends DnsResponsePayloadRdata {

    short preference;
    DomainNameLabels mailExchanger;

    public DnsResponsePayloadRdataMX(byte[] bytes) throws Exception {
        preference = Utils.byteArrayToShort(Arrays.copyOfRange(bytes,0,2));
        mailExchanger = new DomainNameLabels(Arrays.copyOfRange(bytes,2,bytes.length));
    }

    public String toConsoleString(byte[] bytes) {
        try {
            return "Preference:\t " + preference + "\n" +
                    "MailExchanger:\t" + mailExchanger.toFullString(bytes) + "\n";
        } catch (Exception e) {
            //todo: refactor eceptions
            e.printStackTrace();
            return null;
        }

    }
}
