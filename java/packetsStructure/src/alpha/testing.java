package alpha;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.*;

/**
 * Created by grzegorz on 12.09.15.
 */
public class testing {


        public static void main(String[] args) {

            String text = "PING 46.4.242.141 (46.4.242.141) 56(84) bytes of data.\n" +
                    "64 bytes from 46.4.242.141: icmp_seq=1 ttl=50 time=51.3 ms\n" +
                    "\n" +
                    "--- 46.4.242.141 ping statistics ---\n" +
                    "1 packets transmitted, 1 received, 0% packet loss, time 0ms\n" +
                    "rtt min/avg/max/mdev = 51.331/51.331/51.331/0.000 ms\n";
            String patternString = "PING .* \\(([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})\\) .*\\n.*\\n.*\\n.*\\n.*1 received, 0% packet loss.*\\n.*\\n";



            Pattern pattern = Pattern.compile(patternString);

            Matcher matcher = pattern.matcher(text);
            boolean matches = matcher.matches();
            System.out.println(matches);

        }
    }

