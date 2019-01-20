package com.greenbee.commons;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class IOLib {

    public static void close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    public static byte[] readBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 4);

        byte[] buf = new byte[1024];
        for (int size; (size = stream.read(buf)) > 0; ) {
            bos.write(buf, 0, size);
        }

        return bos.toByteArray();
    }
}
