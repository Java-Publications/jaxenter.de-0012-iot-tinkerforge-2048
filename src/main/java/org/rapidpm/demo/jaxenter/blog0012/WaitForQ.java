package org.rapidpm.demo.jaxenter.blog0012;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Sven Ruppert on 15.02.14.
 */
public class WaitForQ {

    public static void waitForQ() {
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        final Thread t = new Thread(() -> {
            System.out.println("press Q THEN ENTER to terminate");
            int quit=0;
            while(true){
                try {
                    Thread.sleep(1000);
                    String msg = null;
                    while(true){
                        try{
                            msg=in.readLine();
                        }catch(Exception e){}
                        if(msg != null && msg.equals("Q")) { quit = 1; }
                        if(quit==1) break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }

        });
        t.start();
    }
}
