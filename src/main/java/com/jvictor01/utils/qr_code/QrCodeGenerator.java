package com.jvictor01.utils.qr_code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QrCodeGenerator {

    public static void printQrCode(String text) throws WriterException {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 1, 1);

        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                System.out.print(matrix.get(x, y) ? "██" : "  ");
            }
            System.out.println();
        }
    }
}
