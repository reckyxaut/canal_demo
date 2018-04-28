package com.goldsign.canal_demo;

import java.io.File;

//import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;



/**
 * Unit test for simple App.
 */
public class AppTest{
	Logger LOGGER = LoggerFactory.getLogger(AppTest.class);
	@org.junit.Test
	public void Test(){ 
		
		String jarEntryName= "com/goldsign/canal/app/jxky/processor/";
		System.out.println(File.separator);
		int xx = jarEntryName.lastIndexOf('/');
		System.out.println(xx +"");
		System.out.println(jarEntryName.substring(0, xx));
		
		
	    MDC.clear();  
	    MDC.put("sessionId" , "f9e287fad9e84cff8b2c2f2ed92adbe6");  
	    MDC.put("siteName" , "北京");  
	    MDC.put("userName" , "userwyh");  
	    LOGGER.info("测试MDC打印一");  
	           
	    MDC.put("mobile" , "110");  
	    LOGGER. error("测试MDC打印二");  
	           
	    MDC.put("mchName", "商户名称");  
	    LOGGER.debug("测试MDC打印三");  
	           
	}  
}
