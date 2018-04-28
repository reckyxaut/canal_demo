package com.goldsign.canal.dispatcher;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goldsign.canal.annotation.CanalProcessor;
import com.goldsign.canal.processor.BaseProcessor;
import com.goldsign.canal.utils.ClassUtil;
import com.goldsign.canal.utils.SpringContextHolder;

/**
 * 适配器
 * @author johnny
 *
 */
public class MyDispatcher{
//	public static Map<String,BaseDeal> handleMapping = new HashMap<String, BaseDeal>();
	public static Map<String,Class<?>> processorAdaptor = new HashMap<>();
//	public static Map<String, Object> processorMap = new HashMap<>();
	
	/**
	 * 根据注解扫描
	 * @param basePackage
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void initProcessorAdaptor(final String packageName , Class<?> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		List<Class> c_ls = ClassUtil.getAllClassByAnnotation(packageName ,clazz);
		System.out.println("classes all : " + c_ls.size());
		for(Class cz:c_ls){
			System.out.println(cz.getName());
		}
		if(!(c_ls !=null && c_ls.size() > 0)) return ;
		
		StringBuffer buf = new StringBuffer();
		for( Class  c: c_ls){
				CanalProcessor processor = (CanalProcessor) c.getAnnotation(clazz);
				buf.setLength(0); 
				buf.append(processor.schema());
				buf.append("_");
				buf.append(processor.table());
				processorAdaptor.put(buf.toString(), c );
		}
	}
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static void scanProcessorAdaptor(Class<? extends Annotation > calzz) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
//		processorMap = SpringContextHolder.getBeansWithAnnotation(calzz); 
//		System.out.println("111");
//	}
}
