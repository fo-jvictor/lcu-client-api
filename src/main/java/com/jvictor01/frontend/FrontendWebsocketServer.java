package com.jvictor01.frontend;


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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrontendWebsocketServer {
    private Socket client;
    private OutputStream outputStream;


    public FrontendWebsocketServer() throws IOException {

    }

    public void connect() {
        try {
            final ServerSocket serverSocket = new ServerSocket(200);
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        client = serverSocket.accept();
                        System.out.println("Client connected: ");
                        InputStream inputStream = client.getInputStream();
                        outputStream = client.getOutputStream();
                        Scanner scanner = new Scanner(inputStream, "UTF-8");
                        handshake(scanner);
                    } catch (IOException ioException) {
                    }
                }
            });

            thread.start();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void sendMessage(String message) {
        if (outputStream != null && !client.isClosed() && client != null) {
            try {
                byte[] rawData = message.getBytes(StandardCharsets.UTF_8);
                int frameSize = rawData.length + 2;

                byte[] frame = new byte[frameSize];
                frame[0] = (byte) 0x81;
                frame[1] = (byte) rawData.length;
                System.arraycopy(rawData, 0, frame, 2, rawData.length);

                outputStream.write(frame);
                outputStream.flush();

                System.out.println("message sent: " + message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.err.println("No client connected, unable to send message: " + message);

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
                    System.out.println("message sent: " + response);
                }

            }
        } catch (NoSuchAlgorithmException | IOException e) {

        }

    }

}
