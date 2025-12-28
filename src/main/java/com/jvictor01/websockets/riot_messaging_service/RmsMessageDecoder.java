package com.jvictor01.websockets.riot_messaging_service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class RmsMessageDecoder {

    public String decode(ByteBuffer buffer) {
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        boolean isGzip = data.length >= 2
                && (data[0] & 0xFF) == 0x1F
                && (data[1] & 0xFF) == 0x8B;

        try {
            if (isGzip) {
                return gunzip(data);
            } else {
                return new String(data);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode RMS message", e);
        }
    }

    private String gunzip(byte[] compressed) throws Exception {
        GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(compressed));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;

        while ((len = gzip.read(buf)) != -1) {
            out.write(buf, 0, len);
        }

        return out.toString(StandardCharsets.UTF_8);
    }
}
