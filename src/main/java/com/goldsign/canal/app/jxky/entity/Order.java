package com.goldsign.canal.app.jxky.entity;

import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.goldsign.canal.entity.BaseCanalEntity;

public class Order extends BaseCanalEntity<Order> {
	private static final long serialVersionUID = 1L;
	String id;
	String name;
	Double price;
	String type;
	
	public Order() {
		super();
	}
	public Order(EventType eventType) {
		super(eventType);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + ", price=" + price + ", type=" + type + "]";
	}
	
}
