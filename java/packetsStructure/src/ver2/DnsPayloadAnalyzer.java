package ver2;

import alpha.labels.DomainNameLabels;
import com.sun.java.browser.plugin2.DOM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gpietrus on 15.08.15.
 */
public class DnsPayloadAnalyzer {

    private byte[] payloadBytes;
    private DnsHeader dnsHeader;
    private int globalIterator = 0;

    public List getDnsQueryList() {
        return dnsQueryList;
    }

    public List getDnsResponseList() {
        return dnsResponseList;
    }

    public List getDnsAuthorityResponseList() {
        return dnsAuthorityResponseList;
    }

    public List getDnsAdditionalResponseList() {
        return dnsAdditionalResponseList;
    }

    List dnsQueryList = new ArrayList<DnsQueryPayload>();
    List dnsResponseList = new ArrayList<DnsResponsePayload>();
    List dnsAuthorityResponseList = new ArrayList<DnsResponsePayload>();
    List dnsAdditionalResponseList = new ArrayList<DnsResponsePayload>();


    public void analyze() throws Exception {
        int qdcount = dnsHeader.getQdcount().toInt();
        int ancount = dnsHeader.getAncount().toInt();
        int nscount = dnsHeader.getNscount().toInt();
        int arcount = dnsHeader.getArcount().toInt();

        for(int i = 0 ; i < qdcount; i++) { //query payload
            dnsQueryList.add(analyzeDnsQueryPayload(Arrays.copyOfRange(payloadBytes, globalIterator, payloadBytes.length)));
        }

        for(int i = 0; i < ancount; i++) {
            dnsResponseList.add(analyzeDnsAnswerPayload(Arrays.copyOfRange(payloadBytes, globalIterator, payloadBytes.length)));
        }

        for(int i = 0; i < nscount; i++) {
            dnsAuthorityResponseList.add(analyzeDnsAnswerPayload(Arrays.copyOfRange(payloadBytes,globalIterator,payloadBytes.length)));
        }

        for(int i = 0; i < arcount; i++) {
            dnsAdditionalResponseList.add(analyzeDnsAnswerPayload(Arrays.copyOfRange(payloadBytes,globalIterator,payloadBytes.length)));
        }

    }

    public DnsPayloadAnalyzer(byte[] payloadBytes, DnsHeader dnsHeader) {
        this.payloadBytes = payloadBytes;
        this.dnsHeader = dnsHeader;
    }

    public DnsQuery analyzeDnsQueryPayload(byte[] bytes) throws Exception {
        Utils.printBinaryString(Utils.byteArraytoBinaryString(bytes));

        int iter = 0;

        DomainNameLabels domainNameLabels = new DomainNameLabels(bytes);
        iter = domainNameLabels.length();

        //moving forward

        //2 byte type field
        byte[] tmp = Arrays.copyOfRange(bytes, iter, iter + 2);
        short type = Utils.byteArrayToShort(Arrays.copyOfRange(bytes, iter, iter + 2));
        DnsPayload.RecordType recordType = DnsPayload.RecordType.fromValue(type);

        iter += 2;
        //2 byte class field

        short dnsClass = Utils.byteArrayToShort(Arrays.copyOfRange(bytes, iter, iter + 2));

        iter += 2;
        globalIterator += iter;

        return new DnsQuery(domainNameLabels,recordType,dnsClass);
    }

    public DnsResponsePayload analyzeDnsAnswerPayload(byte[] bytes) throws Exception {
        int iter = 0;

        DomainNameLabels domainNameLabels = new DomainNameLabels(bytes);
        iter += domainNameLabels.length();

        byte[] tmp = Arrays.copyOfRange(bytes, iter, iter + 2);
        short type = Utils.byteArrayToShort(tmp);
        DnsPayload.RecordType recordType = DnsPayload.RecordType.fromValue(type);

        iter += 2;

        short dnsClass = Utils.byteArrayToShort(Arrays.copyOfRange(bytes, iter, iter + 2));

        iter += 2;

        //4 byte ttl
        int ttl = Utils.byteArrayToInt(Arrays.copyOfRange(bytes,iter,iter+4));

        iter += 4;

        //2 byte data length
        short dataLength = Utils.byteArrayToShort(Arrays.copyOfRange(bytes,iter,iter+2));

        iter += 2;

        //rdata zalezne od dataLength
        byte[] rdata = Arrays.copyOfRange(bytes,iter,iter+dataLength);

        DnsResponsePayload dnsResponsePayload;
        DnsResponsePayloadRdata dnsResponsePayloadRdata = null;

        //todo: refactor to switch
        if (recordType == DnsPayload.RecordType.SOA) { //todo: implement more
            dnsResponsePayloadRdata = new DnsResponsePayloadRdataSOA(rdata);
        }
        else if(recordType == DnsPayload.RecordType.A) {
            dnsResponsePayloadRdata = new DnsResponsePayloadRdataA(rdata);
        }
        else if(recordType == DnsPayload.RecordType.AAAA) {
            dnsResponsePayloadRdata = new DnsResponsePayloadRdataAAAA(rdata);
        }
        else if (recordType == DnsPayload.RecordType.MX) {
            dnsResponsePayloadRdata = new DnsResponsePayloadRdataMX(rdata);
        }
        else if (recordType == DnsPayload.RecordType.PTR) {
            dnsResponsePayloadRdata = new DnsResponsePayloadRdataPTR(rdata);
        }
        else if (recordType == DnsPayload.RecordType.NS) {
            dnsResponsePayloadRdata = new DnsResponsePayloadRdataNS(rdata);
        }
        else if (recordType == DnsPayload.RecordType.TXT) {
            dnsResponsePayloadRdata = new DnsResponsePayloadRdataTXT(rdata);
        }
        else if (recordType == DnsPayload.RecordType.RP) {
            dnsResponsePayloadRdata = new DnsResponsePayloadRdataRP(rdata);
        }
        else {
            //todo: zaimplementowac wszystkie, co z pozostalymi?
//            System.out.println("implement more");
        }

        iter += rdata.length;
        int debug = 1;
        globalIterator += iter;

        return new DnsResponsePayload(domainNameLabels, type, dnsClass, ttl, dataLength, dnsResponsePayloadRdata);

    }

    /*
    public void analyzeDnsAuthoritativeNameserversPayload(byte[] bytes) throws Exception {
        tu skonczylem
    }

    public void analyzeAdditionalRecordsPayload(byte[] bytes) throws Exception {
        tu skonczylem
    }
    */

    public void analyzeRData(byte[] bytes, DnsPayload.RecordType recordType) {
        //todo: implement and return rdata object zalezny od typu rekordu
    }



}
