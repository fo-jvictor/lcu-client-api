package com.jvictor01.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeagueClientAuthentication {
    public static String AUTHORIZATION_TOKEN;
    public static int SERVER_PORT;
    public static final String LCU_BASE_ENDPOINT = "https://127.0.0.1:";

    public void connectToLCUApi() throws IOException, InterruptedException {
        String[] getLeagueClientUXInfo = {"wmic", "process", "where", "Name='LeagueClientUx.exe'", "get", "commandLine"};
        Pattern authTokenRegexPattern = Pattern.compile("--remoting-auth-token=([\\w-]*)");
        Pattern portNumberRegexPattern = Pattern.compile("--app-port=([0-9]*)");

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(getLeagueClientUXInfo);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        int count = 0;
        while ((line = reader.readLine()) != null) {
            count++;
            if (count > 2) {
                Matcher token_matcher = authTokenRegexPattern.matcher(line);
                Matcher port_matcher = portNumberRegexPattern.matcher(line);
                if (token_matcher.find()) {
                    AUTHORIZATION_TOKEN = encodeToBase64(token_matcher.group(1));
                }
                if (port_matcher.find()) {
                    SERVER_PORT = Integer.parseInt(port_matcher.group(1));
                }
            }
        }
        System.out.println("AUTH TOKEN: " + AUTHORIZATION_TOKEN);
        System.out.println("SERVER PORT: " + SERVER_PORT);
        process.waitFor();
        reader.close();
    }

    private String encodeToBase64(String input) {
        String passwordToBeEncoded = "riot:" + input;
        byte[] encodedBytes = Base64.getEncoder().encode(passwordToBeEncoded.getBytes());
        return new String(encodedBytes);
    }


    public static String kkkvsf() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }


}
