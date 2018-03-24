package org.anhcraft.spaciouslib.socket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public abstract class SocketHandler extends Thread {
    protected boolean isStopped;
    protected InputStreamReader in;
    protected OutputStreamWriter out;
    public abstract void close() throws IOException;
}
