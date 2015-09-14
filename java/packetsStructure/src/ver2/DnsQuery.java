package ver2;

import alpha.labels.DomainNameLabels;

/**
 * Created by gpietrus on 15.08.15.
 */
public class DnsQuery {
    private DomainNameLabels domainNameLabels;
    private DnsPayload.RecordType recordType;
    private short dnsClass; //todo: change to enum?

    public DnsPayload.RecordType getRecordType() {
        return recordType;
    }

    public short getDnsClass() {
        return dnsClass;
    }

    public DomainNameLabels getDomainNameLabels() {
        return domainNameLabels;
    }

    public DnsQuery(DomainNameLabels domainNameLabels, DnsPayload.RecordType recordType, short dnsClass) {
        this.domainNameLabels = domainNameLabels;
        this.recordType = recordType;
        this.dnsClass = dnsClass;
    }

    public String toString() {
        return "Domain name: " + domainNameLabels.toString() +
                "\nRecord type: " + recordType +
                "\nClass: " + dnsClass;
    }
}
