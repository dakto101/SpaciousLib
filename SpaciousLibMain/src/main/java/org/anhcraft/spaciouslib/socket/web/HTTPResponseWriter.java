package org.anhcraft.spaciouslib.socket.web;

import org.anhcraft.spaciouslib.builders.EqualsBuilder;
import org.anhcraft.spaciouslib.builders.HashCodeBuilder;
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

    /**
     * Enables or disables the GZip compression
     * @param enable true if you want to enable
     */
    public void setGZip(boolean enable){
        if(enable){
            setField("content-encoding", "gzip");
        } else {
            removeField("content-encoding");
        }
        this.gzip = enable;
    }

    /**
     * Removes the given field out of this HTTP response
     * @param field a field
     */
    private void removeField(String field) {
        fields.remove(field);
    }

    /**
     * Sets the HTTP version
     * @param version the version
     */
    public void setHTTPVersion(String version){
        this.HTTPVersion = version;
    }

    /**
     * Sets the status code (e.g: 200, 403, 404, 500, etc)
     * @param code a status code
     */
    public void setStatusCode(int code){
        this.statusCode = code;
    }

    /**
     * Adds the given data to the body of this HTTP response
     * @param data an array of bytes
     */
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

    /**
     * Adds the given data to the body of this HTTP response
     * @param data a string
     */
    public void addData(String data){
        addData(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Sets a HTTP header field
     * @param field a field
     * @param value the value of that field
     */
    public void setField(String field, String value){
        this.fields.put(field, value);
    }

    /**
     * Writes this response into a byte array
     * @return an array of bytes
     */
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

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            HTTPResponseWriter h = (HTTPResponseWriter) o;
            return new EqualsBuilder()
                    .append(h.fields, this.fields)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(31, 15)
                .append(this.fields).build();
    }
}
