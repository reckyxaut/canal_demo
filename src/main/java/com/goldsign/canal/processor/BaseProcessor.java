package com.goldsign.canal.processor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.goldsign.canal.dao.CrudDao;
import com.goldsign.canal.entity.BaseCanalEntity;

public class BaseProcessor<E extends BaseCanalEntity<E> , T extends CrudDao<E>> {
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseProcessor.class);
	private Map<String, Map<String, Field>> clzFieldsCached;  
	@Autowired
	T pao;
	
	private DefaultConversionService convertor = new DefaultConversionService() {  
        {  
//            addConverter(new DateConverter());  
        }  
    }; 
	
	/**数据转换1
	 * @throws Exception */
	@SuppressWarnings("unchecked")
	public  E convertObj(RowData rowData,EventType eventType , boolean isAfter) throws Exception{
		Class<?> clz = null;
		Type genericSuperclass  = this.getClass().getGenericSuperclass();
		if(genericSuperclass instanceof ParameterizedType){  
            //参数化类型  
            ParameterizedType parameterizedType= (ParameterizedType) genericSuperclass;  
            //返回表示此类型实际类型参数的 Type 对象的数组  
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();  
            clz =  (Class<E>)actualTypeArguments[0];  
        }else{  
        	clz= (Class<E>)genericSuperclass;  
        }  
		
		LOGGER.debug("convert to Object:{}",clz.getSimpleName());
		
		if (clzFieldsCached == null) {  
            clzFieldsCached = new HashMap<String, Map<String, Field>>();  
        }  
  
        Map<String, Field> fieldscached = clzFieldsCached.get(clz.getName());  
        if (fieldscached == null || fieldscached.size() <= 0) {  
            fieldscached = new HashMap<String, Field>();  
            for (Field field : clz.getDeclaredFields()) {  
            	if(Modifier.isFinal(field.getModifiers())) continue;
                field.setAccessible(true);  
                fieldscached.put(field.getName().toLowerCase(), field);  
            }  
            clzFieldsCached.put(clz.getName(), fieldscached);  
        }  
        
		E bean = (E) clz.newInstance();
		List<Column> cols = isAfter ? rowData.getAfterColumnsList() : rowData.getBeforeColumnsList();  
		if (cols == null || cols.size() <= 0)  return null; 
//		int count = 0;  
        for (Column col : cols) {
        	String type = col.getMysqlType(); 
        	String name = col.getName();  
        	String value = col.getValue();  //getMysqlType()
        	boolean isKey = col.getIsKey();
        	LOGGER.debug("Column[Type={},Name={},Value={},isKey={}]" ,type,name,value,isKey);
            Field field = fieldscached.get(name.toLowerCase());  
            if (field == null) {  
                continue;  
            }  

            if (StringUtils.isNotEmpty(value)) {  
                Object nvalue = convertor.convert(value, field.getType());  
                field.set(bean, nvalue);  
            }  
//            count++;  
        } 
        
//      if (count != fieldscached.size()) {  
//            return null;  
//      }
        
        bean.setEventType(eventType);
        
        LOGGER.debug(bean.toString());
        
        convertObjLocal(bean);
        
        return bean;
	}
	
	public  E convertObjLocal(E entity) throws Exception{ return entity;}
	
//	public abstract  BaseLocalEntity<?> convertObjLocal(BaseCanalEntity<?> entity) throws Exception;
	
	public void process(BaseCanalEntity<E> entity){
		switch (entity.getEventType()) {
        case DELETE:
        	LOGGER.debug("DELETE");
        	pao.delete(entity);
            break;
        case INSERT:
        	LOGGER.debug("INSERT");
        	pao.insert(entity);
        	break;
        case UPDATE:
        	LOGGER.debug("UPDATE");
        	pao.update(entity);
        	break;
        default:
        	LOGGER.debug("whenOthers");
        	pao.select(entity);
        	break;
        	//whenOthers(entry);
		}
	}
	
	/*public void process(BaseLocalEntity<T> entity,EventType eventType){
		switch (eventType) {
        case DELETE:
        	LOGGER.debug("DELETE");
        	pao.delete("11");
            break;
        case INSERT:
        	LOGGER.debug("INSERT");
        	pao.insert("22");
        	break;
        case UPDATE:
        	LOGGER.debug("UPDATE");
        	pao.update("33");
        	break;
        default:
        	LOGGER.debug("whenOthers");
        	pao.select("00");
        	break;
        	//whenOthers(entry);
		}
	}*/
	
//	public void process(CanalRowChange rowData){
//		LOGGER.debug("sql:{}",rowData.getSql());
//        switch (rowData.getEventType()) {
//            case DELETE:
//            	LOGGER.debug("DELETE");
//            	pao.delete("1");
//                break;
//            case INSERT:
//            	LOGGER.debug("INSERT");
//            	pao.insert("2");
//            	break;
//            case UPDATE:
//            	LOGGER.debug("UPDATE");
//            	pao.update("3");
//            	break;
//            default:
//            	LOGGER.debug("whenOthers");
//            	pao.select("0");
//            	break;
//        }
//	}
	

	/*@Override
	public T insert(String id) {
		processor
	}

	@Override
	public T update(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	
    public static void printColumn(List<Column> columns) {  
    	/* for (Column column : columns) {  
            System.out.println(column.getName() + ":" + column.getValue() + "    update=" + column.getUpdated());  
        }  */
        String line = columns.stream()
                .map(column -> column.getName() + "=" + column.getValue())
                .collect(Collectors.joining(","));
        System.out.println(line);
    } 
    
}
