package com.goldsign.demo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThreedHook {
	protected static final Logger LOGGER = LoggerFactory.getLogger(ThreedHook.class);
	
	public static void startEndPoint(){
		new Thread(new Runnable() {  
            @Override  
            public void run()  
            {  
                try {  
                    System.in.read();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
                System.exit(0); 
            }  
        }).start();
    	
    	
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {  
            @Override  
            public void run()  
            {  
            	LOGGER.info("call hook.");  
            	LOGGER.info("cunsumer unsubscribe and disconnect...");
            	//connector.unsubscribe();
//            	connector.disconnect();
            }  
        }));
	}
}
