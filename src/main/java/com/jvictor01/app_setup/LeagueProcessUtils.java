package com.jvictor01.app_setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LeagueProcessUtils {

    private final LeagueApisSetup leagueApisSetup = new LeagueApisSetup();

    private void startLeagueClient() {
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

    public boolean isLeagueUxRunning() {
        Runtime runtime = Runtime.getRuntime();
        String[] str = {"tasklist"};
        try {
            Process exec = runtime.exec(str);
            BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("leagueclientux.exe")) {
                    return true;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void setUpLeagueClient() {
        startLeagueClient();
        boolean leagueUxRunning = isLeagueUxRunning();

        while (!leagueUxRunning) {
            try {
                System.out.println("Waiting for League UX to start...");
                Thread.sleep(5000);
                leagueUxRunning = isLeagueUxRunning();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("League UX is running.");
        try {
            leagueApisSetup.setupLcuAndRiotApis();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
