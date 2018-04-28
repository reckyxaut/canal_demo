package com.goldsign.canal.app.jxky.processor;

import org.springframework.stereotype.Component;

import com.goldsign.canal.annotation.CanalProcessor;
import com.goldsign.canal.app.jxky.dao.OrderDao;
import com.goldsign.canal.app.jxky.entity.Order;
import com.goldsign.canal.processor.BaseProcessor;
import com.goldsign.canal.utils.Global;


@Component
@CanalProcessor(schema="canal_test",table="oorder")
public class OrderProcessor extends BaseProcessor<Order,OrderDao> {

	@Override
	public Order convertObjLocal(Order entity) throws Exception {
		Global.LOGGER.debug("convert data before:{}",entity.toString());
		Global.LOGGER.debug("convert data after:{}",entity.toString());
		return entity;
	}
}
