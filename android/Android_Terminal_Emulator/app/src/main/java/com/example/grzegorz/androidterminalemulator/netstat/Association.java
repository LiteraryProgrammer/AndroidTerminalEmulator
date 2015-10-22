package com.example.grzegorz.androidterminalemulator.netstat;

public class Association {

    //todo: change types
    ConnectionType connectionType;
    String recvq;
    String sendq;

    String srcAddress;
    String srcPort;

    String dstAddress;
    String dstPort;

    //    String uid; //todo: potrzebne?
    TCP.TcpState tcpState;

    public Association(ConnectionType connectionType, String recvq, String sendq, String srcAddress, String srcPort, String dstAddress, String dstPort, TCP.TcpState tcpState) {
        this.connectionType = connectionType;
        this.recvq = recvq;
        this.sendq = sendq;
        this.srcAddress = srcAddress;
        this.srcPort = srcPort;
        this.dstAddress = dstAddress;
        this.dstPort = dstPort;
        this.tcpState = tcpState;
    }

    @Override
    public String toString() {
        return "Association{" +
                "connectionType=" + connectionType +
                ", recvq='" + recvq + '\'' +
                ", sendq='" + sendq + '\'' +
                ", srcAddress='" + srcAddress + '\'' +
                ", srcPort='" + srcPort + '\'' +
                ", dstAddress='" + dstAddress + '\'' +
                ", dstPort='" + dstPort + '\'' +
                ", tcpState=" + (connectionType == ConnectionType.TCP ? tcpState : "n/a") +
                '}';
    }

    public String toConsoleString() {
        return connectionType.toString() + "\t" +
                recvq + "\t" +
                sendq + "\t" +
                srcAddress + "\t" +
                srcPort + "\t" +
                dstAddress + "\t" +
                dstPort + "\t" +
                tcpState + "\n";
    }
}