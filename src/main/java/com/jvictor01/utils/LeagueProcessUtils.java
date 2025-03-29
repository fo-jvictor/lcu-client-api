package com.jvictor01.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LeagueProcessUtils {

    private static void startLeagueClient() {
        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);

        //TODO: find a way to get the path of the LeagueClientServices.exe dynamically
        builder.command(
                "C:\\Riot Games\\Riot Client\\RiotClientServices.exe",
                "--launch-product=league_of_legends",
                "--launch-patchline=live",
                "--client-config-url=https://clientconfig.rpg.riotgames.com"
        );
        try {
            Process process = builder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isLeagueRunning() {
        try {
            Process process = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("leagueclient.exe")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setUpLeagueClient() {
        if (!isLeagueRunning()) {
            startLeagueClient();
            System.out.println("Starting League of Legends...");
            setUpLeagueClient();
        } else {
            System.out.println("League of Legends is up and running.");
        }
    }


}
