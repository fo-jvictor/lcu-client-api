package com.jvictor01.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeagueClientAuthentication {
    public static String LCU_AUTHORIZATION_TOKEN;
    public static int LCU_SERVER_PORT;
    public static int RIOT_CLIENT_SERVER_PORT;
    public static String RIOT_CLIENT_AUTH_TOKEN;
    public static final String LCU_BASE_ENDPOINT = "https://127.0.0.1:";

    public void connectToLCUApi() throws IOException, InterruptedException {
        String[] getLeagueClientUXInfo = {"wmic", "process", "where", "Name='LeagueClientUx.exe'", "get", "commandLine"};
        Pattern lcuApiAuthTokenRegexPattern = Pattern.compile("--remoting-auth-token=([\\w-]*)");
        Pattern lcuApiPortNumberRegexPattern = Pattern.compile("--app-port=([0-9]*)");

        Pattern riotClientPortPattern = Pattern.compile("--riotclient-app-port=([0-9]*)");
        Pattern riotClientTokenPattern = Pattern.compile("--riotclient-auth-token=([\\w-]*)");

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(getLeagueClientUXInfo);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        int count = 0;
        while ((line = reader.readLine()) != null) {
            count++;
            if (count > 2) {
                Matcher lcu_api_token_matcher = lcuApiAuthTokenRegexPattern.matcher(line);
                Matcher lcu_api_port_matcher = lcuApiPortNumberRegexPattern.matcher(line);
                Matcher riotClientPortMatcher = riotClientPortPattern.matcher(line);
                Matcher riotClientTokenMatcher = riotClientTokenPattern.matcher(line);

                if (lcu_api_token_matcher.find()) {
                    LCU_AUTHORIZATION_TOKEN = encodeToBase64(lcu_api_token_matcher.group(1));
                }
                if (lcu_api_port_matcher.find()) {
                    LCU_SERVER_PORT = Integer.parseInt(lcu_api_port_matcher.group(1));
                }

                if (riotClientPortMatcher.find()) {
                    RIOT_CLIENT_SERVER_PORT = Integer.parseInt(riotClientPortMatcher.group(1));
                }

                if (riotClientTokenMatcher.find()) {
                    RIOT_CLIENT_AUTH_TOKEN = encodeToBase64(riotClientTokenMatcher.group(1));
                }
            }
        }
        System.out.println("LCU AUTH TOKEN: " + LCU_AUTHORIZATION_TOKEN);
        System.out.println("LCU PORT: " + LCU_SERVER_PORT);

        System.out.println("RIOT CLIENT PORT: " + RIOT_CLIENT_SERVER_PORT);
        System.out.println("RIOT CLIENT AUTH TOKEN: " + RIOT_CLIENT_AUTH_TOKEN);
        process.waitFor();
        reader.close();
    }

    private String encodeToBase64(String input) {
        String passwordToBeEncoded = "riot:" + input;
        byte[] encodedBytes = Base64.getEncoder().encode(passwordToBeEncoded.getBytes());
        return new String(encodedBytes);
    }


}
