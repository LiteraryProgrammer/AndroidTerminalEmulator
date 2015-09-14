package ver1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //ver1.DNSFrame dnsframe = ver2 ver1.DNSFrame("poczta.onet.pl");
        //dnsframe.compileFrame();

        Nslookup nslookup = new Nslookup();
        nslookup.run();
    }


}


