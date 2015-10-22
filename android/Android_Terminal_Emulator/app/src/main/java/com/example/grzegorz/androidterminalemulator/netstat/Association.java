package com.example.grzegorz.androidterminalemulator.netstat;

public class Association {
    //todo: change types
    String srcAddress;
    String srcPort;
    String dstAddress;
    String dstPort;
    String uid; //todo: potrzebne?
    ConnectionType connectionType;
    TCP.TcpState tcpState;

    @Override
    public String toString() {
        String retString =
                        "Association{" +
                        "srcAddress='" + srcAddress + '\'' +
                        ", srcPort='" + srcPort + '\'' +
                        ", dstAddress='" + dstAddress + '\'' +
                        ", dstPort='" + dstPort + '\'' +
                        ", uid='" + uid + '\'';
//        if (connectionType == connectionType.TCP) {
            retString += ", connectionType=" + connectionType +
                    ", tcpState=" + tcpState;
//        }
        retString += '}';
        return retString;
    }

    public Association(String srcAddress, String srcPort, String dstAddress, String dstPort, String uid, ConnectionType connectionType, TCP.TcpState tcpState) {
        this.srcAddress = srcAddress;
        this.srcPort = srcPort;
        this.dstAddress = dstAddress;
        this.dstPort = dstPort;
        this.uid = uid;
        this.connectionType = connectionType;
        this.tcpState = tcpState;
    }

}