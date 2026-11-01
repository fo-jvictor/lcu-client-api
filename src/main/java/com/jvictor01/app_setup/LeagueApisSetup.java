package com.jvictor01.app_setup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.utils.qr_code.QrCodeGenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LeagueApisSetup {
    public static String LCU_AUTHORIZATION_TOKEN;
    public static int LCU_SERVER_PORT;
    public static int RIOT_CLIENT_SERVER_PORT;
    public static String RIOT_CLIENT_AUTH_TOKEN;
    public static final String LOCAL_HOST = "https://127.0.0.1:";

    private static final String LOCKFILE_NAME = "lockfile";
    private static final String DEFAULT_LEAGUE_INSTALL_DIR = "C:\\Riot Games\\League of Legends";
    private static final List<String> LEAGUE_CLIENT_EXECUTABLES = List.of("leagueclientux.exe", "leagueclient.exe");

    public void setupLcuAndRiotApis() throws IOException, InterruptedException {
        String lcuLockfilePath = findLcuLockfilePath().toString();
        String lcuLockfileContent = getFileContent(lcuLockfilePath);
        String[] split = lcuLockfileContent.split(":");

        LCU_AUTHORIZATION_TOKEN = encodeToBase64(split[3]);
        LCU_SERVER_PORT = Integer.parseInt(split[2]);

        String riotClientLockfilePath = findRiotClientLockfilePath().toString();
        String riotClientLockfileContent = getFileContent(riotClientLockfilePath);
        String[] riotClientLockfileSplit = riotClientLockfileContent.split(":");

        RIOT_CLIENT_AUTH_TOKEN = encodeToBase64(riotClientLockfileSplit[3]);
        RIOT_CLIENT_SERVER_PORT = Integer.parseInt(riotClientLockfileSplit[2]);
    }


    private Path findLcuLockfilePath() throws FileNotFoundException {
        Set<Path> candidateDirs = new LinkedHashSet<>();
        findRunningLeagueClientDir().ifPresent(candidateDirs::add);
        candidateDirs.addAll(findLeagueInstallDirsFromRiotMetadata());
        candidateDirs.add(Path.of(DEFAULT_LEAGUE_INSTALL_DIR));

        for (Path dir : candidateDirs) {
            Path lockfile = dir.resolve(LOCKFILE_NAME);
            if (Files.isRegularFile(lockfile)) {
                return lockfile;
            }
        }

        throw new FileNotFoundException("LCU lockfile not found. Searched in: " + candidateDirs);
    }

    private Path findRiotClientLockfilePath() throws FileNotFoundException {
        String localAppData = System.getenv("LOCALAPPDATA");
        Path localAppDataDir = localAppData != null
                ? Path.of(localAppData)
                : Path.of(System.getProperty("user.home"), "AppData", "Local");

        Path lockfile = localAppDataDir.resolve(Path.of("Riot Games", "Riot Client", "Config", LOCKFILE_NAME));
        if (!Files.isRegularFile(lockfile)) {
            throw new FileNotFoundException("Riot Client lockfile not found at: " + lockfile);
        }
        return lockfile;
    }

    private Optional<Path> findRunningLeagueClientDir() {
        return ProcessHandle.allProcesses()
                .map(process -> process.info().command())
                .flatMap(Optional::stream)
                .map(Path::of)
                .filter(command -> {
                    Path fileName = command.getFileName();
                    return fileName != null && LEAGUE_CLIENT_EXECUTABLES.contains(fileName.toString().toLowerCase());
                })
                .map(Path::getParent)
                .filter(dir -> dir != null && Files.isRegularFile(dir.resolve(LOCKFILE_NAME)))
                .findFirst();
    }

    private List<Path> findLeagueInstallDirsFromRiotMetadata() {
        List<Path> dirs = new ArrayList<>();
        String programData = System.getenv("PROGRAMDATA");
        Path riotGamesData = Path.of(programData != null ? programData : "C:\\ProgramData", "Riot Games");

        Path productSettings = riotGamesData.resolve(Path.of("Metadata", "league_of_legends.live",
                "league_of_legends.live.product_settings.yaml"));
        try {
            if (Files.isRegularFile(productSettings)) {
                for (String line : Files.readAllLines(productSettings)) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("product_install_full_path:")) {
                        String value = trimmed.substring(trimmed.indexOf(':') + 1).trim().replace("\"", "");
                        if (!value.isEmpty()) dirs.add(Path.of(value));
                    }
                }
            }
        } catch (Exception ignored) {
        }

        Path riotClientInstalls = riotGamesData.resolve("RiotClientInstalls.json");
        try {
            if (Files.isRegularFile(riotClientInstalls)) {
                JsonNode associatedClients = new ObjectMapper().readTree(riotClientInstalls.toFile()).path("associated_client");
                List<Path> pbeDirs = new ArrayList<>();
                Iterator<String> installDirs = associatedClients.fieldNames();
                while (installDirs.hasNext()) {
                    String installDir = installDirs.next();
                    if (!installDir.toLowerCase().contains("league of legends")) continue;
                    if (installDir.toLowerCase().contains("pbe")) pbeDirs.add(Path.of(installDir));
                    else dirs.add(Path.of(installDir));
                }
                dirs.addAll(pbeDirs);
            }
        } catch (Exception ignored) {
        }

        return dirs;
    }

    private String getFileContent(String path) {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            String fileContent = new String(fileInputStream.readAllBytes());
            fileInputStream.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String encodeToBase64(String input) {
        String passwordToBeEncoded = "riot:" + input;
        byte[] encodedBytes = Base64.getEncoder().encode(passwordToBeEncoded.getBytes());
        return new String(encodedBytes);
    }

    public void displayConnectionInfo() {
        System.out.println("LCU AUTH TOKEN: " + LCU_AUTHORIZATION_TOKEN);
        System.out.println("LCU PORT: " + LCU_SERVER_PORT);
        System.out.println("RIOT CLIENT PORT: " + RIOT_CLIENT_SERVER_PORT);
        System.out.println("RIOT CLIENT AUTH TOKEN: " + RIOT_CLIENT_AUTH_TOKEN);
        System.out.println("\nAccess the following QR code via mobile, " +
                "MAKE SURE YOU'RE CONNECTED IN THE SAME NETWORK AS YOUR PC");

        try {
            QrCodeGenerator.printQrCode("http://" + getIp() + ":8080");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getIp() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface net = interfaces.nextElement();

            if (!net.isUp() || net.isLoopback()) continue;

            Enumeration<InetAddress> addresses = net.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();

                if (addr instanceof Inet4Address) {
                    return addr.getHostAddress();
                }
            }
        }
        return "";
    }


}
