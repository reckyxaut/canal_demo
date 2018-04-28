package com.goldsign.demo;

import java.io.IOException;

public class TestHook {
	public void start()
	{
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run()
			{
				System.out.println("Execute Hook.....");
			}
		}));
	}
	
	public static void main(String[] args)
	{
		new TestHook().start();
		System.out.println("The Application is doing something");
		
		try {  
            System.in.read();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        System.exit(0); 
	}
}
