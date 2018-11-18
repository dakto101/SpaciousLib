import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.socket.ClientSocketManager;
import org.anhcraft.spaciouslib.socket.ServerSocketClientManager;
import org.anhcraft.spaciouslib.socket.ServerSocketHandler;
import org.anhcraft.spaciouslib.socket.ServerSocketManager;
import org.anhcraft.spaciouslib.socket.web.HTTPRequestReader;
import org.anhcraft.spaciouslib.socket.web.HTTPResponseWriter;
import org.anhcraft.spaciouslib.socket.web.WebServerManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Test5 {
    public static class Server {
        public static void main(String[] args){
            ServerSocketManager sv = new ServerSocketManager(14474, new ServerSocketHandler() {
                @Override
                public void request(ServerSocketClientManager client, byte[] data) {
                    client.getManager().getClients().forEach(serverSocketClientManager -> {
                        try {
                            serverSocketClientManager.send("#"+client.getId()+": "+ new String(data, StandardCharsets.UTF_8));
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                public void connect(ServerSocketClientManager client) {
                    System.out.println("Client #"+client.getId()+" connected!");
                }
            });
            Scanner s = new Scanner(System.in);
            while(s.hasNextLine()){
                String x = s.nextLine();
                sv.getClients().forEach(serverSocketClientManager -> {
                    try {
                        serverSocketClientManager.send("SYSTEM: "+x);
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public static class Client {
        public static void main(String[] args) {
            ClientSocketManager c = new ClientSocketManager("localhost",14474, (manager, data) -> {
                System.out.println(new String(data, StandardCharsets.UTF_8));
            });
            Scanner s = new Scanner(System.in);
            while(s.hasNextLine()){
                try {
                    c.send(s.nextLine());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Web{
        public static void main(String[] args) {
            // creates a web server
            new WebServerManager(new ServerSocketHandler() {
                @Override
                public void request(ServerSocketClientManager client, byte[] data) {
                    // [WARNING] the browser only requests the data one time
                    // it means we allowed to use this method

                    try {
                        // uses HTTPRequestReader to read the request
                        HTTPRequestReader in = new HTTPRequestReader(new String(data));

                        // gets the requested file object
                        // uses the current working directory as the root directory of the website
                        Path root = Paths.get("").normalize().toAbsolutePath();
                        File requestedFile = new File(root + "/" + in.getPath());

                        System.out.println("#"+client.getId()+": "+requestedFile.getAbsolutePath());

                        HTTPResponseWriter output = new HTTPResponseWriter();
                        output.setGZip(true); // enables GZip compression
                        output.setField("content-language", "en");
                        output.setField("server", "HTTP server (unknown)");

                        if(requestedFile.exists()) {
                            // if a browser requests with the path of a directory,
                            // maybe it wants to get the index.html file.
                            // https://en.wikipedia.org/wiki/Webserver_directory_index
                            if(requestedFile.isDirectory()) {
                                File file = new File(requestedFile, "index.html");
                                output.setField("content-type", "text/html; charset=UTF-8");
                                if(file.exists()) {
                                    // reads the requested file
                                    output.addData(new FileManager(file).read());
                                } else {
                                    output.addData("You cannot access this directory!");
                                    output.setStatusCode(403);
                                }
                            } else if(requestedFile.isFile() && !requestedFile.isHidden()){
                                if(requestedFile.getName().endsWith(".html")){
                                    output.setField("content-type", "text/html; charset=UTF-8");
                                } else if(requestedFile.getName().endsWith(".js")){
                                    output.setField(
                                            "content-type", "text/javascript; charset=UTF-8");
                                } else if(requestedFile.getName().endsWith(".css")){
                                    output.setField("content-type", "text/css; charset=UTF-8");
                                }
                                // reads the requested file
                                output.addData(new FileManager(requestedFile).read());
                            }
                        } else {
                            output.addData("Couldn't found the requested file or directory");
                            output.setStatusCode(404);
                        }

                        client.send(output.write());

                        // closes the connection to tell the browser handles the response
                        client.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void connect(ServerSocketClientManager client) {
                }
            });
        }
    }
}
