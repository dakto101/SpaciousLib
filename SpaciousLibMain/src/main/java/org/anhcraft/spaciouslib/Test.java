package org.anhcraft.spaciouslib;

import org.anhcraft.spaciouslib.io.FileManager;
import org.anhcraft.spaciouslib.scheduler.DelayedTask;
import org.anhcraft.spaciouslib.scheduler.TimerTask;
import org.anhcraft.spaciouslib.socket.ServerSocketClientManager;
import org.anhcraft.spaciouslib.socket.ServerSocketHandler;
import org.anhcraft.spaciouslib.socket.ServerSocketManager;
import org.anhcraft.spaciouslib.socket.web.HTTPRequestReader;
import org.anhcraft.spaciouslib.socket.web.HTTPResponseWriter;
import org.anhcraft.spaciouslib.socket.web.WebServerManager;
import org.anhcraft.spaciouslib.utils.TimeUnit;
import org.anhcraft.spaciouslib.utils.TimedList;
import org.anhcraft.spaciouslib.utils.TimedMap;
import org.anhcraft.spaciouslib.utils.TimedSet;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {
    private static TimerTask task;

    public static void main(String[] args){
        System.out.println(Color.GREEN.getGreen());

        task = new TimerTask(() -> System.out.println("Your message..."), 0, 1);
        task.run();
        new DelayedTask(() -> {
            task.stop();
            System.out.println("Stopped.");
        }, (long) TimeUnit.MINUTE.getSeconds()).run();
        /////////////////////////////////////////////////////////////////////////

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
                    File requestedFile = new File(root + "/Test/src/website/" + in.getPath());

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

        //////////////////////////////////////////////////////////////////////

        // initializes the map
        TimedMap<String, Integer> map = new TimedMap<>();
        // puts some data to that map
        map.put("A", 0, 3);
        map.put("B", 1, 5);

        System.out.println("A: " + map.isExpired("A")); // returns false
        try {
            Thread.sleep(4000); // 4 seconds
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        // after 4 seconds...
        System.out.println("A: " + map.isExpired("A")); // returns true
        System.out.println("B: " + map.isExpired("B")); // returns false
        try {
            Thread.sleep(6000); // 6 seconds
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        // after 10 seconds...
        System.out.println("A: " + map.isExpired("A")); // returns true
        System.out.println("B: " + map.isExpired("B")); // returns true

        //////////////////////////////////////////////////////////////////////

        // initializes the list
        TimedList<String> list = new TimedList<>();
        // adds some data to that list
        list.add("A",3);
        list.add("B",5);

        System.out.println(list.size()); // returns 2
        try {
            Thread.sleep(4000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(list.size()); // returns 1

        //////////////////////////////////////////////////////////////////////

        // initializes the set
        TimedSet<String> set = new TimedSet<>();
        // adds some data to thatset
        set.add("A",2);
        set.add("B",3);
        System.out.println(set.size()); // returns 2

        try {
            Thread.sleep(3500);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(set.size()); // returns 0

        //======================================================================================

        // initializes the socket server
        new ServerSocketManager(25565, new ServerSocketHandler() {
            @Override
            public void request(ServerSocketClientManager client, byte[] data) {
                String message = new String(data);
                // handles the command from the client socket
                if(message.equalsIgnoreCase("stop")) {
                    // don't try to use the variable "server" above
                    // there's a method to help you get the main manager
                    try {
                        client.getManager().close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(1);
                    return;
                }
                // prints the sent message
                System.out.println("Client#" + client.getInetAddress().getHostAddress() + " >> " + message);
            }

            @Override
            public void connect(ServerSocketClientManager client) {
                System.out.println(client.getInetAddress().getHostAddress()+" has connected");
            }
        });
    }
}
