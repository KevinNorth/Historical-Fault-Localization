package edu.unl.knorth.historical_fault_localization.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Redirects an arbitrary input stream to STDOUT.
 * <p/>
 * Code borrowed from
 * https://stackoverflow.com/questions/1732455/redirect-process-output-to-stdout
 * <p/>
 * Usage:
 * <p/>
 * <code>
 * Runtime rt = Runtime.getRuntime();<br/>
 * Process proc = rt.exec("javac");<br/>
 * //output both stdout and stderr data from proc to stdout of this process<br/>
 * StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream());<br/>
 * StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream());<br/>
 * errorGobbler.start();<br/>
 * outputGobbler.start();<br/>
 * proc.waitFor();<br/>
 * </code>
 */
public class StreamGobbler extends Thread {
    private final InputStream is;

    // reads everything from is until empty. 
    public StreamGobbler(InputStream is) {
        this.is = is;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ( (line = br.readLine()) != null)
                System.out.println(line);    
        } catch (IOException ioe) {
            ioe.printStackTrace();  
        }
    }
}