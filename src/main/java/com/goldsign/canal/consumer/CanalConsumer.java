package com.goldsign.canal.consumer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.MDC;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.goldsign.canal.base.AbstractCanalConsumer;
import com.goldsign.canal.base.CanalRowChange;
import com.goldsign.canal.dispatcher.MyDispatcher;
import com.goldsign.canal.entity.BaseCanalEntity;
import com.goldsign.canal.processor.BaseProcessor;
import com.goldsign.canal.utils.Global;
import com.goldsign.canal.utils.SpringContextHolder;

public class CanalConsumer extends AbstractCanalConsumer {
	private LinkedBlockingQueue<Entry> customTableQueue = new LinkedBlockingQueue<Entry>();
	private static final ExecutorService threadPool = Executors.newFixedThreadPool(2);
	
	public CanalConsumer(String destination){
        super(destination);
    }

	public CanalConsumer(String destination, CanalConnector connector) {
		super(destination, connector);
	}

	/**
	 * 启动canal client服务
     * 采用异步线程方式生产数据到内存队列
	 */
	public void startClient() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
            	try{
            		connector.connect();
                    connector.subscribe(filter);
	                while (!threadPool.isShutdown() && !threadPool.isTerminated()) {
	                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
	                    long batchId = message.getId();
	                    int size = message.getEntries().size();
	                    if (batchId == -1 || size == 0) {
	                        try {
	                            Thread.sleep(waitingTime);
//	                            LOGGER.info("empty!");
	                        } catch (InterruptedException e) {
	                        	LOGGER.warn("CustomSimpleCanalClient invoke thread interrupted!");
	                        }
	                    } else {
	                        List<Entry> entryList = message.getEntries();
	                        for (Entry entry : entryList) {
	                            try {
	                                customTableQueue.put(entry);
	                            } catch (InterruptedException e) {
	                                //do nothing
	                            	LOGGER.warn("customTableQueue interrupt.", e);
	                            }
	                        }
	                    }
	                    connector.ack(batchId); // 提交确认
	                }
            	}catch (Exception e) {
            		LOGGER.error("startClient error!", e);
				}finally{
					connector.disconnect();
				}
            }
        });
    }
	
    /**
     * 启动内存队列消费线程
     */
    public void startConsume() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Entry entry = null;
                do {
                    try {
                        entry = customTableQueue.take();
                        MDC.put("canal-info", entry.getHeader().getLogfileName().split("!")[0]+"#" +entry.getHeader().getLogfileOffset());
                        session(entry);
                    } catch (InterruptedException e) {
                    	LOGGER.warn("customTableQueue interrupt.", e);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e1) {
                        }
                        continue;
                    }finally{
                    	MDC.remove("canal-info");
                    }
                } while (!threadPool.isShutdown() && !threadPool.isTerminated());

            }
        });
    }
	
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void catChange(Object rowChangeObj) throws Exception {
		 CanalRowChange rowChange =(CanalRowChange) rowChangeObj;
		 
		 for(RowData row:rowChange.getRowData()){
			 	Class<?>  clazz_processor = MyDispatcher.processorAdaptor.get(rowChange.getSchemaName() +"_" + rowChange.getTableName());
			 	
			 	/**方法1[推荐]：通过spring工厂调用*/
			 	BaseProcessor<?,?>  processor = (BaseProcessor) SpringContextHolder.getBean(clazz_processor);
				
				if(processor==null) throw new Exception("找不到对应的processor.");
			 	
//			 	BaseProcessor processor = (BaseProcessor) MyDispatcher.processorMap.get(rowChange.getSchemaName() +"." + rowChange.getTableName());
				
			 	BaseCanalEntity entity;
//				BaseLocalEntity<?> localEntity ;
				try {
					/*数据加工 msg-->Object0*/
					boolean isAfter = rowChange.getEventType().equals(EventType.DELETE) ? false: true;
					entity = (BaseCanalEntity) processor.convertObj(row,rowChange.getEventType(), isAfter);
					if (entity==null) throw new Exception("data convert error."); 
					
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
					throw new Exception("data convert error.") ;
				} 
				
				processor.process(entity);
				
				
				/**方法2：通过java反射调用*/
				/*Method method;
				try {
					method = clazz_processor.getMethod("process", CanalRowChange.class);
					method.setAccessible(true);
					method.invoke(clazz_processor.newInstance(), rowChange );
				}catch (NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					e.printStackTrace();
				}*/
		 }
		 
//		 rowChange.getRowData().forEach((row) -> {
//				
//		 });
		 
	}
	
	public void addHookPoint(){
		/**开发模式增加优雅退出*/
		if("0".equals(Global.getConfig("runModel"))){
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
		}
    	
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {  
            @Override  
            public void run()  
            {  
            	LOGGER.info("call hook.");  
            	LOGGER.info("cunsumer unsubscribe and disconnect...");
            	//connector.unsubscribe();
            	connector.disconnect();
            }  
        }));
	}
}
