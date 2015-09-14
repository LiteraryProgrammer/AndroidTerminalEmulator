package ver2;

import alpha.labels.DomainNameLabels;

import java.util.Arrays;

/**
 * Created by gpietrus on 02.08.15.
 */
public class DnsResponsePayload extends DnsPayload {

    DomainNameLabels domainNameLabels;
//    private byte[] type = new byte[2];
    private short type;
    private short dnsClass;
    private int ttl;
    private int dataLength;
    private DnsResponsePayloadRdata dnsResponsePayloadRdata;


    public DnsResponsePayload(DomainNameLabels domainNameLabels, short type, short dnsClass, int ttl, int dataLength, DnsResponsePayloadRdata dnsResponsePayloadRdata) {
        this.domainNameLabels = domainNameLabels;
        this.type = type;
        this.dnsClass = dnsClass;
        this.ttl = ttl;
        this.dataLength = dataLength;
        this.dnsResponsePayloadRdata = dnsResponsePayloadRdata;
    }

/*
    public DnsResponsePayload(byte[] payloadsBytes) throws Exception {

        int bytesIter = 0;
        //name
//        name = Arrays.copyOfRange(payloadsBytes, bytesIter, bytesIter + 2);
//        bytesIter += 2;

        domainNameLabels = new DomainNameLabels(payloadsBytes);
        bytesIter += domainNameLabels.length();

        //type
        type = Utils.byteArrayToShort(Arrays.copyOfRange(Arrays.copyOfRange(payloadsBytes, bytesIter, bytesIter + 2), bytesIter, bytesIter + 2));
        bytesIter += 2;

        //dnsClass
        dnsClass =  Utils.byteArrayToShort(Arrays.copyOfRange(Arrays.copyOfRange(payloadsBytes, bytesIter, bytesIter + 2), bytesIter, bytesIter + 2));
        bytesIter += 2;

        //ttl
        ttl = Utils.byteArrayToInt(Arrays.copyOfRange(Arrays.copyOfRange(payloadsBytes, bytesIter, bytesIter + 4), bytesIter, bytesIter + 4));
        bytesIter += 4;

        //rdlength
        dataLength = Utils.byteArrayToShort(Arrays.copyOfRange(Arrays.copyOfRange(payloadsBytes, bytesIter, bytesIter + 2),bytesIter,bytesIter+2));
        bytesIter += 2;

        //rdata
        //int rdlengthInt = Utils.byteArrayToShort(dataLength);
        int rdlengthInt = dataLength;
        rdata = Arrays.copyOfRange(payloadsBytes, bytesIter, bytesIter + rdlengthInt);
    }
*/

/*
    private void addToList() {

        listOfFields.add(Utils.byteArrayToBitField(qnameBytes)); //todo: konwecja to bitfield ale po zainicjalizowaniu?
        listOfFields.add(qtype);
        listOfFields.add(qclass);
    }
    */
/*
    public String getDomainName() {

        StringJoiner stringJoiner = new StringJoiner(".");
        int labelPartLength = qnameBytes[0];
        int labelIter = 1;
        while(labelPartLength != 0) {
            String domainNamePart = "";
            int i;
            for (i = labelIter; i < labelIter + labelPartLength; i++) {
                domainNamePart += (char) (qnameBytes[i] & 0xFF);
            }
            labelPartLength = qnameBytes[i];
            labelIter = i + 1;
            stringJoiner.add(domainNamePart);
        }

        return stringJoiner.toString();
    }
*/

    public String toConsoleString(byte[] bytes) {

        StringBuilder stringBuilder = new StringBuilder();

        //todo: refactor exceptions
        try {
            stringBuilder.append(domainNameLabels.toFullString(bytes) + " \n");
        stringBuilder.append("Type:\t" + RecordType.fromValue(type) + "\n");
            stringBuilder.append("Class:\t" + dnsClass + "\n");
            stringBuilder.append("TTL:\t" + ttl + "\n");
            stringBuilder.append("DataLength:\t" + dataLength + "\n");
            stringBuilder.append("Payload:\t" + dnsResponsePayloadRdata.toConsoleString(bytes) + "\n");
            stringBuilder.append("\n");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public String getAddress() {



        //todo: zalozenie ze 4 bajty tylko
        //todo: sprawdzic co jak bedzie ich wiecej

        StringBuilder address = new StringBuilder();
/*
        for (int i = 0; i < rdata.length; i++) {
            address.append(rdata[i] & 0xFF); //interprete as unsigned
            address.append(".");
        }*/
        return address.toString();

    }

    public int getTtl() {
        return ttl;
    }


}
