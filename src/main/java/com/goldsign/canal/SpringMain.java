package com.goldsign.canal;


import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.goldsign.canal.app.jxky.config.AppConfig;
import com.goldsign.canal.consumer.CanalConsumer;
import com.goldsign.canal.utils.Global;
import com.goldsign.canal.utils.SpringContextHolder;

public class SpringMain {
	public static final Logger LOGGER = LoggerFactory.getLogger(SpringMain.class);
	public static void main(String[] args) {
		LOGGER.info("canal consumer starting...");
		SpringContextHolder.reloadContext();
		try {
			String basePackage = Global.getConfig("scan.base.package");
			AppConfig.initProcessor(basePackage);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return ; 
		} catch (InstantiationException e) {
			e.printStackTrace();
			return ; 
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return ; 
		}
		
		String destination = Global.getExtendConfig("canal.instance.destination","canal-config",  "canal.properties");
		String ip = Global.getExtendConfig("canal.server.ip","canal-config",  "canal.properties");
        int port = Integer.valueOf(Global.getExtendConfig("canal.server.port", "canal-config", "canal.properties"));
        String username = Global.getExtendConfig("canal.username","canal-config",  "canal.properties");
        String psw = Global.getExtendConfig("canal.password","canal-config",  "canal.properties");
        
        /**单机模式*/
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(ip, port),
															            destination,
															            username,
															            psw);
        
        
        /**cluster模式的客户端链接*/
//        String zkServers = Global.getExtendConfig("canal.zkServers","canal-config",  "canal.properties");
//        CanalConnector connector = CanalConnectors.newClusterConnector( zkServers,
//															            destination,
//															            username,
//															            psw );
        
        CanalConsumer consumer = new CanalConsumer(destination,connector);
        
        consumer.addHookPoint();
        
//      consumer.start(); 在一个线程中拉取并消费数据
        
        /**1:客户端线程1：消费内存队列中的数据*/
        consumer.startConsume();
        
        consumer.startClient();
        /**2:客户端线程2：将数据放到内存队列*/
        
        Global.LOGGER.info("canal consumer start listening[{}]" , destination);
        
	}

	
	
}
