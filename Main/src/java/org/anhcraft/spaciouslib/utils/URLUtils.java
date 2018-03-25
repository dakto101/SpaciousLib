package org.anhcraft.spaciouslib.utils;

import java.io.*;
import java.net.*;

public class URLUtils {

    public static BufferedReader get(String u) {
        URL url;

        try {
            url = new URL(u);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "" +
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/51.0.2704.103 Safari/537.36");

            return new BufferedReader(
                    new InputStreamReader(
                            conn.getInputStream()
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean download(String fileurl, String filepath) throws IOException {
        if(new File(filepath).exists()) {
            throw new IOException();
        }
        try {
            URL url = new URL(fileurl);
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            httpConnection.setRequestProperty("User-Agent", "" +
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/51.0.2704.103 Safari/537.36");
            BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fos = new FileOutputStream(filepath);
            BufferedOutputStream bout = new BufferedOutputStream(
                    fos, 1024);
            byte[] data = new byte[1024];
            int x;
            while ((x = in.read(data)) != -1) {
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
