package com.example.grzegorz.androidterminalemulator.netstat;

/**
 * Created by grzegorz on 15.10.15.
 */
public class TCP {

    public static enum TcpState {
        TCP_ESTABLISHED(1),
        TCP_SYN_SENT(2),
        TCP_SYN_RECV(3),
        TCP_FIN_WAIT1(4),
        TCP_FIN_WAIT2(5),
        TCP_TIME_WAIT(6),
        TCP_CLOSE(7),
        TCP_CLOSE_WAIT(8),
        TCP_LAST_ACK(9),
        TCP_LISTEN(10),
        TCP_CLOSING(11);

        private final int id;

        TcpState(int id) {
            this.id = id;
        }

        public int getValue() {
            return id;
        }

        static TcpState getById(int id) {
            for (TcpState tcpState : values()) {
                if (tcpState.id == id) return tcpState;
            }
            return null;
        }


    }
}
