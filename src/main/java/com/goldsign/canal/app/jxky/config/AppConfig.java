package com.goldsign.canal.app.jxky.config;




import com.goldsign.canal.annotation.CanalProcessor;
import com.goldsign.canal.dispatcher.MyDispatcher;
import com.goldsign.canal.utils.Global;

public class AppConfig {
	
	public static void initProcessor(String processorPakge) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		 Global.LOGGER.info("init CanalProcessor...");
		 MyDispatcher.initProcessorAdaptor(processorPakge, CanalProcessor.class);
//		 Map<String,Object> maps= SpringContextHolder.getBeansWithAnnotation(CanalProcessor.class);
//		 Map<String,Object> maps =  SpringContextHolder.getBeansWithAnnotation(CanalProcessor.class); 
//		 Global.LOGGER.info(maps.toString());
		 
//		 MyDispatcher.scanProcessorAdaptor(CanalProcessor.class);
//		 Global.LOGGER.info(MyDispatcher.processorMap.toString());
		 Global.LOGGER.info("init CanalProcessor complete.");
	}
	
}
