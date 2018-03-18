package org.anhcraft.spaciouslib.utils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtils {
    public static byte[] compress(byte[] b) throws IOException {
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(b);
        gzip.flush();
        gzip.close();
        return obj.toByteArray();
    }

    public static byte[] decompress(final byte[] compressed) throws IOException {
        if (isCompressed(compressed)) {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
            return IOUtils.toByteArray(new InputStreamReader(gis, "UTF-8"));
        }
        return new byte[512];
    }

    private static boolean isCompressed(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream
                .GZIP_MAGIC >> 8));
    }
}
