package org.anhcraft.spaciouslib.socket.web;

import org.anhcraft.spaciouslib.utils.GZipUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A class helps you to write HTTP response to send it to a browser
 */
public class HTTPResponseWriter {
    private HashMap<String, String> fields = new HashMap<>();
    private List<byte[]> data = new ArrayList<>();
    private String HTTPVersion = "HTTP/2.0";
    private int statusCode = 200;
    private int contentLength = 0;
    private boolean gzip;

    /**
     * Creates a new HTTPResponseWriter instance
     */
    public HTTPResponseWriter(){
        setField("content-length", "0");
        setField("date", new Date().toString());
    }

    public void setGZip(boolean enable){
        if(enable){
            setField("content-encoding", "gzip");
        } else {
            removeField("content-encoding");
        }
        this.gzip = enable;
    }

    private void removeField(String field) {
        fields.remove(field);
    }

    public void setHTTPVersion(String version){
        this.HTTPVersion = version;
    }

    public void setStatusCode(int code){
        this.statusCode = code;
    }

    public void addData(byte[] data){
        if(gzip){
            try {
                data = GZipUtils.compress(data);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        this.data.add(data);
        contentLength += data.length;
        setField("content-length", Integer.toString(contentLength));
    }

    public void addData(String data){
        addData(data.getBytes(StandardCharsets.UTF_8));
    }

    public void setField(String field, String value){
        this.fields.put(field, value);
    }

    public byte[] write() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringBuilder header = new StringBuilder(HTTPVersion + " " + statusCode + "\r\n");
        for(String field : fields.keySet()){
            header.append(field).append(": ").append(fields.get(field)).append("\r\n");
        }
        header.append("\r\n");
        out.write(header.toString().getBytes(StandardCharsets.UTF_8));
        for(byte[] data : data){
            out.write(data);
        }
        return out.toByteArray();
    }
}
