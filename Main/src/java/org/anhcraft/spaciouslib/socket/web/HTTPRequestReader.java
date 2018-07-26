package org.anhcraft.spaciouslib.socket.web;

import net.pieroxy.ua.detection.UserAgentDetectionResult;
import net.pieroxy.ua.detection.UserAgentDetector;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class helps you to read HTTP requests which was sent by a browser
 */
public class HTTPRequestReader{
    private HashMap<String, String> fields = new HashMap<>();
    private List<String> acceptedMIMETypes = new ArrayList<>();
    private List<String> acceptedEncodings = new ArrayList<>();
    private List<String> acceptedLanguages = new ArrayList<>();
    private HashMap<String, String> cookies = new HashMap<>();
    private String userAgent;
    private UserAgentDetectionResult userAgentDetection;
    private HTTPRequestMethod method;
    private String httpVersion;
    private String path;
    private HashMap<String, String> queries = new HashMap<>();
    private StringBuilder content = new StringBuilder();

    /**
     * Creates a new HTTPRequestReader instance
     * @param request a raw HTTP request
     */
    public HTTPRequestReader(String request){
        // format of a request:
        // <REQUESTED HEADER>
        // \r\n
        // <REQUESTED BODY>
        // ------------------------------------
        // example:
        //
        // Accept: text/html\r\n
        // Connection: keep-alive\r\n
        // Content-Length: 12\r\n
        // User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0\r\n
        // \r\n
        // hello server
        //
        String[] data = request.split("\r\n");

        // the body-reading mode
        boolean bodyRead = false;

        main:
        for(String line : data){
            // if the body-reading was enabled, it means that all line afterwards are represented for the body of an HTTP request
            if(bodyRead){
                content.append(line).append("\r\n");
                continue;
            }

            // checks will the body be happen in the next line
            // because between the header and the body, there is a line which writes "r\n"
            else if(line.trim().length() == 0){
                bodyRead = true;
                continue;
            }

            // reads the HTTP headers
            else if(line.startsWith("Accept: ")){
                acceptedMIMETypes = CommonUtils.toList(
                        line.substring("Accept: ".length()).split(","));
            } else if(line.startsWith("Accept-Encoding: ")){
                String[] acceptedEncodings = line.substring("Accept-Encoding: ".length()).split(",");
                for(String en : acceptedEncodings){
                    this.acceptedEncodings.add(en.trim());
                }
            } else if(line.startsWith("Accept-Language: ")){
                acceptedLanguages = CommonUtils.toList(
                        line.substring("Accept-Language: ".length()).split(","));
            } else if(line.startsWith("User-Agent: ")){
                userAgent = line.substring("User-Agent: ".length());
            } else if(line.startsWith("Cookie: ")){
                String[] cookies = line.substring("Cookie: ".length()).split(";");
                for(String ck : cookies){
                    String[] ckr = ck.split("=");
                    this.cookies.put(ckr[0], ckr[1]);
                }
            } else {
                for(HTTPRequestMethod method : HTTPRequestMethod.values()){
                    if(line.startsWith(method.toString()+" ")){
                        this.method = method;
                        String[] ir = line.split(" ");
                        if(3 <= ir.length) {
                            this.httpVersion = ir[2];
                        }
                        if(2 <= ir.length) {
                            String[] ur = ir[1].split("\\?");
                            if(1 <= ur.length) {
                                this.path = ur[0];
                            }
                            if(2 <= ur.length) {
                                String[] queries = ur[1].split("&");
                                for(String query : queries) {
                                    String[] qr = query.split("=");
                                    if(2 <= qr.length) {
                                        this.queries.put(qr[0], qr[1]);
                                    }
                                }
                            }
                        }
                        continue main;
                    }
                }
            }

            String[] lr = line.split(": ");
            if(2 <= lr.length) {
                fields.put(lr[0], line.substring(lr[0].length()+2));
            }
        }
        userAgentDetection = new UserAgentDetector().parseUserAgent(this.userAgent);
        if(method.equals(HTTPRequestMethod.POST)){
            String[] queries = content.toString().split("&");
            for(String query : queries) {
                String[] qr = query.split("=");
                if(2 <= qr.length) {
                    this.queries.put(qr[0], qr[1]);
                }
            }
        }
    }

    public String getUserAgent(){
        return this.userAgent;
    }

    public List<String> getAcceptedMIMETypes(){
        return this.acceptedMIMETypes;
    }

    public List<String> getAcceptedLanguages(){
        return this.acceptedLanguages;
    }

    public List<String> getAcceptedEncodings(){
        return this.acceptedEncodings;
    }

    public HashMap<String, String> getCookies(){
        return this.cookies;
    }

    public UserAgentDetectionResult getUserAgentDetection(){
        return this.userAgentDetection;
    }

    public HTTPRequestMethod getRequestMethod(){
        return this.method;
    }

    public String getHTTPVersion(){
        return this.httpVersion;
    }

    public HashMap<String, String> getQueries(){
        return this.queries;
    }

    public String getPath(){
        return this.path;
    }

    public String getContent(){
        return this.content.toString();
    }

    public HashMap<String, String> getFields(){
        return this.fields;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            HTTPRequestReader h = (HTTPRequestReader) o;
            return new EqualsBuilder()
                    .append(h.fields, this.fields)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(35, 19)
                .append(this.fields).toHashCode();
    }
}
