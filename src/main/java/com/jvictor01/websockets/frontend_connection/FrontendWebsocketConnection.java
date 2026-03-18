package com.jvictor01.websockets.frontend_connection;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrontendWebsocketConnection {
    private Socket client;
    private OutputStream outputStream;
    private volatile boolean isHeartbeatRunning = false;
    private final AtomicLong connectionId = new AtomicLong(0);

    public FrontendWebsocketConnection() {

    }

    public void connect() {
        try {
            final ServerSocket serverSocket = new ServerSocket(200);
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        Socket newClient = serverSocket.accept();
                        synchronized (this) {
                            closeConnection();
                            this.client = newClient;
                            this.outputStream = newClient.getOutputStream();
                        }
                        Long newConnectionId = connectionId.incrementAndGet();
                        InputStream inputStream = this.client.getInputStream();
                        this.outputStream = this.client.getOutputStream();
                        handshake(new Scanner(inputStream, StandardCharsets.UTF_8));
                        startHeartbeat(newConnectionId);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to start WebSocket server", e);
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(String message) {
        if (this.client == null || this.outputStream == null) {
            return;
        }

        try {
            byte[] rawData = message.getBytes(StandardCharsets.UTF_8);
            byte[] frame = new byte[rawData.length + 2];
            frame[0] = (byte) 0x81;
            frame[1] = (byte) rawData.length;
            System.arraycopy(rawData, 0, frame, 2, rawData.length);

            outputStream.write(frame);
            outputStream.flush();

        } catch (IOException e) {
            closeConnection();
        }
    }

    private synchronized void closeConnection() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (client != null && !client.isClosed()) {
                client.close();
            }
        } catch (IOException ignored) {
        } finally {
            this.outputStream = null;
            this.client = null;
            this.isHeartbeatRunning = false;
        }
    }


    private void startHeartbeat(Long newConnectionId) {
        isHeartbeatRunning = true;
        Thread heartbeat = new Thread(() -> {
            while (isHeartbeatRunning) {
                try {
                    Thread.sleep(5000);
                    if (this.connectionId.get() != newConnectionId) {
                        return;
                    }

                    if (sendPingSafe()) {
                        return;
                    }

                } catch (InterruptedException ignored) {
                    return;
                }
            }
        });

        heartbeat.setDaemon(true);
        heartbeat.start();
    }

    private boolean sendPingSafe() {
        synchronized (this) {
            if (client == null || client.isClosed() || outputStream == null) {
                return false;
            }
            sendMessage("PING");
            return true;
        }
    }


    private void handshake(Scanner scanner) {
        try {
            String data = scanner.useDelimiter("\\r\\n\\r\\n").next();
            Matcher get = Pattern.compile("^GET").matcher(data);

            if (get.find()) {
                Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);

                if (match.find()) {
                    String key = match.group(1).trim();
                    MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
                    String acceptKey = Base64.getEncoder()
                            .encodeToString(sha1.digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes(StandardCharsets.UTF_8)));

                    String response = "HTTP/1.1 101 Switching Protocols\r\n"
                            + "Connection: Upgrade\r\n"
                            + "Upgrade: websocket\r\n"
                            + "Sec-WebSocket-Accept: " + acceptKey + "\r\n\r\n";

                    outputStream.write(response.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }

            }
        } catch (NoSuchAlgorithmException | IOException e) {
            closeConnection();
        }
    }

}
